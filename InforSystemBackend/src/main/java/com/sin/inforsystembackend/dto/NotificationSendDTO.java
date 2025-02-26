package com.sin.inforsystembackend.dto;

public class NotificationSendDTO {
    private String title; // 通知标题
    private String content; // 通知内容
    private String notificationType; // 通知类型，例如 'TEACHING_NOTICE'
    private Integer senderId; // 发件人ID
    private Integer senderDepartmentId; // 发件人部门ID
    private String recipientType; // 收件人类型，例如 'INDIVIDUAL', 'DEPARTMENT'
    private Long recipientIdentifier; // 收件人标识符，例如用户ID或部门ID

    // Getters and Setters

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

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
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

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public Long getRecipientIdentifier() {
        return recipientIdentifier;
    }

    public void setRecipientIdentifier(Long recipientIdentifier) {
        this.recipientIdentifier = recipientIdentifier;
    }
}