// src/pages/LoginPage.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/LoginPage.css';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!email || !password) {
      setError('邮箱和密码不能为空');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8088/api/users/login', { email, password }, { headers: { 'Content-Type': 'application/json' } });
      const user = response.data;
      localStorage.setItem('user', JSON.stringify(user));
      navigate('/home');
    } catch (err) {
      if (err.response && err.response.status === 401) {
        setError('邮箱或密码错误，请重试');
      } else if (err.response && err.response.status === 403) {
        setError('您没有权限访问此资源');
      } else {
        setError('登录失败，请稍后再试');
      }
    }
  };

  return (
    <div className="login-container">
      <h2>登录</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label htmlFor="email">邮箱:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">密码:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        {error && <p className="error">{error}</p>}
        <button type="submit">登录</button>
      </form>
    </div>
  );
};

export default LoginPage;