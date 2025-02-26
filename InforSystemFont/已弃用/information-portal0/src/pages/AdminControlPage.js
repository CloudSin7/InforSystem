import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/AdminControlPage.css'; // 导入样式文件

const AdminControlPage = () => {
  const [departments, setDepartments] = useState([]);
  const [selectedDepartmentId, setSelectedDepartmentId] = useState(null);
  const [users, setUsers] = useState([]);

  // 获取所有部门信息
  useEffect(() => {
    axios.get('http://localhost:8088/api/departments')
      .then(response => {
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
          setUsers(response.data);
        })
        .catch(error => {
          console.error("无法获取用户信息:", error);
        });
    }
  }, [selectedDepartmentId]);

  const handleDepartmentClick = (departmentId) => {
    setSelectedDepartmentId(departmentId);
  };

  const handleEditUser = (userId) => {
    // 实现用户信息编辑的逻辑（例如弹出窗口）
    console.log(`编辑用户 ${userId}`);
  };

  return (
    <div className="admin-control-page">
      <div className="sidebar">
        <h3>部门列表</h3>
        <ul>
          {departments.map((department) => (
            <li key={department.id} onClick={() => handleDepartmentClick(department.id)}>
              {department.name}
            </li>
          ))}
        </ul>
      </div>
      <div className="content">
        <h3>部门人员信息</h3>
        {selectedDepartmentId ? (
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
                <tr key={user.id}>
                  <td>{user.name}</td>
                  <td>{user.email}</td>
                  <td>{user.role}</td>
                  <td>
                    <button onClick={() => handleEditUser(user.id)}>修改</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>请选择一个部门查看人员信息。</p>
        )}
      </div>
    </div>
  );
};

export default AdminControlPage;