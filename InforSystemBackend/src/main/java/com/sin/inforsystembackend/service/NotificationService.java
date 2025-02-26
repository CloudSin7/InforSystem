package com.sin.inforsystembackend.service;

import com.sin.inforsystembackend.dto.NotificationDTO;
import com.sin.inforsystembackend.dto.NotificationSearchDTO;
import com.sin.inforsystembackend.dto.NotificationSendDTO;
import com.sin.inforsystembackend.entity.*;
import com.sin.inforsystembackend.repository.NotificationIndexRepository;
import com.sin.inforsystembackend.repository.NotificationRepository;
import com.sin.inforsystembackend.repository.NotificationRecipientRepository;
import com.sin.inforsystembackend.repository.UserDepartmentRepository;
import com.sin.inforsystembackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationRecipientRepository notificationRecipientRepository;

    @Autowired
    private NotificationIndexRepository notificationIndexRepository;

    @Autowired
    private UserDepartmentRepository userDepartmentRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRecipientService notificationRecipientService;

    @Autowired
    private NotificationIndexService notificationIndexService;
    /**
     * 创建通知并生成通知索引
     *
     * @param notification         通知实体
     * @param recipientIdentifiers 接收者标识列表
     * @param recipientType        接收者类型
     * @return 保存后的通知对象
     */
    public Notification createAndDistributeNotification(Notification notification, List<Long> recipientIdentifiers, String recipientType) {
        // 设置发送时间
        notification.setSentTime(LocalDateTime.now());
        logger.info("Setting sent time to: {}", LocalDateTime.now());
        // 保存通知记录
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Saved notification with id: {}", savedNotification.getNotificationId());

        // 根据接收者类型生成索引
        switch (recipientType.toUpperCase()) {
            case "INDIVIDUAL":
                List<User> users = recipientIdentifiers.stream()
                        .map(id -> userRepository.findById(id.intValue())
                                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id)))
                        .collect(Collectors.toList());
                logger.info("Found {} users for individual recipient type", users.size());
                generateNotificationIndex(savedNotification, users, NotificationIndex.RecipientType.INDIVIDUAL);
                break;

            case "DEPARTMENT":
                recipientIdentifiers.forEach(departmentId -> {
                    List<User> departmentUsers = userDepartmentRepository.findUsersByDepartmentId(departmentId.intValue());
                    logger.info("Found {} users for department with id: {}", departmentUsers.size(), departmentId);
                    generateNotificationIndex(savedNotification, departmentUsers, NotificationIndex.RecipientType.DEPARTMENT);
                });
                break;

            case "ALL_STUDENTS":
                List<User> students = userDepartmentRepository.findUsersByRole("STUDENT");
                logger.info("Found {} students", students.size());
                generateNotificationIndex(savedNotification, students, NotificationIndex.RecipientType.ALL_STUDENTS);
                break;

            case "ALL_TEACHERS":
                List<User> teachers = userDepartmentRepository.findUsersByRole("TEACHER");
                logger.info("Found {} teachers", teachers);
                generateNotificationIndex(savedNotification, teachers, NotificationIndex.RecipientType.ALL_TEACHERS);
                break;

            default:
                logger.error("Unsupported recipient type: {}", recipientType);
                throw new IllegalArgumentException("Unsupported recipient type: " + recipientType);
        }

        return savedNotification;
    }

    /**
     * 生成通知索引
     *
     * @param savedNotification 保存后的通知
     * @param users             接收用户列表
     * @param recipientType     接收者类型
     */
    private void generateNotificationIndex(Notification savedNotification, List<User> users, NotificationIndex.RecipientType recipientType) {
        users.forEach(user -> {
            NotificationIndex index = new NotificationIndex();
            index.setNotification(savedNotification);
            index.setRecipient(user);
            index.setNotificationType(savedNotification.getNotificationType());
            index.setRecipientType(recipientType);
            notificationIndexRepository.save(index);
            logger.info("Saved notification index for user: {}", user.getUserId());
        });
    }

    /**
     * 根据传入的查询条件查询通知
     * @param searchDTO 搜索条件封装对象
     * @return 匹配的通知DTO列表
     */
    public List<NotificationDTO> searchNotifications(NotificationSearchDTO searchDTO) {
        logger.info("Searching notifications with search DTO: {}", searchDTO);

        // 根据条件查询所有通知
        List<Notification> notifications = getNotificationsBySearchCriteria(searchDTO);
        logger.info("Found {} notifications based on search criteria", notifications.size());

        // 根据登录用户ID筛选，仅保留匹配的通知
        Integer recipientId = searchDTO.getRecipientId();
        if (recipientId == null) {
            throw new IllegalArgumentException("Recipient ID cannot be null");
        }

        // 查询该用户的所有 NotificationIndex
        List<NotificationIndex> notificationIndexes = notificationIndexRepository.findByRecipient_UserId(recipientId);

        // 提取该用户可以访问的通知 ID
        Set<Integer> allowedNotificationIds = notificationIndexes.stream()
                .map(index -> index.getNotification().getNotificationId()) // 通过关联获取通知 ID
                .collect(Collectors.toSet());
        logger.info("Filtered notifications for recipient ID {}: {}", recipientId, allowedNotificationIds);

        // 过滤通知，仅保留符合条件的通知
        List<Notification> filteredNotifications = notifications.stream()
                .filter(notification -> allowedNotificationIds.contains(notification.getNotificationId()))
                .collect(Collectors.toList());

        // 将实体列表转换为 DTO 列表并返回
        return filteredNotifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据搜索条件查询通知
     * @param searchDTO 搜索条件
     * @return 匹配的通知列表
     */
    private List<Notification> getNotificationsBySearchCriteria(NotificationSearchDTO searchDTO) {
        List<Notification> notifications;

        // 按照不同的搜索条件进行查询
        if (searchDTO.getTitle() != null && !searchDTO.getTitle().isEmpty()) {
            logger.info("Searching by title: {}", searchDTO.getTitle());
            notifications = notificationRepository.findByTitleContaining(searchDTO.getTitle());
        } else if (searchDTO.getDepartment() != null && !searchDTO.getDepartment().isEmpty()) {
            logger.info("Searching by department: {}", searchDTO.getDepartment());
            notifications = notificationRepository.findBySenderDepartment_DepartmentName(searchDTO.getDepartment());
        } else if (searchDTO.getNotificationType() != null && !searchDTO.getNotificationType().isEmpty()) {
            logger.info("Searching by notification type: {}", searchDTO.getNotificationType());
            notifications = notificationRepository.findByNotificationType(
                    Notification.NotificationType.valueOf(searchDTO.getNotificationType()));
        } else if (searchDTO.getStartDate() != null && searchDTO.getEndDate() != null) {
            logger.info("Searching by start date: {} and end date: {}", searchDTO.getStartDate(), searchDTO.getEndDate());
            notifications = notificationRepository.findBySentTimeBetween(searchDTO.getStartDate(), searchDTO.getEndDate());
        } else {
            logger.info("No specific search criteria, fetching all notifications");
            notifications = notificationRepository.findAll();  // 如果没有任何筛选条件，查询所有通知
        }
        logger.info("Found {} notifications", notifications.size());
        return notifications;
    }

    /**
     * 根据通知ID获取通知详情
     * @param notificationId 通知ID
     * @return 通知DTO
     */
    public NotificationDTO getNotificationById(Integer notificationId) {
        return notificationRepository.findById(notificationId)
                .map(this::convertToDTO)
                .orElse(null);
    }



    /**
     * 将 Notification 实体对象转换为 NotificationDTO。
     * @param notification 通知实体对象
     * @return 转换后的 NotificationDTO
     */
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getNotificationId());
        dto.setTitle(notification.getTitle());
        dto.setContent(notification.getContent());
        dto.setType(notification.getNotificationType().toString());
        dto.setSentTime(notification.getSentTime());

        //将发件部门信息转换并设置到 DTO 中
        if (notification.getSenderDepartment() != null) {
            dto.setSenderDepartment(notification.getSenderDepartment().getDepartmentName());
        }

        logger.info("Converted notification to DTO: {}", dto);
        return dto;
    }

    /**
     * 更新通知记录
     * @param notificationId 通知的 ID
     * @param updatedNotification 更新后的通知对象
     * @return 更新后的通知对象
     */
    public Notification updateNotification(Integer notificationId, Notification updatedNotification) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notification.setTitle(updatedNotification.getTitle());
                    notification.setContent(updatedNotification.getContent());
                    notification.setNotificationType(updatedNotification.getNotificationType());
                    notification.setSentTime(updatedNotification.getSentTime());
                    notification.setSender(updatedNotification.getSender());
                    notification.setSenderDepartment(updatedNotification.getSenderDepartment());
                    notification.setStatus(updatedNotification.getStatus());
                    return notificationRepository.save(notification);
                })
                .orElseThrow(() -> new IllegalArgumentException("通知不存在，ID: " + notificationId));
    }


    /**
     * 发送通知
     * @param notificationSendDTO 包含通知和接收者信息的 DTO
     * @return 创建的通知对象
     */
    @Transactional
    public Notification sendNotification(NotificationSendDTO notificationSendDTO) {
        logger.info("Received recipientIdentifier: {}", notificationSendDTO.getRecipientIdentifier());
        // Step 1: 解析 DTO 并创建 Notification 实体
        Notification notification = new Notification();
        notification.setTitle(notificationSendDTO.getTitle());
        notification.setContent(notificationSendDTO.getContent());
        notification.setNotificationType(Notification.NotificationType.valueOf(notificationSendDTO.getNotificationType()));
        notification.setSender(userService.getUserById(notificationSendDTO.getSenderId()));
        notification.setSenderDepartment(departmentService.getDepartmentById(notificationSendDTO.getSenderDepartmentId()));
        notification.setSentTime(LocalDateTime.now());
        notification.setStatus(Notification.Status.SENT);


        // Step 2: 创建 Notification 记录
        Notification savedNotification = notificationRepository.save(notification);

        // Step 3: 创建 NotificationRecipient 记录
        NotificationRecipient notificationRecipient = new NotificationRecipient();
        notificationRecipient.setNotification(savedNotification);
        notificationRecipient.setRecipientType(NotificationRecipient.RecipientType.valueOf(notificationSendDTO.getRecipientType()));
        Long recipientIdentifier = notificationSendDTO.getRecipientIdentifier();
        notificationRecipient.setRecipientIdentifier(recipientIdentifier);
        if (recipientIdentifier == null) {
            throw new IllegalArgumentException("Recipient identifier cannot be null");
        }
        notificationRecipientService.createRecipient(notificationRecipient);


        logger.info("Attempting to fetch user or department with recipientIdentifier: " + notificationRecipient.getRecipientId());

        // Step 4: 根据 recipientType 和 recipientIdentifier 生成 NotificationIndex 记录
        String recipientType = notificationSendDTO.getRecipientType();


        if ("INDIVIDUAL".equalsIgnoreCase(recipientType)) {
            // 个人通知，直接创建一个索引
            User recipient = userService.getUserById(recipientIdentifier.intValue());
            createNotificationIndex(savedNotification, recipient);

        } else if ("DEPARTMENT".equalsIgnoreCase(recipientType)) {
            // 部门通知，查询部门下的所有用户
            List<User> departmentUsers = departmentService.getUsersByDepartmentId(recipientIdentifier.intValue());
            departmentUsers.forEach(user -> createNotificationIndex(savedNotification, user));

        } else if ("ALL_STUDENTS".equalsIgnoreCase(recipientType)) {
            // 全体学生通知，查询所有学生
            List<User> students = userService.getUsersByType(User.UserType.STUDENT);
            students.forEach(user -> createNotificationIndex(savedNotification, user));

        } else if ("ALL_TEACHERS".equalsIgnoreCase(recipientType)) {
            // 全体老师通知，查询所有老师
            List<User> teachers = userService.getUsersByType(User.UserType.TEACHER);
            teachers.forEach(user -> createNotificationIndex(savedNotification, user));
        }

        return savedNotification;
    }

    /**
     * 创建通知索引记录
     * @param notification 通知对象
     * @param recipient 接收者对象
     */
    private void createNotificationIndex(Notification notification, User recipient) {
        NotificationIndex notificationIndex = new NotificationIndex();
        notificationIndex.setNotification(notification);
        notificationIndex.setRecipient(recipient);
        notificationIndex.setNotificationType(notification.getNotificationType());
        notificationIndex.setRecipientType(NotificationIndex.RecipientType.INDIVIDUAL); // 这里的类型也可以根据需求调整
        notificationIndexService.createNotificationIndex(notificationIndex);
    }
}