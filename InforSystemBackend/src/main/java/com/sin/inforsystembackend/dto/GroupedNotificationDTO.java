package com.sin.inforsystembackend.dto;

import java.util.List;

public class GroupedNotificationDTO {
    private String notificationType; // 通知类型
    private List<NotificationDTO> notifications; // 属于该类型的通知列表

    // Getters and Setters
    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }
}