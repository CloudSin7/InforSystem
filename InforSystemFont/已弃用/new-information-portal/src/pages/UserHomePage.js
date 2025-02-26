import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import '../styles/UserHomePage.css';

const UserHomePage = () => {
  const [notifications, setNotifications] = useState({
    study: [],
    teaching: [],
    office: [],
    research: [],
  });
  const [error, setError] = useState('');

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user) {
      window.location.href = '/'; // 未登录用户重定向到登录页
      return;
    }
    axios.get('http://localhost:8088/api/notifications')
      .then(response => {
        const data = response.data;
        const categorizedNotifications = {
          study: data.filter(notif => notif.type === 'TEACHING_NOTICE'),
          teaching: data.filter(notif => notif.type === 'STUDENT_NOTICE'),
          office: data.filter(notif => notif.type === 'OFFICE_NOTICE'),
          research: data.filter(notif => notif.type === 'RESEARCH_NOTICE'),
        };
        setNotifications(categorizedNotifications);
      })
      .catch(error => {
        console.error('获取通知失败:', error);
        setError('无法加载通知，请稍后再试。');
      });
  }, []);

  return (
    <div className="user-home-page">
      <h2>欢迎回来！</h2>
      {error && <p className="error-message">{error}</p>}
      <div className="notifications-container">
        {Object.keys(notifications).map((category) => (
          <div key={category} className="notification-category">
            <h3>{category} 通知</h3>
            <ul>
              {notifications[category].map((notification, index) => (
                <li key={index}>
                  <Link to={`/notifications/${notification.id}`}>
                    <strong style={{ color: notification.isRead ? 'gray' : 'blue' }}>
                      {notification.title} - {notification.senderDepartment} ({notification.date})
                    </strong>
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>
    </div>
  );
};

export default UserHomePage;