// src/index.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css'; // 这里是样式文件，你可以根据需要编写 CSS
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);