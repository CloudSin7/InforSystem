import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/UserHomePage.css';

const UserHomePage = () => {
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedDepartment, setSelectedDepartment] = useState('');
  const [selectedNotificationType, setSelectedNotificationType] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      // 确保用户信息被存储到 localStorage
      localStorage.setItem('user', JSON.stringify(user));
      const recipientId = user.userId;
      axios
        .get(`http://localhost:8088/api/notification-indexes/user/${recipientId}/grouped-by-type`)
        .then((response) => {
          const updatedNotifications = response.data.map(group => ({
            ...group,
            notifications: group.notifications.map(notification => ({
              ...notification,
              senderDepartment: notification.senderDepartment || '未知部门'
            }))
          }));
          setNotifications(updatedNotifications);
          setLoading(false);
        })
        .catch(() => {
          setError('无法加载通知，请稍后再试');
          setLoading(false);
        });
    } else {
      setError('用户未登录');
      setLoading(false);
    }
  }, []);

  const handleNotificationClick = (id) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      navigate(`/notifications/${id}`, { state: { user } });
    } else {
      setError('用户未登录');
    }
  };

  const handleSearch = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (!user) {
        setError('用户未登录');
        return;
      }

      navigate({
        pathname: '/search-results',
        search: `?title=${searchQuery}&department=${selectedDepartment}&type=${selectedNotificationType}&startTime=${startTime}&endTime=${endTime}&recipientId=${user.userId}`,
      });
    } catch (error) {
      setError('搜索失败，请稍后再试');
      setLoading(false);
    }
  };

  const handleMoreClick = (notificationType) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user) {
      setError('用户未登录');
      return;
    }

    navigate({
      pathname: '/search-results',
      search: `?type=${notificationType}&recipientId=${user.userId}`
    });
  };

  const handleNavigateToNotificationCenter = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        navigate(`/notification-center?userId=${user.userId}&userName=${user.name}`);
    } else {
        setError('用户未登录');
    }
};


  const departmentOptions = [
    '中华民族共同体学院', '海南国际学院', '筑牢中华民族共同体意识研究院', '民族与社会学学院',
    '中国语言文学学部', '中国民族语文应用研究院', '文学院', '历史文化学院',
    '哲学与宗教学院', '新闻与传播学院', '外国语学院', '马克思主义学院',
    '经济学院', '管理学院', '法学院', '生命与环境科学学院',
    '药学院', '理学院', '信息工程学院', '音乐学院',
    '舞蹈学院', '美术学院', '体育学院', '教育学院',
    '国际教育学院', '预科教育学院', '继续教育学院（干部培训部）', '藏学研究院',
  ];

  const notificationTypeOptions = [
    { value: 'TEACHING_NOTICE', label: '教学通知' },
    { value: 'STUDENT_NOTICE', label: '学工通知' },
    { value: 'OFFICE_NOTICE', label: '办公通知' },
    { value: 'RESEARCH_NOTICE', label: '科研通知' },
    { value: 'OTHER', label: '其它通知' }
  ];

  return (
    <div className="user-home-page">
      <header className="page-header">
        <h2>欢迎来到信息门户主页</h2>
        <div className="header-buttons">
          <button onClick={handleNavigateToNotificationCenter} className="notification-center-button">进入通知中心</button>
        </div>
        <div className="search-bar">
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="输入标题搜索"
          />
          <button onClick={handleSearch}>搜索</button>
          <div className="search-filters">
            <select value={selectedDepartment} onChange={(e) => setSelectedDepartment(e.target.value)}>
              <option value="">选择部门</option>
              {departmentOptions.map((department, index) => (
                <option key={index} value={department}>{department}</option>
              ))}
            </select>
            <select value={selectedNotificationType} onChange={(e) => setSelectedNotificationType(e.target.value)}>
              <option value="">选择通知类型</option>
              {notificationTypeOptions.map((type, index) => (
                <option key={index} value={type.value}>{type.label}</option>
              ))}
            </select>
            <input
              type="datetime-local"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
              placeholder="开始时间"
            />
            <input
              type="datetime-local"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
              placeholder="结束时间"
            />
          </div>
        </div>
      </header>
      {loading ? (
        <div>加载中...</div>
      ) : error ? (
        <div className="error-message">{error}</div>
      ) : (
        <div className="notifications-container">
          {notifications.map((group) => (
            <NotificationSection
              key={group.notificationType}
              title={mapNotificationTypeToName(group.notificationType)}
              notifications={group.notifications}
              onNotificationClick={handleNotificationClick}
              notificationType={group.notificationType}
              onMoreClick={handleMoreClick}
            />
          ))}
        </div>
      )}
    </div>
  );
};

const NotificationSection = ({ title, notifications, onNotificationClick, notificationType, onMoreClick }) => {
  const limitedNotifications = notifications.slice(0, 9); // 限制显示最多 9 条（3 行，每行 3 条）
  const rows = [];
  for (let i = 0; i < limitedNotifications.length; i += 3) {
    rows.push(limitedNotifications.slice(i, i + 3));
  }

  return (
    <div className="notification-section">
      <div className="section-header">
        <h3>{title}</h3>
        <button className="more-button" onClick={() => onMoreClick(notificationType)}>更多</button>
      </div>
      {rows.map((row, rowIndex) => (
        <div key={`row-${rowIndex}`} className="notification-row">
          {row.map((notification) => (
            <div
              key={notification.id}
              className="notification-card"
              style={{ color: notification.isRead ? "#999999" : "#000000" }}
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
          ))}
        </div>
      ))}
    </div>
  );
};

const mapNotificationTypeToName = (type) => {
  const typeMapping = {
    TEACHING_NOTICE: "教学通知",
    STUDENT_NOTICE: "学工通知",
    OFFICE_NOTICE: "办公通知",
    RESEARCH_NOTICE: "科研通知",
    OTHER: "站内信（其他通知）",
  };
  return typeMapping[type] || type;
};

export default UserHomePage;
