// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import UserHomePage from './pages/UserHomePage';
import NotificationDetailPage from './pages/NotificationDetailPage';
import InternalMessagePage from './pages/InternalMessagePage';
import AdminControlPage from './pages/AdminControlPage';

import { Navigate } from 'react-router-dom';


function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/home" element={<ProtectedRoute Component={UserHomePage} requiredRole="STUDENT" />} />
          <Route path="/notifications/:id" element={<ProtectedRoute Component={NotificationDetailPage} requiredRole="STUDENT" />} />
          <Route path="/messages" element={<ProtectedRoute Component={InternalMessagePage} requiredRole="STUDENT" />} />
          <Route path="/admin" element={<ProtectedRoute Component={AdminControlPage} requiredRole="ADMIN" />} />
        </Routes>
      </div>
    </Router>
  );
}

const ProtectedRoute = ({ Component, requiredRole }) => {
  const isAuthenticated = localStorage.getItem('user') !== null;
  const user = JSON.parse(localStorage.getItem('user'));

  if (!isAuthenticated || !user) {
    return <Navigate to="/login" replace />; // 使用 React-Router 的 Navigate 避免死循环跳转
  }

  if (requiredRole && user.userType !== requiredRole) {
    return <Navigate to="/" replace />; // 未授权用户跳转回主页
  }

  return <Component />;
};


export default App;
