package com.sin.inforsystembackend.controller;


import com.sin.inforsystembackend.dto.GroupedNotificationDTO;
import com.sin.inforsystembackend.dto.NotificationDTO;
import com.sin.inforsystembackend.entity.NotificationIndex;
import com.sin.inforsystembackend.service.NotificationIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification-indexes")
public class NotificationIndexController {

    @Autowired
    private NotificationIndexService notificationIndexService;

    /**
     * 根据接收者查询通知索引
     * @param recipientId 接收者 ID
     * @return 通知索引列表
     */
    @GetMapping("/by-recipient/{recipientId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationDTOsByRecipient(@PathVariable("recipientId") Integer recipientId) {
        List<NotificationDTO> notificationDTOs = notificationIndexService.getNotificationDTOsByRecipient(recipientId);
        return ResponseEntity.ok(notificationDTOs);
    }

    /**
     * 根据登录用户 ID 查询所有通知
     * @param recipientId 接收者 ID
     * @return 通知列表
     */
    @GetMapping("/user/{recipientId}/notifications")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByRecipient(@PathVariable("recipientId") Integer recipientId) {
        List<NotificationDTO> notificationDTOs = notificationIndexService.getNotificationDTOsByRecipient(recipientId);
        return ResponseEntity.ok(notificationDTOs);
    }

    /**
     * 获取用户接收到的所有通知，并按通知类型分类
     * @param recipientId 接收者 ID
     * @return 分类后的通知 DTO 列表
     */
    @GetMapping("/user/{recipientId}/grouped-by-type")
    public ResponseEntity<List<GroupedNotificationDTO>> getNotificationsGroupedByType(@PathVariable("recipientId") Integer recipientId) {
        List<GroupedNotificationDTO> groupedNotifications = notificationIndexService.getNotificationsGroupedByType(recipientId);
        if (groupedNotifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(groupedNotifications);
    }


    /**
     * 查询未读通知
     * @param recipientId 接收者 ID
     * @return 未读通知列表
     */
    @GetMapping("/unread/{recipientId}")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable("recipientId") Integer recipientId) {
        List<NotificationDTO> unreadNotificationDTOs = notificationIndexService.getUnreadNotificationDTOsByRecipient(recipientId);
        return ResponseEntity.ok(unreadNotificationDTOs);
    }

    /**
     * 标记通知为已读
     * @param notificationId 通知 ID
     * @param recipientId 接收者 ID
     * @return 无内容响应
     */
    @PatchMapping("/{notificationId}/mark-as-read/{recipientId}")
    public ResponseEntity<Void> markNotificationAsRead(
            @PathVariable("notificationId") Integer notificationId,
            @PathVariable("recipientId") Integer recipientId) {
        boolean updated = notificationIndexService.markNotificationAsRead(notificationId, recipientId);
        if (updated) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 分页查询通知索引
     * @param recipientId 接收者 ID
     * @param page 页码（可选，默认值 0）
     * @param size 每页大小（可选，默认值 10）
     * @return 分页通知索引列表
     */
    @GetMapping("/by-recipient/{recipientId}/paged")
    public ResponseEntity<Page<NotificationDTO>> getNotificationIndexesByRecipientWithPagination(
            @PathVariable("recipientId") Integer recipientId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<NotificationDTO> pagedIndexes = notificationIndexService.getNotificationIndexesByRecipientWithPagination(recipientId, PageRequest.of(page, size));
        return ResponseEntity.ok(pagedIndexes);
    }
}