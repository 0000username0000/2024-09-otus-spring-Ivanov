import React from 'react';
import { useNavigate } from 'react-router-dom';
import './style.css';


const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await fetch('/login', {
        method: 'GET',
        credentials: 'include',
      });

      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');

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