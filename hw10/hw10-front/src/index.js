// src/index.js

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

const rootStyle = {
  padding: '50px',
  fontFamily: 'Arial, sans-serif',
};

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <div style={rootStyle}>
      <App />
    </div>
  </React.StrictMode>
);
