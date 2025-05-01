import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/LoginPage.css'; // 导入样式文件

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // 为 body 添加专属样式类
    document.body.classList.add('login-body');
    return () => {
      // 清理时移除样式类，避免影响其他页面
      document.body.classList.remove('login-body');
    };
  }, []);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        'http://localhost:8088/api/users/login',
        null,
        {
          params: { email, password },
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        }
      );
      const user = response.data;

      if (!user || !user.userType) {
        throw new Error('用户信息不完整');
      }

      localStorage.setItem('user', JSON.stringify(user));
      if (user.userType === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate('/home');
      }
    } catch (err) {
      setError(err.response?.data?.message || '登录失败，请检查您的邮箱和密码！');
    }
  };

  return (
    <div className="login-page">
      <h1>登录</h1>
      <form onSubmit={handleLogin}>
        <label htmlFor="email">邮箱</label>
        <input
          type="email"
          id="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <label htmlFor="password">密码</label>
        <input
          type="password"
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        {error && <p>{error}</p>}
        <button type="submit">登录</button>
      </form>
    </div>
  );
};

export default LoginPage;
