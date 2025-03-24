import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import CategoryList from "./components/CategoryList";
import OrderList from "./components/OrderList";
import ProductList from "./components/ProductList";
import ProductDetails from "./components/ProductDetails";

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
      </nav>

      <div className="content">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/categories" element={<CategoryList />} />
          <Route path="/categories/:categoryId/products" element={<ProductList />} />
          <Route path="/products/:productId" element={<ProductDetails />} />
          <Route path="/orders" element={<OrderList />} />
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