package com.sin.inforsystembackend.service;

import com.sin.inforsystembackend.dto.GroupedNotificationDTO;
import com.sin.inforsystembackend.dto.NotificationDTO;
import com.sin.inforsystembackend.entity.Notification;
import com.sin.inforsystembackend.entity.NotificationIndex;
import com.sin.inforsystembackend.entity.NotificationRecipient;
import com.sin.inforsystembackend.repository.NotificationIndexRepository;
import com.sin.inforsystembackend.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationIndexService {

    private final NotificationIndexRepository notificationIndexRepository;

    @Autowired
    public NotificationIndexService(NotificationIndexRepository notificationIndexRepository) {
        this.notificationIndexRepository = notificationIndexRepository;
    }

    /**
     * 根据接收者 ID 获取其所有通知 DTO
     *
     * @param recipientId 接收者 ID
     * @return 通知 DTO 列表
     */
    public List<NotificationDTO> getNotificationDTOsByRecipient(Integer recipientId) {
        List<NotificationIndex> notificationIndexes = notificationIndexRepository.findByRecipient_UserId(recipientId);
        return convertNotificationIndexesToDTOs(notificationIndexes);
    }

    /**
     * 查询未读通知 DTO
     *
     * @param recipientId 接收者 ID
     * @return 未读通知 DTO 列表
     */
    public List<NotificationDTO> getUnreadNotificationDTOsByRecipient(Integer recipientId) {
        List<NotificationIndex> unreadNotifications = notificationIndexRepository.findByReadStatusFalseAndRecipient_UserId(recipientId);
        return convertNotificationIndexesToDTOs(unreadNotifications);
    }

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知 ID
     * @param recipientId    接收者 ID
     * @return 如果更新成功返回 true，否则返回 false
     */
    public boolean markNotificationAsRead(Integer notificationId, Integer recipientId) {
        Optional<NotificationIndex> notificationIndexOptional = notificationIndexRepository
                .findByNotification_NotificationIdAndRecipient_UserId(notificationId, recipientId);

        if (notificationIndexOptional.isEmpty()) {
            return false;
        }

        NotificationIndex notificationIndex = notificationIndexOptional.get();
        if (!notificationIndex.isReadStatus()) {
            notificationIndex.setReadStatus(true);
            notificationIndex.setReadTime(LocalDateTime.now());
            notificationIndexRepository.save(notificationIndex);
            return true;
        }
        return false;
    }

    /**
     * 分页查询通知索引 DTO
     *
     * @param recipientId 接收者 ID
     * @param pageable    分页信息
     * @return 分页的通知 DTO 列表
     */
    public Page<NotificationDTO> getNotificationIndexesByRecipientWithPagination(Integer recipientId, PageRequest pageable) {
        Page<NotificationIndex> notificationIndexPage = notificationIndexRepository.findByRecipient_UserId(recipientId, pageable);
        return notificationIndexPage.map(DtoConverter::convertToNotificationDTO);
    }

    /**
     * 将 NotificationIndex 列表转换为 NotificationDTO 列表
     *
     * @param indexes 通知索引列表
     * @return 通知 DTO 列表
     */
    private List<NotificationDTO> convertNotificationIndexesToDTOs(List<NotificationIndex> indexes) {
        return indexes.stream().map(DtoConverter::convertToNotificationDTO).collect(Collectors.toList());
    }


    /**
     * 获取用户接收到的所有通知，并按通知类型分类
     *
     * @param recipientId 接收者 ID
     * @return 分类后的通知 DTO 列表
     */
    public List<GroupedNotificationDTO> getNotificationsGroupedByType(Integer recipientId) {
        // 查询用户所有通知索引
        List<NotificationIndex> notificationIndexes = notificationIndexRepository.findByRecipient_UserId(recipientId);

        // 按类型分类
        Map<String, List<NotificationDTO>> groupedNotifications = notificationIndexes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.groupingBy(NotificationDTO::getType)); // 按通知类型分组

        // 转换为 GroupedNotificationDTO 列表
        return groupedNotifications.entrySet().stream()
                .map(entry -> {
                    GroupedNotificationDTO groupedDTO = new GroupedNotificationDTO();
                    groupedDTO.setNotificationType(entry.getKey());
                    groupedDTO.setNotifications(entry.getValue());
                    return groupedDTO;
                })
                .collect(Collectors.toList());
    }


    /**
     * 将 NotificationIndex 转换为 NotificationDTO
     *
     * @param index 通知索引实体
     * @return 通知 DTO
     */
    private NotificationDTO convertToDTO(NotificationIndex index) {
        Notification notification = index.getNotification();
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getNotificationId());
        dto.setTitle(notification.getTitle());
        dto.setContent(notification.getContent());
        dto.setType(notification.getNotificationType().toString());
        dto.setSenderDepartment(notification.getSenderDepartment() != null
                ? notification.getSenderDepartment().getDepartmentName()
                : "无部门");
        dto.setSentTime(notification.getSentTime());
        dto.setIsRead(index.isReadStatus());
        return dto;
    }

    /**
     * 更新通知索引记录
     * @param indexId 通知索引的 ID
     * @param updatedIndex 更新后的通知索引对象
     * @return 更新后的通知索引对象
     */
    public NotificationIndex updateNotificationIndex(Integer indexId, NotificationIndex updatedIndex) {
        return notificationIndexRepository.findById(indexId)
                .map(index -> {
                    index.setNotification(updatedIndex.getNotification());
                    index.setRecipient(updatedIndex.getRecipient());
                    index.setReadStatus(updatedIndex.isReadStatus());
                    index.setReadTime(updatedIndex.getReadTime());
                    index.setNotificationType(updatedIndex.getNotificationType());
                    index.setRecipientType(updatedIndex.getRecipientType());
                    return notificationIndexRepository.save(index);
                })
                .orElseThrow(() -> new IllegalArgumentException("通知索引不存在，ID: " + indexId));
    }

    /**
     * 创建新的通知索引记录
     * @param newIndex 要创建的通知索引对象
     * @return 创建后的通知索引对象
     */
    public NotificationIndex createNotificationIndex(NotificationIndex newIndex) {
        // 直接调用 repository 保存新通知索引
        return notificationIndexRepository.save(newIndex);
    }

}