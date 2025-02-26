package com.sin.inforsystembackend.controller;

import com.sin.inforsystembackend.dto.*;
import com.sin.inforsystembackend.entity.Notification;
import com.sin.inforsystembackend.entity.User;
import com.sin.inforsystembackend.entity.Department;
import com.sin.inforsystembackend.service.NotificationService;
import com.sin.inforsystembackend.repository.UserRepository;
import com.sin.inforsystembackend.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 批量创建通知
     * @param notifications 通知请求列表
     * @return 创建的通知列表
     */
    @PostMapping("/batch-insert")
    public ResponseEntity<List<Notification>> batchInsertNotifications(@RequestBody List<NotificationRequestDTO> notifications) {
        List<Notification> createdNotifications = new ArrayList<>();

        for (NotificationRequestDTO notificationRequest : notifications) {
            // 手动验证参数的完整性
            if (!isNotificationRequestValid(notificationRequest)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // 构建通知实体
            Notification notification = new Notification();
            notification.setTitle(notificationRequest.getTitle());
            notification.setContent(notificationRequest.getContent());
            notification.setNotificationType(Notification.NotificationType.valueOf(notificationRequest.getType()));

            // 查找用户和部门，确保获取到有效的实体
            User sender = userRepository.findById(notificationRequest.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + notificationRequest.getSenderId()));
            Department department = departmentRepository.findById(notificationRequest.getSenderDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + notificationRequest.getSenderDepartmentId()));

            notification.setSender(sender);
            notification.setSenderDepartment(department);
            notification.setStatus(Notification.Status.SENT);

            // 创建通知并生成索引
            Notification createdNotification = notificationService.createAndDistributeNotification(
                    notification,
                    notificationRequest.getRecipientIdentifiers(),
                    notificationRequest.getRecipientType()
            );
            createdNotifications.add(createdNotification);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotifications);
    }

    /**
     * 验证 NotificationRequestDTO 的完整性和有效性
     * @param request 通知请求 DTO
     * @return 是否有效
     */
    private boolean isNotificationRequestValid(NotificationRequestDTO request) {
        if (request == null) {
            return false;
        }
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return false;
        }
        if (request.getContent() == null || request.getContent().isEmpty()) {
            return false;
        }
        if (request.getType() == null || request.getType().isEmpty()) {
            return false;
        }
        if (request.getSenderId() == null) {
            return false;
        }
        if (request.getSenderDepartmentId() == null) {
            return false;
        }
        if (request.getRecipientIdentifiers() == null || request.getRecipientIdentifiers().isEmpty()) {
            return false;
        }
        if (request.getRecipientType() == null || request.getRecipientType().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 根据搜索条件查询通知
     * @param searchDTO 搜索条件封装对象
     * @return 匹配的通知列表
     */
    @PostMapping("/search")
    public ResponseEntity<List<NotificationDTO>> searchNotifications(@RequestBody NotificationSearchDTO searchDTO) {
        if (searchDTO.getRecipientId() == null) {
            logger.error("Recipient ID is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        logger.info("Searching notifications with recipient ID: {}", searchDTO.getRecipientId());
        List<NotificationDTO> notifications = notificationService.searchNotifications(searchDTO);
        return ResponseEntity.ok(notifications);
    }

    /**
     * 根据通知ID获取通知详情
     * @param notificationId 通知ID
     * @return 通知详细信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable("id") Integer notificationId) {
        NotificationDTO notificationDTO = notificationService.getNotificationById(notificationId);
        if (notificationDTO != null) {
            return ResponseEntity.ok(notificationDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    /**
     * 发送通知
     * @param sendDTO 包含通知和接收者信息的 DTO
     * @return 创建的通知对象
     */
    @PostMapping("/send")
    public ResponseEntity<NotificationDTO> sendNotification(@RequestBody NotificationSendDTO sendDTO) {
        Notification notification = notificationService.sendNotification(sendDTO);
        if (notification != null) {
            NotificationDTO notificationDTO = convertToDTO(notification);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificationDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    /**
     * 修改通知内容
     * @param notificationId 通知ID
     * @param updatedNotification 更新后的通知内容
     * @return 更新后的通知
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotificationContent(@PathVariable("id") Integer notificationId,
                                                                     @RequestBody NotificationUpdateDTO updatedNotification) {
        // 检查请求数据的有效性
        if (updatedNotification.getTitle() == null || updatedNotification.getContent() == null) {
            return ResponseEntity.badRequest().body(null);  // 如果标题或内容为空，返回400
        }

        try {
            // 调用 service 层方法更新通知
            Notification updated = notificationService.updateNotification(notificationId, convertToEntity(updatedNotification));

            // 转换为 DTO 并返回
            NotificationDTO notificationDTO = convertToDTO(updated);
            return ResponseEntity.ok(notificationDTO);
        } catch (IllegalArgumentException ex) {
            // 如果通知不存在，返回 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 将 NotificationUpdateDTO 转换为 Notification 实体
    private Notification convertToEntity(NotificationUpdateDTO updatedNotification) {
        Notification notification = new Notification();
        notification.setTitle(updatedNotification.getTitle());
        notification.setContent(updatedNotification.getContent());
        notification.setNotificationType(Notification.NotificationType.valueOf(updatedNotification.getType())); // 假设你有一个 NotificationType 枚举
        notification.setSentTime(updatedNotification.getSentTime());
        notification.setSender(userRepository.findById(updatedNotification.getSenderId()).orElse(null));
        notification.setSenderDepartment(departmentRepository.findById(updatedNotification.getSenderDepartmentId()).orElse(null));
        notification.setStatus(updatedNotification.getStatus());
        return notification;
    }


    // 将 Notification 转换为 NotificationDTO
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getNotificationId());
        dto.setTitle(notification.getTitle());
        dto.setContent(notification.getContent());
        dto.setType(notification.getNotificationType().toString());
        dto.setSenderDepartment(notification.getSenderDepartment() != null
                ? notification.getSenderDepartment().getDepartmentName()
                : "无部门");
        dto.setSentTime(notification.getSentTime());
        return dto;
    }

}