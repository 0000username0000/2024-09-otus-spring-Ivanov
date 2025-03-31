import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./table.css";

const CategoryList = () => {
  const [categories, setCategories] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({ name: "" });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    setIsLoading(true);
    try {
      const response = await axios.get("/api/categories");
      setCategories(response.data);
      setError("");
    } catch (error) {
      console.error("Error fetching categories:", error);
      setError("Failed to load categories. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleCreateCategory = async () => {
    if (!formData.name.trim()) {
      setError("Category name cannot be empty");
      return;
    }

    setIsLoading(true);
    try {
      await axios.post("/api/categories", { name: formData.name });
      setFormData({ name: "" });
      setError("");
      await fetchCategories();
    } catch (error) {
      console.error("Error creating category:", error);
      setError(error.response?.data?.message || "Error creating category");
    } finally {
      setIsLoading(false);
    }
  };

  const handleStartEdit = (category) => {
    setEditingId(category.id);
    setFormData({ name: category.name });
  };

const handleUpdateCategory = async () => {
  if (!formData.name.trim()) {
    setError("Category name cannot be empty");
    return;
  }

  setIsLoading(true);
  try {
    await axios.put(`/api/categories/${editingId}`, {
      id: editingId,  // Добавляем ID в тело запроса
      name: formData.name,
      productCount: 0  // Добавляем обязательное поле
    });
    setEditingId(null);
    setFormData({ name: "" });
    setError("");
    await fetchCategories();
  } catch (error) {
    console.error("Error updating category:", error);
    setError(error.response?.data || "Error updating category");
  } finally {
    setIsLoading(false);
  }
};

  const handleDeleteCategory = async (categoryId) => {
    if (!window.confirm("Are you sure you want to delete this category?")) return;

    setIsLoading(true);
    try {
      await axios.delete(`/api/categories/${categoryId}`);
      await fetchCategories();
    } catch (error) {
      console.error("Error deleting category:", error);
      setError(error.response?.data?.message || "Error deleting category");
    } finally {
      setIsLoading(false);
    }
  };

  const cancelEdit = () => {
    setEditingId(null);
    setFormData({ name: "" });
    setError("");
  };

  return (
    <div className="category-container">
      <h2>Categories</h2>

      {/* Форма для создания/редактирования */}
      <div className="category-form">
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleInputChange}
          placeholder="Category name"
          disabled={isLoading}
        />

        {editingId ? (
          <div className="form-actions">
            <button onClick={handleUpdateCategory} disabled={isLoading}>
              {isLoading ? "Updating..." : "Update Category"}
            </button>
            <button onClick={cancelEdit} disabled={isLoading}>
              Cancel
            </button>
          </div>
        ) : (
          <button onClick={handleCreateCategory} disabled={isLoading}>
            {isLoading ? "Adding..." : "Add Category"}
          </button>
        )}

        {error && <div className="error-message">{error}</div>}
      </div>

      {/* Таблица категорий */}
      {isLoading && !categories.length ? (
        <div className="loading">Loading categories...</div>
      ) : (
        <table className="category-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Product Count</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((category) => (
              <tr key={category.id}>
                <td>{category.name}</td>
                <td>{category.productCount}</td>
                <td className="actions">
                  <button onClick={() => navigate(`/categories/${category.id}/products`)}>
                    View Products
                  </button>
                  <button onClick={() => handleStartEdit(category)}>
                    Edit
                  </button>
                  <button
                    onClick={() => handleDeleteCategory(category.id)}
                    disabled={category.productCount > 0}
                    title={category.productCount > 0 ? "Cannot delete category with products" : ""}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default CategoryList;