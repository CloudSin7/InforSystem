import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/AdminControlPage.css'; // 导入样式文件

const AdminControlPage = () => {
  const [departments, setDepartments] = useState([]);
  const [selectedDepartmentId, setSelectedDepartmentId] = useState(null);
  const [users, setUsers] = useState([]);
  const [newUser, setNewUser] = useState({ name: '', email: '', role: '' });

  // 获取所有部门信息
  useEffect(() => {
    axios.get('http://localhost:8088/api/departments/all')
      .then(response => {
        console.log("部门数据:", response.data);
        setDepartments(response.data);
      })
      .catch(error => {
        console.error("无法获取部门信息:", error);
      });
  }, []);

  // 获取选定部门的用户信息
  useEffect(() => {
    if (selectedDepartmentId) {
      axios.get(`http://localhost:8088/api/departments/${selectedDepartmentId}/users`)
        .then(response => {
          console.log("用户数据:", response.data); // 确保返回的数据包含 userId 字段
          setUsers(response.data); // 假设后端返回的数据是正确的，包含 userId
        })
        .catch(error => {
          console.error("无法获取用户信息:", error);
        });
    }
  }, [selectedDepartmentId]);

  // 处理部门点击事件
  const handleDepartmentClick = (departmentId) => {
    setSelectedDepartmentId(departmentId);
  };

  // 处理修改用户信息
  const handleEditUser = (userId) => {
    const updatedUser = prompt('请输入新用户信息（格式：姓名,邮箱,角色）：');
    if (updatedUser) {
      const [name, email, role] = updatedUser.split(',');
      const updatedUserData = { name, email, userType: role };

      // 修改用户 API 请求
      axios.put(`http://localhost:8088/api/admin/user/${userId}`, updatedUserData)
        .then(response => {
          setUsers(users.map(user => user.userId === userId ? response.data : user));
        })
        .catch(error => {
          console.error("无法更新用户信息:", error);
        });
    }
  };

  // 处理删除用户
  const handleDeleteUser = (userId) => {
    console.log("Attempting to delete user with ID:", userId); // 调试日志

    if (userId === null || userId === undefined) {
      console.error("User ID is null or undefined!");
      return; // 如果 userId 为 null 或 undefined，直接返回
    }

    // 删除用户 API 请求
    axios.delete(`http://localhost:8088/api/admin/user/${userId}`)
      .then(() => {
        setUsers(users.filter(user => user.userId !== userId)); // 更新用户列表
      })
      .catch(error => {
        console.error("无法删除用户:", error);
      });
  };

  // 处理添加新用户
  const handleAddUser = () => {
    if (!newUser.name || !newUser.email || !newUser.role) {
      alert('请填写完整的新用户信息');
      return;
    }
    const newUserWithDefaultPassword = { ...newUser, password: '000000' };

    // 添加新用户 API 请求
    axios.post('http://localhost:8088/api/admin/user', newUserWithDefaultPassword)
      .then(response => {
        setUsers([...users, response.data]);
        setNewUser({ name: '', email: '', role: '' }); // 清空表单
      })
      .catch(error => {
        console.error("无法添加用户:", error);
      });
  };

  return (
    <div className="admin-control-page">
      <div className="sidebar">
        <h3>部门列表</h3>
        <ul>
          {departments.map((department) => (
            <li key={department.departmentId} onClick={() => handleDepartmentClick(department.departmentId)}>
              {department.departmentName}
            </li>
          ))}
        </ul>
      </div>
      <div className="content">
        <h3>部门人员信息</h3>
        {selectedDepartmentId ? (
          <div>
            <table className="user-table">
              <thead>
                <tr>
                  <th>姓名</th>
                  <th>邮箱</th>
                  <th>角色</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.userId}> {/* 确保使用 userId 作为 key */}
                    <td>{user.name}</td>
                    <td>{user.email}</td>
                    <td>{user.userType}</td>
                    <td>
                      <button onClick={() => handleEditUser(user.userId)}>修改</button>
                      {/* 删除按钮传递 userId */}
                      <button onClick={() => handleDeleteUser(user.userId)}>删除</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <h3>添加新用户</h3>
            <form onSubmit={(e) => { e.preventDefault(); handleAddUser(); }}>
              <input
                type="text"
                placeholder="姓名"
                value={newUser.name}
                onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
                required
              />
              <input
                type="email"
                placeholder="邮箱"
                value={newUser.email}
                onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                required
              />
              <select
                value={newUser.role}
                onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
                required
              >
                <option value="">选择角色</option>
                <option value="STUDENT">学生</option>
                <option value="TEACHER">教师</option>
                <option value="ADMIN">管理员</option>
              </select>
              <button type="submit">添加用户</button>
            </form>
          </div>
        ) : (
          <p>请选择一个部门查看人员信息。</p>
        )}
      </div>
    </div>
  );
};

export default AdminControlPage;
