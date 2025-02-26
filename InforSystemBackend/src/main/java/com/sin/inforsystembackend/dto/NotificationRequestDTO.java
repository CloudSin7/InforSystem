package com.sin.inforsystembackend.dto;

import java.util.List;

public class NotificationRequestDTO {

    private String title;
    private String content;
    private String type; // 通知类型
    private Integer senderId; // 发送者 ID
    private Integer senderDepartmentId; // 发送者部门 ID
    private String recipientType; // 接收者类型
    private List<Long> recipientIdentifiers; // 接收者标识列表

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

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public List<Long> getRecipientIdentifiers() {
        return recipientIdentifiers;
    }

    public void setRecipientIdentifiers(List<Long> recipientIdentifiers) {
        this.recipientIdentifiers = recipientIdentifiers;
    }
}