import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/SearchResultPage.css';

const SearchResultPage = () => {
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 从 URL 的查询参数中获取搜索条件
        const urlParams = new URLSearchParams(window.location.search);
        const title = urlParams.get('title');
        const department = urlParams.get('department');
        const type = urlParams.get('type');
        const startTime = urlParams.get('startTime');
        const endTime = urlParams.get('endTime');
        const recipientId = urlParams.get('recipientId');

        if (!recipientId) {
          setError('当前用户未登录或用户 ID 缺失');
          setLoading(false);
          return;
        }

        // 构建请求参数对象
        const searchDTO = {
          title: title || '',
          department: department || '',
          notificationType: type || '',
          startDate: startTime ? new Date(startTime).toISOString() : null,
          endDate: endTime ? new Date(endTime).toISOString() : null,
          recipientId,
        };

        // 发送请求到后端获取搜索结果
        const response = await axios.post('http://localhost:8088/api/notifications/search', searchDTO);
        const groupedNotifications = groupNotificationsByType(response.data);
        setNotifications(groupedNotifications);
        setLoading(false);
      } catch (err) {
        console.error('Error while fetching notifications:', err);
        setError('搜索结果加载失败，请稍后再试');
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const groupNotificationsByType = (notifications) => {
    return notifications.reduce((acc, notification) => {
      const { type } = notification;
      if (!acc[type]) {
        acc[type] = [];
      }
      acc[type].push(notification);
      return acc;
    }, {});
  };

  const handleNotificationClick = (id) => {
    // 使用 navigate 跳转到通知详情页
    navigate(`/notifications/${id}`);
  };

  const renderNotificationSection = (title, notifications) => {
    const limitedNotifications = notifications.slice(0, 9); // 限制显示最多 9 条（3 行，每行 3 条）
    const rows = [];
    for (let i = 0; i < limitedNotifications.length; i += 3) {
      rows.push(limitedNotifications.slice(i, i + 3));
    }

    return (
      <div className="notification-section" key={title}>
        <div className="section-header">
          <h3>{mapNotificationTypeToName(title)}</h3>
        </div>
        {rows.map((row, rowIndex) => (
          <div key={rowIndex} className="notification-row">
            {row.map((notification) => (
              <div
                key={notification.id}
                className="notification-card"
                style={{ color: notification.isRead ? '#999999' : '#000000' }}
              >
                <h4>
                  <button
                    onClick={() => handleNotificationClick(notification.id)}
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
            ))}
          </div>
        ))}
      </div>
    );
  };

  const mapNotificationTypeToName = (type) => {
    const typeMapping = {
      TEACHING_NOTICE: '教学通知',
      STUDENT_NOTICE: '学工通知',
      OFFICE_NOTICE: '办公通知',
      RESEARCH_NOTICE: '科研通知',
      OTHER: '站内信（其他通知）',
    };
    return typeMapping[type] || type;
  };

  return (
    <div className="search-result-page">
      <h1>搜索结果</h1>
      {loading ? (
        <div>加载中...</div>
      ) : error ? (
        <div className="error-message">{error}</div>
      ) : (
        <div className="notifications-container">
          {Object.entries(notifications).map(([type, notifications]) =>
            renderNotificationSection(type, notifications)
          )}
        </div>
      )}
    </div>
  );
};

export default SearchResultPage;
