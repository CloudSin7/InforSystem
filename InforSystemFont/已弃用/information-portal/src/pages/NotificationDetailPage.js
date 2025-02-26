import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const NotificationDetailPage = () => {
  const { id } = useParams(); // 获取通知的ID
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    // 获取通知详情
    axios.get(`http://localhost:8088/api/notifications/${id}`)
      .then(response => {
        setNotification(response.data);
  
        // 标记通知为已读
        return axios.patch(`http://localhost:8088/api/notifications/${id}/mark-as-read`);
      })
      .then(() => {
        console.log("通知已标记为已读");
      })
      .catch(error => {
        console.error("获取通知详情或标记为已读失败:", error);
      });
  }, [id]);
  

  if (!notification) {
    return <div>加载中...</div>;
  }

  return (
    <div className="notification-detail">
      <h2>{notification.title}</h2>
      <div className="notification-meta">
        <span>发件单位: {notification.department}</span>
        <span>时间: {notification.date}</span>
      </div>
      <div className="notification-content">
        <p>{notification.content}</p>
      </div>
    </div>
  );

  
};

export default NotificationDetailPage;