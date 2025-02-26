package com.sin.inforsystembackend.repository;

import com.sin.inforsystembackend.entity.Notification;
import com.sin.inforsystembackend.entity.Notification.NotificationType;
import com.sin.inforsystembackend.entity.NotificationIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * NotificationRepository 继承 JpaRepository，提供基础的数据访问操作。
 * 这里定义了对通知数据的查询方法，包括按照通知类型、发件时间、标题等字段进行查询。
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    /**
     * 根据通知类型查询通知
     * @param notificationType 通知类型
     * @return 匹配的通知列表
     */
    List<Notification> findByNotificationType(NotificationType notificationType);

    /**
     * 根据标题模糊查询通知
     * @param title 部分或全部标题
     * @return 匹配的通知列表
     */
    List<Notification> findByTitleContaining(String title);

    /**
     * 根据发件部门查询通知
     * @param departmentName 部门名称
     * @return 匹配的通知列表
     */
    List<Notification> findBySenderDepartment_DepartmentName(String departmentName);

    /**
     * 根据发送时间范围筛选通知
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 匹配的通知列表
     */
    List<Notification> findBySentTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据通知状态查询通知
     * @param status 通知状态
     * @return 匹配的通知列表
     */
    List<Notification> findByStatus(Notification.Status status);


}