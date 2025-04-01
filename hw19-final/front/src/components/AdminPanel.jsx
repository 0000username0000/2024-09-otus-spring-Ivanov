import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import "./table.css";

const AdminPanel = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingUser, setEditingUser] = useState(null);
    const navigate = useNavigate();

    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8081';

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const token = localStorage.getItem('accessToken');
            if (!token) {
                navigate('/login');
                return;
            }

            const response = await axios.get(`${API_BASE_URL}/api/admin/users`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setUsers(response.data);
        } catch (err) {
            handleError(err);
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (user) => {
        setEditingUser({...user});
    };

    const handleSave = async () => {
        try {
            const token = localStorage.getItem('accessToken');
            await axios.put(
                `${API_BASE_URL}/api/admin/users/${editingUser.id}`,
                editingUser,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            setEditingUser(null);
            await fetchUsers();
        } catch (err) {
            handleError(err);
        }
    };

    const handleDelete = async (userId) => {
        try {
            const token = localStorage.getItem('accessToken');
            await axios.delete(`${API_BASE_URL}/api/admin/users/${userId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            await fetchUsers();
        } catch (err) {
            handleError(err);
        }
    };

    const handleError = (err) => {
        console.error('Error:', err);
        if (err.response?.status === 401) {
            navigate('/login');
        } else {
            setError(err.response?.data?.message || err.message || 'An error occurred');
        }
    };

    if (loading) return <div className="loading">Loading...</div>;
    if (error) return <div className="error">Error: {error}</div>;

    return (
        <div className="admin-panel">
            <h2>User Management</h2>

            {editingUser && (
                <div className="edit-modal">
                    <h3>Edit User</h3>
                    <div className="form-group">
                        <label>Login:</label>
                        <input
                            type="text"
                            value={editingUser.login}
                            onChange={(e) => setEditingUser({...editingUser, login: e.target.value})}
                        />
                    </div>
                    <div className="form-group">
                        <label>First Name:</label>
                        <input
                            type="text"
                            value={editingUser.firstName || ''}
                            onChange={(e) => setEditingUser({...editingUser, firstName: e.target.value})}
                        />
                    </div>
                    <div className="form-group">
                        <label>Last Name:</label>
                        <input
                            type="text"
                            value={editingUser.lastName || ''}
                            onChange={(e) => setEditingUser({...editingUser, lastName: e.target.value})}
                        />
                    </div>
                    <div className="button-group">
                        <button onClick={handleSave}>Save</button>
                        <button onClick={() => setEditingUser(null)}>Cancel</button>
                    </div>
                </div>
            )}

            {users.length === 0 ? (
                <div className="no-users">No users found</div>
            ) : (
                <table className="users-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Login</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Roles</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.login}</td>
                                <td>{user.firstName || '-'}</td>
                                <td>{user.lastName || '-'}</td>
                                <td>
                                    {user.roles?.map(role => role.name).join(', ') || 'No roles'}
                                </td>
                                <td>
{/*                                     <button */}
{/*                                         className="edit-btn" */}
{/*                                         onClick={() => handleEdit(user)} */}
{/*                                     > */}
{/*                                         Edit */}
{/*                                     </button> */}
{/*                                     <button */}
{/*                                         className="delete-btn" */}
{/*                                         onClick={() => handleDelete(user.id)} */}
{/*                                     > */}
{/*                                         Delete */}
{/*                                     </button> */}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default AdminPanel;