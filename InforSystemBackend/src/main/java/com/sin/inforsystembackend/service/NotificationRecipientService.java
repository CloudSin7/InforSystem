package com.sin.inforsystembackend.service;

import com.sin.inforsystembackend.dto.NotificationSendDTO;
import com.sin.inforsystembackend.entity.Notification;
import com.sin.inforsystembackend.entity.NotificationRecipient;
import com.sin.inforsystembackend.repository.NotificationRecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationRecipientService {

    @Autowired
    private NotificationRecipientRepository notificationRecipientRepository;

    /**
     * 根据通知 ID 查询接收者
     * @param notificationId 通知 ID
     * @return 匹配的接收者列表
     */
    public List<NotificationRecipient> findRecipientsByNotificationId(Integer notificationId) {
        if (notificationId == null) {
            return List.of();
        }
        return notificationRecipientRepository.findByNotification_NotificationId(notificationId);
    }

    /**
     * 根据接收者类型查询接收者
     * @param recipientType 接收者类型
     * @return 匹配的接收者列表
     */
    public List<NotificationRecipient> findRecipientsByType(NotificationRecipient.RecipientType recipientType) {
        if (recipientType == null) {
            return List.of();
        }
        return notificationRecipientRepository.findByRecipientType(recipientType);
    }

    /**
     * 根据通知 ID 和接收者类型查询接收者
     * @param notificationId 通知 ID
     * @param recipientType 接收者类型
     * @return 匹配的接收者列表
     */
    public List<NotificationRecipient> findRecipientsByNotificationIdAndType(Integer notificationId, NotificationRecipient.RecipientType recipientType) {
        if (notificationId == null || recipientType == null) {
            return List.of();
        }
        // 使用现有方法拆分查询
        List<NotificationRecipient> recipients = notificationRecipientRepository.findByNotification_NotificationId(notificationId);
        return recipients.stream()
                .filter(recipient -> recipientType.equals(recipient.getRecipientType()))
                .toList();
    }

    /**
     * 根据接收者标识查询接收的通知
     * @param recipientIdentifier 接收者标识
     * @return 匹配的通知接收者列表
     */
    public List<NotificationRecipient> findRecipientsByIdentifier(Long recipientIdentifier) {
        if (recipientIdentifier == null) {
            return List.of();
        }
        return notificationRecipientRepository.findByRecipientIdentifier(recipientIdentifier);
    }

    /**
     * 更新接收者记录
     * @param recipientId 接收者的 ID
     * @param updatedRecipient 更新后的接收者对象
     * @return 更新后的接收者对象
     */
    public NotificationRecipient updateRecipient(Integer recipientId, NotificationRecipient updatedRecipient) {
        return notificationRecipientRepository.findById(recipientId)
                .map(recipient -> {
                    recipient.setRecipientType(updatedRecipient.getRecipientType());
                    recipient.setRecipientIdentifier(updatedRecipient.getRecipientIdentifier());
                    recipient.setNotification(updatedRecipient.getNotification());
                    return notificationRecipientRepository.save(recipient);
                })
                .orElseThrow(() -> new IllegalArgumentException("接收者不存在，ID: " + recipientId));
    }

    /**
     * 创建新的接收者记录
     * @param newRecipient 要创建的接收者对象
     * @return 创建后的接收者对象
     */
    public NotificationRecipient createRecipient(NotificationRecipient newRecipient) {
        // 直接调用 repository 保存新接收者
        return notificationRecipientRepository.save(newRecipient);
    }

}