package com.sin.inforsystembackend.dto;

import java.time.LocalDateTime;

/**
 * NotificationSearchDTO 用于封装搜索通知时所需的各种查询条件。
 * 该类包含标题、部门、通知类型、时间范围等字段，
 * 用于根据前端传来的请求条件动态查询通知。
 */
public class NotificationSearchDTO {

    private String title;        // 通知标题
    private String department;   // 发件部门
    private String notificationType; // 通知类型
    private LocalDateTime startDate;  // 发件时间起点
    private LocalDateTime endDate;    // 发件时间终点
    private Integer recipientId;

    // Getters 和 Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }
}