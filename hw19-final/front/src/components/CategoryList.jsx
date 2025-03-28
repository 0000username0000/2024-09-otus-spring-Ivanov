import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./table.css";

const CategoryList = () => {
  const [categories, setCategories] = useState([]);
  const [editingCategory, setEditingCategory] = useState(null);
  const [newCategoryName, setNewCategoryName] = useState("");
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

  const handleViewProducts = (categoryId) => {
    navigate(`/categories/${categoryId}/products`);
  };

  const handleCreateCategory = async () => {
    if (!newCategoryName.trim()) {
      setError("Category name cannot be empty");
      return;
    }

    setIsLoading(true);
    try {
      await axios.post("/api/categories", { name: newCategoryName });
      setNewCategoryName("");
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
    setEditingCategory(category);
    setNewCategoryName(category.name);
  };

  const handleUpdateCategory = async () => {
    if (!newCategoryName.trim()) {
      setError("Category name cannot be empty");
      return;
    }

    setIsLoading(true);
    try {
      await axios.put(`/api/categories/${editingCategory.id}`, {
        name: newCategoryName
      });
      setEditingCategory(null);
      setNewCategoryName("");
      setError("");
      await fetchCategories();
    } catch (error) {
      console.error("Error updating category:", error);
      setError(error.response?.data?.message || "Error updating category");
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
      if (error.response) {
        if (error.response.status === 400) {
          setError(error.response.data?.message || "Cannot delete category with existing products");
        } else if (error.response.status === 404) {
          setError("Category not found");
        } else {
          setError(`Server error: ${error.response.status}`);
        }
      } else {
        setError("Network error - could not connect to server");
      }
    } finally {
      setIsLoading(false);
    }
  };

  const cancelEdit = () => {
    setEditingCategory(null);
    setNewCategoryName("");
    setError("");
  };

  return (
    <div className="category-container">
      <h2>Categories</h2>

      {/* Форма для создания/редактирования */}
      <div className="category-form">
        <input
          type="text"
          value={newCategoryName}
          onChange={(e) => setNewCategoryName(e.target.value)}
          placeholder="Category name"
          disabled={isLoading}
        />

        {editingCategory ? (
          <div className="form-actions">
            <button
              onClick={handleUpdateCategory}
              disabled={isLoading}
            >
              {isLoading ? "Updating..." : "Update Category"}
            </button>
            <button
              onClick={cancelEdit}
              disabled={isLoading}
            >
              Cancel
            </button>
          </div>
        ) : (
          <button
            onClick={handleCreateCategory}
            disabled={isLoading}
          >
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
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((category) => (
              <tr key={category.id}>
                <td>{category.name}</td>
                <td className="actions">
                  <button
                    onClick={() => handleViewProducts(category.id)}
                    disabled={isLoading}
                  >
                    View Products
                  </button>
                  <button
                    onClick={() => handleStartEdit(category)}
                    disabled={isLoading}
                  >
                    Edit
                  </button>
                  {category.productCount > 0 ? (
                    <span/>
                  ) : (
                    <button
                      onClick={() => handleDeleteCategory(category.id)}
                      disabled={isLoading}
                    >
                      Delete
                    </button>
                  )}
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