package com.sin.inforsystembackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_index")
public class NotificationIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "index_id")
    private Integer indexId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    @NotNull(message = "Notification cannot be null")
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    @NotNull(message = "Recipient cannot be null")
    private User recipient;

    @Column(name = "is_read", nullable = false)
    private boolean readStatus = false;

    @Column(name = "read_time")
    private LocalDateTime readTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    @NotNull(message = "Notification type cannot be null")
    private Notification.NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    @NotNull(message = "Recipient type cannot be null")
    private RecipientType recipientType;

    public enum RecipientType {
        INDIVIDUAL, DEPARTMENT, ALL_STUDENTS, ALL_TEACHERS, ALL
    }

    // Getters and Setters
    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    public Notification.NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Notification.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public RecipientType getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(RecipientType recipientType) {
        this.recipientType = recipientType;
    }
}