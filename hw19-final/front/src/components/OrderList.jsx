import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './table.css';

const OrderList = () => {
    const [orders, setOrders] = useState([]);
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingOrder, setEditingOrder] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        status: 'CREATED',
        orderItems: [],
        totalPrice: 0
    });

    const navigate = useNavigate();

    const orderStatuses = [
        { value: 'CREATED', label: 'Created' },
        { value: 'APPROVED', label: 'Approved' }
    ];

    const getStatusDescription = (status) => {
        const found = orderStatuses.find(s => s.value === status);
        return found ? found.label : status;
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [ordersRes, productsRes] = await Promise.all([
                    axios.get('/api/orders'),
                    axios.get('/api/products')
                ]);
                setOrders(ordersRes.data);
                setProducts(productsRes.data);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleDelete = async (id) => {
        try {
            await axios.delete(`/api/orders/${id}`);
            setOrders(orders.filter(order => order.id !== id));
        } catch (err) {
            setError(err.message);
        }
    };

    const handleEdit = (order) => {
        setEditingOrder(order);
        setFormData({
            status: order.status,
            orderItems: order.orderItems || [],
            totalPrice: order.totalPrice
        });
        setIsModalOpen(true);
    };

    const handleProductSelect = (e, index) => {
        const productId = e.target.value;
        const selectedProduct = products.find(p => p.id === productId);

        const updatedItems = [...formData.orderItems];
        updatedItems[index] = {
            ...updatedItems[index],
            productId,
            price: selectedProduct ? selectedProduct.price : 0,
            productName: selectedProduct ? selectedProduct.name : ''
        };

        setFormData({
            ...formData,
            orderItems: updatedItems,
            totalPrice: calculateTotal(updatedItems)
        });
    };

    const handleQuantityChange = (e, index) => {
        const quantity = parseInt(e.target.value) || 0;
        const updatedItems = [...formData.orderItems];
        updatedItems[index] = {
            ...updatedItems[index],
            quantity
        };

        setFormData({
            ...formData,
            orderItems: updatedItems,
            totalPrice: calculateTotal(updatedItems)
        });
    };

    const calculateTotal = (items) => {
        return items.reduce((sum, item) => {
            return sum + (item.price * (item.quantity || 0));
        }, 0);
    };

    const addProductRow = () => {
        setFormData({
            ...formData,
            orderItems: [...formData.orderItems, { productId: '', quantity: 1, price: 0 }]
        });
    };

    const removeProductRow = (index) => {
        const updatedItems = formData.orderItems.filter((_, i) => i !== index);
        setFormData({
            ...formData,
            orderItems: updatedItems,
            totalPrice: calculateTotal(updatedItems)
        });
    };

const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const token = localStorage.getItem('accessToken');
        const payload = JSON.parse(atob(token.split('.')[1]));

        // Prepare order items data
        const orderItemsData = formData.orderItems.map((item, index) => {
            const product = products.find(p => p.id === item.productId);
            const orderItem = {
                productId: item.productId,
                quantity: item.quantity,
                price: product.price,
                productName: product.name
            };

            // For existing items in an edited order, include the ID
            if (editingOrder && editingOrder.orderItems[index]?.id) {
                orderItem.id = editingOrder.orderItems[index].id;
            }

            // For existing order, include orderId
            if (editingOrder) {
                orderItem.orderId = editingOrder.id;
            }

            return orderItem;
        });

        // Prepare the order data
        const orderData = {
            status: formData.status,
            userId: payload.userId,
            totalPrice: formData.totalPrice,
            orderItems: orderItemsData
        };

        // For existing orders, add the required fields
        if (editingOrder) {
            orderData.id = editingOrder.id;
            orderData.orderNumber = editingOrder.orderNumber;
            orderData.orderDate = editingOrder.orderDate;
        } else {
            orderData.orderDate = new Date().toISOString();
        }

        let response;
        if (editingOrder) {
            response = await axios.put(`/api/orders/${editingOrder.id}`, orderData, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            setOrders(orders.map(order => order.id === editingOrder.id ? response.data : order));
        } else {
            response = await axios.post('/api/orders', orderData, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            setOrders([...orders, response.data]);
        }

        setIsModalOpen(false);
        setError(null); // Clear any previous errors
    } catch (err) {
        console.error('Error:', err.response?.data);
        setError(err.response?.data?.message || err.message || "An error occurred");
    }
};

    const handleViewDetails = (orderId) => {
        navigate(`/orders/${orderId}`);
    };

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="order-container">
            <h2>Order Management</h2>

            <button
                className="add-button"
                onClick={() => {
                    setEditingOrder(null);
                    setFormData({
                        status: 'CREATED',
                        orderItems: [{ productId: '', quantity: 1, price: 0 }],
                        totalPrice: 0
                    });
                    setIsModalOpen(true);
                }}
            >
                Create New Order
            </button>

            <table className="order-table">
                <thead>
                    <tr>
                        <th>Order Number</th>
                        <th>Order Date</th>
                        <th>Status</th>
                        <th>Total</th>
                        <th>Items Count</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map(order => (
                        <tr key={order.id}>
                            <td>{order.orderNumber}</td>
                            <td>{new Date(order.orderDate).toLocaleString()}</td>
                            <td>{getStatusDescription(order.status)}</td>
                            <td>{order.totalPrice.toFixed(2)} ₽</td>
                            <td>{order.orderItems?.length || 0}</td>
                            <td className="actions">
                                <button
                                    className="view-btn"
                                    onClick={() => handleViewDetails(order.id)}
                                >
                                    View
                                </button>
                                <button
                                    className="edit-btn"
                                    onClick={() => handleEdit(order)}
                                >
                                    Edit
                                </button>
                                <button
                                    className="delete-btn"
                                    onClick={() => handleDelete(order.id)}
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {isModalOpen && (
                <div className="modal">
                    <div className="modal-content">
                        <span
                            className="close"
                            onClick={() => {
                                setIsModalOpen(false);
                                setEditingOrder(null);
                            }}
                        >
                            &times;
                        </span>

                        <h2>{editingOrder ? 'Edit Order' : 'Create New Order'}</h2>

                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Status:</label>
                                <select
                                    name="status"
                                    value={formData.status}
                                    onChange={(e) => setFormData({...formData, status: e.target.value})}
                                    required
                                >
                                    {orderStatuses.map(status => (
                                        <option key={status.value} value={status.value}>
                                            {status.label}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <h3>Order Items:</h3>
                            {formData.orderItems.map((item, index) => (
                                <div key={index} className="product-row">
                                    <div className="form-group">
                                        <label>Product:</label>
                                        <select
                                            value={item.productId}
                                            onChange={(e) => handleProductSelect(e, index)}
                                            required
                                        >
                                            <option value="">Select product</option>
                                            {products.map(product => (
                                                <option
                                                    key={product.id}
                                                    value={product.id}
                                                    disabled={formData.orderItems.some((i, idx) =>
                                                        idx !== index && i.productId === product.id
                                                    )}
                                                >
                                                    {product.name} ({product.price} ₽, available: {product.quantity})
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    <div className="form-group">
                                        <label>Quantity:</label>
                                        <input
                                            type="number"
                                            min="1"
                                            max={
                                                products.find(p => p.id === item.productId)?.quantity || 1
                                            }
                                            value={item.quantity || 1}
                                            onChange={(e) => handleQuantityChange(e, index)}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Unit Price:</label>
                                        <input
                                            type="number"
                                            value={item.price || 0}
                                            readOnly
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Total:</label>
                                        <input
                                            type="number"
                                            value={(item.price * (item.quantity || 0)).toFixed(2)}
                                            readOnly
                                        />
                                    </div>

                                    <button
                                        type="button"
                                        className="remove-btn"
                                        onClick={() => removeProductRow(index)}
                                    >
                                        Remove
                                    </button>
                                </div>
                            ))}

                            <button
                                type="button"
                                className="add-product-btn"
                                onClick={addProductRow}
                            >
                                Add Product
                            </button>

                            <div className="form-group total-price">
                                <label>Grand Total:</label>
                                <input
                                    type="number"
                                    value={formData.totalPrice.toFixed(2)}
                                    readOnly
                                />
                                <span>₽</span>
                            </div>

                            <button type="submit" className="submit-btn">
                                {editingOrder ? 'Update' : 'Create'}
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default OrderList;