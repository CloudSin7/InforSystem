package com.sin.inforsystembackend.dto;

import com.sin.inforsystembackend.entity.Notification;

import java.time.LocalDateTime;

public class NotificationUpdateDTO {
    private String title;  // 通知标题
    private String content;  // 通知内容
    private String type;  // 通知类型
    private Integer senderId;  // 发件人ID
    private Integer senderDepartmentId;  // 发件人部门ID
    private Notification.Status status;  // 通知状态，保持为枚举类型
    private LocalDateTime sentTime;  // 发送时间，保持为 LocalDateTime 类型

    // Getters 和 Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getSenderDepartmentId() {
        return senderDepartmentId;
    }

    public void setSenderDepartmentId(Integer senderDepartmentId) {
        this.senderDepartmentId = senderDepartmentId;
    }

    public Notification.Status getStatus() {
        return status;
    }

    public void setStatus(Notification.Status status) {
        this.status = status;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }
}