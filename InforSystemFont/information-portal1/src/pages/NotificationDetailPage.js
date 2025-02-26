import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import '../styles/NotificationDetailPage.css';

const NotificationDetailPage = () => {
  const location = useLocation();
  const [notification, setNotification] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchNotification = async () => {
      const notificationId = location.pathname.split('/').pop();
      try {
        const response = await axios.get(`http://localhost:8088/api/notifications/${notificationId}`);
        setNotification(response.data);
        setLoading(false);
      } catch (err) {
        setError('无法加载通知详情，请稍后再试');
        setLoading(false);
      }
    };

    fetchNotification();
  }, [location]);

  if (loading) {
    return <div className="loading">加载中...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="notification-detail-page">
      <div className="notification-card">
        <div className="notification-header">
          <h1 className="notification-title">{notification.title}</h1>
          <p className="notification-meta">
            <span className="notification-department">发布部门：{notification.senderDepartment}</span>
            <span className="notification-time">发布时间：{new Date(notification.sentTime).toLocaleString()}</span>
          </p>
        </div>
        <div className="notification-content">
          <p>{notification.content}</p>
        </div>
      </div>
    </div>
  );
};

export default NotificationDetailPage;