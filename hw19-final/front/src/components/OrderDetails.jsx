import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './table.css';

const OrderDetails = () => {
    const { orderId } = useParams();
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const token = localStorage.getItem('accessToken');
                const response = await axios.get(`/api/orders/${orderId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setOrder(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchOrder();
    }, [orderId]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;
    if (!order) return <div>Order not found</div>;

    return (
        <div className="order-details-container">
            <h2>Order Details</h2>
            <div className="order-info">
                <p><strong>Order Number:</strong> {order.orderNumber}</p>
                <p><strong>Date:</strong> {new Date(order.orderDate).toLocaleString()}</p>
                <p><strong>Status:</strong> {order.status}</p>
                <p><strong>Total Price:</strong> {order.totalPrice.toFixed(2)} ₽</p>
            </div>

            <h3>Order Items:</h3>
            <table className="order-items-table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    {order.orderItems.map(item => (
                        <tr key={item.id}>
                            <td>{item.productName}</td>
                            <td>{item.quantity}</td>
                            <td>{item.price} ₽</td>
                            <td>{(item.price * item.quantity).toFixed(2)} ₽</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <button
                className="back-button"
                onClick={() => navigate(-1)}
            >
                Back to Orders
            </button>
        </div>
    );
};

export default OrderDetails;