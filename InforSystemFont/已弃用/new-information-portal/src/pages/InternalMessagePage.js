import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/InternalMessagePage.css';

const InternalMessagePage = () => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [recipient, setRecipient] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      axios.get(`http://localhost:8088/api/messages/${user.userId}`)
        .then(response => {
          setMessages(response.data);
        })
        .catch(error => {
          console.error("无法加载站内信:", error);
        });
    }
  }, []);

  const handleSendMessage = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && newMessage && recipient) {
      axios.post('http://localhost:8088/api/messages', {
        senderId: user.userId,
        recipientId: recipient,
        content: newMessage
      })
      .then(() => {
        alert("站内信发送成功！");
        setNewMessage('');
        setRecipient('');
      })
      .catch(error => {
        console.error("发送站内信失败:", error);
      });
    }
  };

  return (
    <div className="internal-message-page">
      <h2>站内信</h2>
      <div className="message-list">
        {messages.map((message, index) => (
          <div key={index} className="message-card">
            <h4>发件人: {message.senderName}</h4>
            <p>{message.content}</p>
          </div>
        ))}
      </div>
      <div className="send-message">
        <h3>发送新站内信</h3>
        <input
          type="text"
          placeholder="接收者ID"
          value={recipient}
          onChange={(e) => setRecipient(e.target.value)}
        />
        <textarea
          placeholder="消息内容"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
        />
        <button onClick={handleSendMessage}>发送</button>
      </div>
    </div>
  );
};

export default InternalMessagePage;