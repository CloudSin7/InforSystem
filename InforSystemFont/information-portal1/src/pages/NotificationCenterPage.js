import React, { useState, useEffect, useCallback } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import '../styles/NotificationCenterPage.css';

const NotificationCenterPage = () => {
  const [notifications, setNotifications] = useState([]);
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const userId = queryParams.get('userId');
  const userName = queryParams.get('userName');

  const [currentUser, setCurrentUser] = useState({ id: userId, name: userName });
  const [allDepartments, setAllDepartments] = useState([]);
  const [userDepartments, setUserDepartments] = useState([]);
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const [isSending, setIsSending] = useState(false);

  const [selectedOption, setSelectedOption] = useState('sendNotification');
  const [notificationData, setNotificationData] = useState({
    title: '',
    content: '',
    notificationType: 'TEACHING_NOTICE',
    recipientType: 'INDIVIDUAL',
    recipientIdentifier: '',
    senderId: userId,
    senderDepartmentId: null
  });

  useEffect(() => {
    if (userId && userName) {
      setCurrentUser({ id: userId, name: userName });
      setNotificationData((prevData) => ({
        ...prevData,
        senderId: userId
      }));
    } else {
      setError('用户信息缺失，请重新登录。');
    }
  }, [userId, userName]);

  useEffect(() => {
    axios.get('http://localhost:8088/api/departments/all')
      .then((response) => {
        setAllDepartments(response.data);
      })
      .catch(() => {
        setError('无法获取所有部门信息，请稍后再试。');
      });
  }, []);

  useEffect(() => {
    if (currentUser && currentUser.id) {
      axios.get(`http://localhost:8088/api/user/${currentUser.id}/departments-dto`)
        .then((response) => {
          setUserDepartments(response.data);
        })
        .catch(() => {
          setError('无法获取用户所属部门信息，请稍后再试。');
        });
    }
  }, [currentUser]);

  useEffect(() => {
    if (notificationData.recipientType === 'INDIVIDUAL') {
      axios.get('http://localhost:8088/api/users/search')
        .then((response) => {
          setUsers(response.data);
        })
        .catch(() => {
          setError('无法获取用户信息，请稍后再试。');
        });
    } else {
      setUsers([]); // 如果不是个人收件人，则清空用户列表
    }
  }, [notificationData.recipientType]);

  const handleOptionChange = (option) => {
    setSelectedOption(option);
    setError(null);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNotificationData((prevData) => ({
      ...prevData,
      [name]: name === 'recipientIdentifier' || name === 'senderDepartmentId' ? (value ? parseInt(value, 10) : '') : value
    }));
  };

  const handleSendNotification = useCallback(() => {
    if (isSending) return;

    setIsSending(true);
    setError(null);

    if (!notificationData.title || !notificationData.content) {
      setError('标题和内容为必填项。');
      setIsSending(false);
      return;
    }

    if ((notificationData.recipientType === 'INDIVIDUAL' || notificationData.recipientType === 'DEPARTMENT') &&
      !notificationData.recipientIdentifier) {
      setError('请选择一个收件人。');
      setIsSending(false);
      return;
    }

    axios.post('http://localhost:8088/api/notifications/send', notificationData)
      .then(() => {
        alert('通知发送成功！');
        setNotificationData({
          title: '',
          content: '',
          notificationType: 'TEACHING_NOTICE',
          recipientType: 'INDIVIDUAL',
          recipientIdentifier: '',
          senderId: currentUser.id,
          senderDepartmentId: null
        });
      })
      .catch(() => {
        setError('发送通知失败，请稍后再试。');
      })
      .finally(() => {
        setIsSending(false);
      });
  }, [notificationData, currentUser, isSending]);

  return (
    <div className="notification-center-page">
      <div className="sidebar">
        <ul>
          <li onClick={() => handleOptionChange('sendNotification')}>发送信件</li>
          <li onClick={() => handleOptionChange('drafts')}>草稿箱</li>
          <li onClick={() => handleOptionChange('sentNotifications')}>发件箱</li>
        </ul>
      </div>
      <div className="content">
        {selectedOption === 'sendNotification' && (
          <div className="send-notification-form">
            <h3>发送信件</h3>
            {error && <div className="error-message">{error}</div>}
            <input
              type="text"
              name="title"
              value={notificationData.title}
              onChange={handleInputChange}
              placeholder="请输入信件标题"
            />
            <textarea
              name="content"
              value={notificationData.content}
              onChange={handleInputChange}
              placeholder="请输入信件内容"
            />
            <select
              name="notificationType"
              value={notificationData.notificationType}
              onChange={handleInputChange}
              onBlur={handleInputChange}
            >
              <option value="TEACHING_NOTICE">教学通知</option>
              <option value="STUDENT_NOTICE">学工通知</option>
              <option value="OFFICE_NOTICE">办公通知</option>
              <option value="RESEARCH_NOTICE">科研通知</option>
              <option value="OTHER">其他通知</option>
            </select>
            <select
              name="recipientType"
              value={notificationData.recipientType}
              onChange={(e) => {
                handleInputChange(e);
                setNotificationData((prevData) => ({ ...prevData, recipientIdentifier: '' })); // 切换收件人类型时清空当前选择
              }}
              onBlur={handleInputChange}
            >
              <option value="INDIVIDUAL">个人</option>
              <option value="DEPARTMENT">部门</option>
              <option value="ALL_STUDENTS">全体学生</option>
              <option value="ALL_TEACHERS">全体教师</option>
            </select>
            {notificationData.recipientType === 'INDIVIDUAL' && (
              <select
                name="recipientIdentifier"
                value={notificationData.recipientIdentifier}
                onChange={handleInputChange}
                onBlur={handleInputChange}
              >
                <option value="">请选择个人收件人</option>
                {users.length === 0 ? (
                  <option value="">暂无用户信息</option>
                ) : (
                  users.map((user) => (
                    <option key={user.id} value={user.id}>
                      {user.name}
                    </option>
                  ))
                )}
              </select>
            )}
            {notificationData.recipientType === 'DEPARTMENT' && (
              <select
                name="recipientIdentifier"
                value={notificationData.recipientIdentifier}
                onChange={handleInputChange}
                onBlur={handleInputChange}
              >
                <option value="">请选择部门收件人</option>
                {allDepartments.length === 0 ? (
                  <option value="">暂无部门信息</option>
                ) : (
                  allDepartments.map((department) => (
                    <option key={department.departmentId} value={department.departmentId}>
                      {department.departmentName}
                    </option>
                  ))
                )}
              </select>
            )}
            {currentUser && (
              <div className="sender-info">
                <p>发件人: {currentUser.id} - {currentUser.name}</p>
              </div>
            )}
            <select
              name="senderDepartmentId"
              value={notificationData.senderDepartmentId || ''}
              onChange={handleInputChange}
              onBlur={handleInputChange}
            >
              <option value="">请选择发件人部门</option>
              {userDepartments.map((department) => (
                <option key={department.departmentId} value={department.departmentId}>
                  {department.departmentName}
                </option>
              ))}
            </select>
            <button
              onClick={handleSendNotification}
              disabled={isSending}
            >
              {isSending ? '正在发送...' : '发送'}
            </button>
          </div>
        )}
        {selectedOption === 'drafts' && (
          <div>
            <h3>草稿箱</h3>
          </div>
        )}
        {selectedOption === 'sentNotifications' && (
          <div>
            <h3>发件箱</h3>
            {Array.isArray(notifications) && notifications.length === 0 ? (
              <p>暂无已发送的通知。</p>
            ) : (
              <ul>
                {notifications.map((notification) => (
                  <li key={notification.id}>
                    <h4>{notification.title}</h4>
                    <p>{notification.content}</p>
                    <p>发送时间: {new Date(notification.sentTime).toLocaleString()}</p>
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default NotificationCenterPage;
