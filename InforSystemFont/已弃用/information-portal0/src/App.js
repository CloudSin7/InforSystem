import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import UserHomePage from './pages/UserHomePage';
import NotificationDetailPage from './pages/NotificationDetailPage';
import InternalMessagePage from './pages/InternalMessagePage';
import AdminControlPage from './pages/AdminControlPage';




function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/home" element={<ProtectedRoute Component={UserHomePage} />} />
          <Route path="/notifications/:id" element={<ProtectedRoute Component={NotificationDetailPage} />} />
          <Route path="/messages" element={<ProtectedRoute Component={InternalMessagePage} />} />
          <Route path="/admin" element={<ProtectedRoute Component={AdminControlPage} />} />

        </Routes>
      </div>
    </Router>
  );
}

const ProtectedRoute = ({ Component }) => {
  const isAuthenticated = localStorage.getItem('user') !== null;
  return isAuthenticated ? <Component /> : (window.location.href = '/');
};

export default App;