package com.sin.inforsystembackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_recipients")
public class NotificationRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipient_id")
    private Integer recipientId;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    private RecipientType recipientType;

    @Column(name = "recipient_identifier")
    private Long recipientIdentifier;

    public enum RecipientType {
        INDIVIDUAL, DEPARTMENT, ALL_STUDENTS, ALL_TEACHERS
    }

    // Getters and Setters
    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public RecipientType getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(RecipientType recipientType) {
        this.recipientType = recipientType;
    }

    public Long getRecipientIdentifier() {
        return recipientIdentifier;
    }

    public void setRecipientIdentifier(Long recipientIdentifier) {
        this.recipientIdentifier = recipientIdentifier;
    }
}