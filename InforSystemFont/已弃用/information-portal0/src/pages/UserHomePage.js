import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/UserHomePage.css'; // 修改导入路径

const UserHomePage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true); // 加载指示器状态
  const [error, setError] = useState(''); // 错误状态

  useEffect(() => {
    // 从 localStorage 获取用户信息
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      const recipientId = user.userId;

      // 获取用户的所有通知
      axios.get(`http://localhost:8088/api/notifications/by-recipient/${recipientId}`)
        .then(response => {
          setNotifications(response.data);
          setLoading(false); // 加载完成，关闭加载指示器
        })
        .catch(error => {
          setError('无法加载通知，请稍后再试');
          setLoading(false); // 发生错误，关闭加载指示器
        });
    }
  }, []);

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
        <div className="message-button-container">
          <button className="message-button" onClick={() => window.location.href = '/messages'}>
            站内信
          </button>
        </div>
      </header>
      <div className="notifications-container">
        <NotificationSection title="办公通知" notifications={notifications.filter(n => n.notificationType === 'OFFICE_NOTICE')} />
        <NotificationSection title="教学通知" notifications={notifications.filter(n => n.notificationType === 'TEACHING_NOTICE')} />
        <NotificationSection title="学工通知" notifications={notifications.filter(n => n.notificationType === 'STUDENT_NOTICE')} />
        <NotificationSection title="其他通知" notifications={notifications.filter(n => n.notificationType === 'OTHER')} />
      </div>
    </div>
  );
};

const NotificationSection = ({ title, notifications }) => (
  <div className="notification-section">
    <h3>{title}</h3>
    <div className="notification-list">
      {notifications.map((notification) => (
        <div key={notification.id} className="notification-card">
          <h4><a href={`/notifications/${notification.id}`}>{notification.title}</a></h4>
          <div className="notification-info">
            <span>发件单位: {notification.department}</span>
            <span>时间: {notification.date}</span>
          </div>
        </div>
      ))}
    </div>
  </div>
);

export default UserHomePage;