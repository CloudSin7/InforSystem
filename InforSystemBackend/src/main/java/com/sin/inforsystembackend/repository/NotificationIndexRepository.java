package com.sin.inforsystembackend.repository;

import com.sin.inforsystembackend.entity.NotificationIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationIndexRepository extends JpaRepository<NotificationIndex, Integer> {

    /**
     * 根据通知 ID 和接收者 ID 查询通知索引
     * @param notificationId 通知 ID
     * @param recipientId 接收者 ID
     * @return 匹配的通知索引
     */
    Optional<NotificationIndex> findByNotification_NotificationIdAndRecipient_UserId(Integer notificationId, Integer recipientId);
    /**
     * 根据接收者 ID 查询通知索引列表
     * @param recipientId 接收者 ID
     * @return 通知索引列表
     */
    List<NotificationIndex> findByRecipient_UserId(Integer recipientId);

    /**
     * 根据接收者 ID 查询通知索引（分页）
     * @param recipientId 接收者 ID
     * @param pageable 分页信息
     * @return 分页的通知索引
     */
    Page<NotificationIndex> findByRecipient_UserId(Integer recipientId, Pageable pageable);

    /**
     * 查询指定接收者的未读通知索引列表
     * @param recipientId 接收者 ID
     * @return 未读通知索引列表
     */
    List<NotificationIndex> findByReadStatusFalseAndRecipient_UserId(Integer recipientId);

    /**
     * 查询所有未读通知索引（分页）
     * @param recipientId 接收者 ID
     * @param pageable 分页信息
     * @return 分页的未读通知索引
     */
    Page<NotificationIndex> findByReadStatusFalseAndRecipient_UserId(Integer recipientId, Pageable pageable);

}