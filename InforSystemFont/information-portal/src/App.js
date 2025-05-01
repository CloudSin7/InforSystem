import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import UserHomePage from './pages/UserHomePage';
import SearchResultPage from './pages/SearchResultPage';
import AdminControlPage from './pages/AdminControlPage';
import NotificationDetailPage from './pages/NotificationDetailPage';
import NotificationCenterPage from './pages/NotificationCenterPage';


function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/home" element={<ProtectedRoute Component={UserHomePage} />} />
          <Route path="/admin" element={<ProtectedRoute Component={AdminControlPage} />} />
          <Route path="/search-results" element={<SearchResultPage />} />
          <Route path="/notifications/:id" element={<ProtectedRoute Component={NotificationDetailPage} />} />
          <Route path="/notification-center" element={<NotificationCenterPage />} />
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