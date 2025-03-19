import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import CategoryList from "./components/CategoryList";
import ProductList from "./components/ProductList";
import ProductDetails from "./components/ProductDetails";

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<CategoryList />} />
        <Route path="/categories/:categoryId/products" element={<ProductList />} />
        <Route path="/products/:productId" element={<ProductDetails />} />
      </Routes>
    </Router>
  );
};

export default App;