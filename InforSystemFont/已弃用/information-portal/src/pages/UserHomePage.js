import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/UserHomePage.css'; // 样式文件

const UserHomePage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true); // 加载状态
  const [error, setError] = useState(''); // 错误状态

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      const recipientId = user.userId;

      // 获取接收的通知
      axios
        .get(`http://localhost:8088/api/notification-indexes/by-recipient/${recipientId}`)
        .then((response) => {
          setNotifications(response.data);
          setLoading(false); // 加载完成
        })
        .catch(() => {
          setError('无法加载通知，请稍后再试');
          setLoading(false); // 发生错误
        });
    }
  }, []);

  const handleNotificationClick = async (id) => {
    try {
      // 标记通知为已读
      await axios.patch(`http://localhost:8088/api/notifications/${id}/mark-as-read`);
      // 更新通知状态
      setNotifications((prevNotifications) =>
        prevNotifications.map((notification) =>
          notification.id === id ? { ...notification, isRead: true } : notification
        )
      );
      // 跳转到通知详情页
      window.location.href = `/notifications/${id}`;
    } catch (error) {
      console.error('标记通知为已读失败:', error);
    }
  };

  if (loading) {
    return <div>加载中...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="user-home-page">
      <header className="page-header">
        <h2>欢迎来到信息门户主页</h2>
      </header>
      <div className="notifications-container">
        <NotificationSection
          title="办公通知"
          notifications={notifications.filter((n) => n.notificationType === 'OFFICE_NOTICE')}
          onNotificationClick={handleNotificationClick}
        />
        <NotificationSection
          title="教学通知"
          notifications={notifications.filter((n) => n.notificationType === 'TEACHING_NOTICE')}
          onNotificationClick={handleNotificationClick}
        />
        <NotificationSection
          title="学工通知"
          notifications={notifications.filter((n) => n.notificationType === 'STUDENT_NOTICE')}
          onNotificationClick={handleNotificationClick}
        />
        <NotificationSection
          title="科研通知"
          notifications={notifications.filter((n) => n.notificationType === 'RESEARCH_NOTICE')}
          onNotificationClick={handleNotificationClick}
        />
        <NotificationSection
          title="其他通知"
          notifications={notifications.filter((n) => n.notificationType === 'OTHER')}
          onNotificationClick={handleNotificationClick}
        />
      </div>
    </div>
  );
};

const NotificationSection = ({ title, notifications, onNotificationClick }) => (
  <div className="notification-section">
    <h3>{title}</h3>
    <div className="notification-list">
      {notifications.length > 0 ? (
        notifications.map((notification) => (
          <div
            key={notification.id}
            className="notification-card"
            style={{ color: notification.isRead ? '#999999' : '#000000' }} // 已读为灰色，未读为黑色
          >
            <h4>
              <button
                onClick={() => onNotificationClick(notification.id)}
                className="link-button"
              >
                {notification.title}
              </button>
            </h4>
            <div className="notification-info">
              <span>发件单位: {notification.senderDepartment}</span>
              <span>时间: {new Date(notification.sentTime).toLocaleString()}</span>
            </div>
          </div>
        ))
      ) : (
        <p>暂无通知</p>
      )}
    </div>
  </div>
);

export default UserHomePage;
