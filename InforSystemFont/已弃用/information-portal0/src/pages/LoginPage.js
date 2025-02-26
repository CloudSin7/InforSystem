// src/pages/LoginPage.js
import React, { useState } from 'react';
import axios from 'axios';
import qs from 'qs'; // 引入 qs 来处理 URL 编码

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // 使用 qs 库对请求参数进行 URL 编码
      const data = qs.stringify({
        email,
        password,
      });

      const response = await axios.post('http://localhost:8088/api/users/login', data, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });

      const user = response.data;
      console.log('登录成功:', user);
      localStorage.setItem('user', JSON.stringify(user));

      // 根据用户类型进行跳转
      if (user.userType === 'ADMIN') {
        window.location.href = '/dashboard';  // 管理员跳转到管理员页面
      } else if (user.userType === 'STUDENT') {
        window.location.href = '/home';  // 学生跳转到普通用户主页
      } else if (user.userType === 'TEACHER') {
        window.location.href = '/home';  // 老师跳转到普通用户主页
      } else {
        setError('无法识别的用户类型');
      }
    } catch (err) {
      setError('登录失败，请检查您的邮箱和密码');
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