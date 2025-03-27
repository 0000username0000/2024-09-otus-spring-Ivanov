import React from 'react';
import { useNavigate } from 'react-router-dom';
import './style.css';


const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await fetch('http://localhost:8081/logout', {
        method: 'POST',
        credentials: 'include',
      });

      // Удаляем токены из localStorage
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');

      // Перенаправляем на страницу входа
      navigate('/login');
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  return (
    <button onClick={handleLogout} className="logout-button">
      Logout
    </button>
  );
};

export default Logout;