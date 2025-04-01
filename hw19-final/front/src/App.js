import React, { useState, useEffect } from "react";
import { Routes, Route, Link, Navigate } from "react-router-dom";
import axios from 'axios';
import CategoryList from "./components/CategoryList";
import OrderList from "./components/OrderList";
import OrderDetails from "./components/OrderDetails";
import ProductList from "./components/ProductList";
import ProductDetails from "./components/ProductDetails";
import Login from "./components/Login";
import Logout from './components/Logout';
import AdminPanel from './components/AdminPanel';

const HomePage = () => (
  <div className="home-page">
    <h1>Welcome!</h1>
  </div>
);

const PrivateRoute = ({ children, adminOnly = false }) => {
  const token = localStorage.getItem('accessToken');

  if (!token) return <Navigate to="/login" />;

  if (adminOnly) {
    const isAdmin = localStorage.getItem('isAdmin') === 'true';
    if (!isAdmin) return <Navigate to="/" />;
  }

  return children;
};

const App = () => {
  const [userInfo, setUserInfo] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserInfo = async () => {
      const token = localStorage.getItem('accessToken');
      if (token) {
        try {
          const response = await axios.get('/auth/user-info', {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          setUserInfo(response.data);
          localStorage.setItem('isAdmin', response.data.roles.includes('ADMIN'));
        } catch (error) {
          console.error("Failed to fetch user info", error);
          localStorage.removeItem('accessToken');
          localStorage.removeItem('isAdmin');
        }
      }
      setLoading(false);
    };

    fetchUserInfo();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div className="app-container">
      <nav className="sidebar">
        <h2>Menu</h2>
        <ul>
          <li><Link to="/">Home</Link></li>
          <li><Link to="/categories">Categories</Link></li>
          <li><Link to="/orders">Orders</Link></li>
          {userInfo?.roles.includes('ADMIN') && (
            <li><Link to="/admin">Admin Panel</Link></li>
          )}
        </ul>
        <div className="logout-container">
          {userInfo && <span>Hello, {userInfo.firstName}</span>}
          <Logout />
        </div>
      </nav>

      <div className="content">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={
            <PrivateRoute>
              <HomePage />
            </PrivateRoute>
          } />
          <Route path="/categories" element={
            <PrivateRoute>
              <CategoryList />
            </PrivateRoute>
          } />
          <Route path="/categories/:categoryId/products" element={
            <PrivateRoute>
              <ProductList />
            </PrivateRoute>
          } />
          <Route path="/products/:productId" element={
            <PrivateRoute>
              <ProductDetails />
            </PrivateRoute>
          } />
          <Route path="/orders" element={
            <PrivateRoute>
              <OrderList />
            </PrivateRoute>
          } />
          <Route path="/orders/:orderId" element={
            <PrivateRoute>
              <OrderDetails />
            </PrivateRoute>
          } />
          <Route path="/admin" element={
            <PrivateRoute adminOnly>
              <AdminPanel />
            </PrivateRoute>
          } />
        </Routes>
      </div>
    </div>
  );
};

export default App;