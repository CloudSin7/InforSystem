import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/AdminControlPage.css';

const AdminControlPage = () => {
  const [users, setUsers] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchUsers(currentPage);
  }, [currentPage]);

  const fetchUsers = async (page = 0, size = 10) => {
    try {
      const response = await axios.get(`/api/users?page=${page}&size=${size}`);
      if (response.ok) {
        const data = await response.json();
        setUsers(data.content);
        setTotalPages(data.totalPages);
      } else {
        console.error('获取用户列表失败');
        alert('获取用户列表失败，请稍后重试。');
      }
    } catch (error) {
      console.error('请求用户列表时发生错误', error);
      alert('服务器请求失败，请检查网络或稍后重试。');
    }
  };

  const addUser = async (user) => {
    try {
      const response = await axios.post('/api/users', user);
      if (response.ok) {
        alert('成功添加用户');
        fetchUsers(currentPage); // 刷新用户列表
      } else {
        alert('添加用户失败，请检查输入是否正确。');
      }
    } catch (error) {
      alert('服务器请求失败，请检查网络或稍后重试。');
    }
  };

  const deleteUser = async (userId) => {
    try {
      const response = await axios.delete(`/api/users/${userId}`);
      if (response.ok) {
        alert('成功删除用户');
        fetchUsers(currentPage); // 刷新用户列表
      } else {
        alert('删除用户失败，请稍后重试。');
      }
    } catch (error) {
      alert('服务器请求失败，请检查网络或稍后重试。');
    }
  };

  return (
    <div>
      <h1>管理员用户管理</h1>
      <ul>
        {users.map((user) => (
          <li key={user.userId}>
            {user.name} - {user.email}
            <button onClick={() => deleteUser(user.userId)}>删除</button>
          </li>
        ))}
      </ul>
      <div>
        <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 0}>
          上一页
        </button>
        <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages - 1}>
          下一页
        </button>
      </div>
    </div>
  );
};

export default AdminControlPage;