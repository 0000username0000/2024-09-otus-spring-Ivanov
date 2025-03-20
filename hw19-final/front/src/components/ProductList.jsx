import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import "./table.css"; // Импортируем стили

const ProductList = () => {
  const { categoryId } = useParams();
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState({
    name: "",
    description: "",
    price: 0,
    quantity: 0,
  });
  const [editingProduct, setEditingProduct] = useState(null);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  // Загрузка продуктов
  useEffect(() => {
    axios.get(`http://localhost:8080/api/products/category/${categoryId}`)
      .then((response) => setProducts(response.data))
      .catch((error) => console.error("Error fetching products:", error));
  }, [categoryId]);

  // Обработчик добавления продукта
  const handleAddProduct = () => {
    axios.post(`http://localhost:8080/api/products`, { ...newProduct, categoryId })
      .then((response) => {
        setProducts([...products, response.data]);
        setIsAddModalOpen(false);
        setNewProduct({ name: "", description: "", price: 0, quantity: 0 });
      })
      .catch((error) => console.error("Error adding product:", error));
  };

  // Обработчик редактирования продукта
  const handleEditProduct = (product) => {
    setEditingProduct(product);
    setIsEditModalOpen(true);
  };

  // Обработчик сохранения изменений
  const handleSaveProduct = () => {
    axios.put(`http://localhost:8080/api/products/${editingProduct.id}`, editingProduct)
      .then((response) => {
        setProducts(products.map((p) => (p.id === editingProduct.id ? response.data : p)));
        setIsEditModalOpen(false);
        setEditingProduct(null);
      })
      .catch((error) => console.error("Error updating product:", error));
  };

  // Обработчик удаления продукта
  const handleDeleteProduct = (productId) => {
    axios.delete(`http://localhost:8080/api/products/${productId}`)
      .then(() => {
        setProducts(products.filter((p) => p.id !== productId));
      })
      .catch((error) => console.error("Error deleting product:", error));
  };

  return (
    <div>
      <h2>Products</h2>
      <button onClick={() => setIsAddModalOpen(true)}>Add Product</button>

      {/* Модальное окно для добавления продукта */}
      {isAddModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <h3>Add New Product</h3>
            <input
              type="text"
              placeholder="Name"
              value={newProduct.name}
              onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
            />
            <input
              type="text"
              placeholder="Description"
              value={newProduct.description}
              onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
            />
            <input
              type="number"
              placeholder="Price"
              value={newProduct.price}
              onChange={(e) => setNewProduct({ ...newProduct, price: parseFloat(e.target.value) })}
            />
            <input
              type="number"
              placeholder="Quantity"
              value={newProduct.quantity}
              onChange={(e) => setNewProduct({ ...newProduct, quantity: parseInt(e.target.value) })}
            />
            <button onClick={handleAddProduct}>Save</button>
            <button onClick={() => setIsAddModalOpen(false)}>Cancel</button>
          </div>
        </div>
      )}

      {/* Модальное окно для редактирования продукта */}
      {isEditModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <h3>Edit Product</h3>
            <input
              type="text"
              placeholder="Name"
              value={editingProduct.name}
              onChange={(e) => setEditingProduct({ ...editingProduct, name: e.target.value })}
            />
            <input
              type="text"
              placeholder="Description"
              value={editingProduct.description}
              onChange={(e) => setEditingProduct({ ...editingProduct, description: e.target.value })}
            />
            <input
              type="number"
              placeholder="Price"
              value={editingProduct.price}
              onChange={(e) => setEditingProduct({ ...editingProduct, price: parseFloat(e.target.value) })}
            />
            <input
              type="number"
              placeholder="Quantity"
              value={editingProduct.quantity}
              onChange={(e) => setEditingProduct({ ...editingProduct, quantity: parseInt(e.target.value) })}
            />
            <button onClick={handleSaveProduct}>Save</button>
            <button onClick={() => setIsEditModalOpen(false)}>Cancel</button>
          </div>
        </div>
      )}

      <table className="product-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
              <td>{product.description}</td>
              <td>{product.price}</td>
              <td>{product.quantity}</td>
              <td>
                <Link to={`/products/${product.id}`}>View Details</Link>
                <button onClick={() => handleEditProduct(product)}>Edit</button>
                <button onClick={() => handleDeleteProduct(product.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductList;