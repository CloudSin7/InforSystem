package com.sin.inforsystembackend.repository;

import com.sin.inforsystembackend.dto.NotificationSendDTO;
import com.sin.inforsystembackend.entity.NotificationRecipient;
import com.sin.inforsystembackend.entity.NotificationRecipient.RecipientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Integer> {

    /**
     * 根据通知 ID 查询接收者
     * @param notificationId 通知 ID
     * @return 匹配的接收者列表
     */
    List<NotificationRecipient> findByNotification_NotificationId(Integer notificationId);

    /**
     * 根据接收者类型筛选接收者
     * @param recipientType 接收者类型
     * @return 匹配的接收者列表
     */
    List<NotificationRecipient> findByRecipientType(RecipientType recipientType);

    /**
     * 根据接收者标识查询接收者
     * @param recipientIdentifier 接收者标识
     * @return 匹配的接收者列表
     */
    List<NotificationRecipient> findByRecipientIdentifier(Long recipientIdentifier);

    /**
     * 根据接收者类型和标识查询接收者
     * @param recipientType 接收者类型
     * @param recipientIdentifier 接收者标识
     * @return 匹配的接收者列表
     */
    List<NotificationRecipient> findByRecipientTypeAndRecipientIdentifier(RecipientType recipientType, Long recipientIdentifier);



}