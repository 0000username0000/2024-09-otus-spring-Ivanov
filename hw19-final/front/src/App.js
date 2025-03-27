import React from "react";
import { Routes, Route, Link, Navigate } from "react-router-dom";
import CategoryList from "./components/CategoryList";
import OrderList from "./components/OrderList";
import ProductList from "./components/ProductList";
import ProductDetails from "./components/ProductDetails";
import Login from "./components/Login";
import Logout from './components/Logout';

const PrivateRoute = ({ children }) => {
  const isAuthenticated = !!localStorage.getItem('accessToken');
  return isAuthenticated ? children : <Navigate to="/login" />;
};

const App = () => {
  return (
    <div className="app-container">
      <nav className="sidebar">
        <h2>Menu</h2>
        <ul>
          <li><Link to="/">Home</Link></li>
          <li><Link to="/categories">Categories</Link></li>
          <li><Link to="/orders">Orders</Link></li>
        </ul>
        <div className="logout-container">
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
        </Routes>
      </div>
    </div>
  );
};

const HomePage = () => (
  <div className="home-page">
    <h1>Welcome to E-Commerce Admin Panel</h1>
  </div>
);

export default App;