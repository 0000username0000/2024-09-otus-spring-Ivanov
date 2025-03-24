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
        { value: 'CREATED', label: 'Создан' },
        { value: 'PROCESSING', label: 'В процессе' },
        { value: 'COMPLETED', label: 'Завершен' },
        { value: 'CANCELLED', label: 'Отменен' }
    ];

    const getStatusDescription = (status) => {
        const found = orderStatuses.find(s => s.value === status);
        return found ? found.label : status;
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [ordersRes, productsRes] = await Promise.all([
                    axios.get('http://localhost:8080/api/orders'),
                    axios.get('http://localhost:8080/api/products')
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
            await axios.delete(`http://localhost:8080/api/orders/${id}`);
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
            const orderData = {
                status: formData.status,
                orderItems: formData.orderItems.map(item => ({
                    productId: item.productId,
                    quantity: item.quantity,
                    price: item.price
                })),
                totalPrice: formData.totalPrice
            };

            let response;
            if (editingOrder) {
                response = await axios.put(
                    `http://localhost:8080/api/orders/${editingOrder.id}`,
                    orderData
                );
                setOrders(orders.map(order =>
                    order.id === response.data.id ? response.data : order
                ));
            } else {
                response = await axios.post(
                    'http://localhost:8080/api/orders',
                    orderData
                );
                setOrders([...orders, response.data]);
            }

            setIsModalOpen(false);
        } catch (err) {
            setError(err.response?.data?.message || err.message);
        }
    };

    const handleViewDetails = (orderId) => {
        navigate(`/orders/${orderId}`);
    };

    if (loading) return <div>Загрузка...</div>;
    if (error) return <div>Ошибка: {error}</div>;

    return (
        <div className="order-container">
            <h2>Управление заказами</h2>

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
                Добавить новый заказ
            </button>

            <table className="order-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Дата заказа</th>
                        <th>Статус</th>
                        <th>Сумма</th>
                        <th>Товаров</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map(order => (
                        <tr key={order.id}>
                            <td>{order.id}</td>
                            <td>{new Date(order.orderDate).toLocaleString()}</td>
                            <td>{getStatusDescription(order.status)}</td>
                            <td>{order.totalPrice.toFixed(2)} ₽</td>
                            <td>{order.orderItems?.length || 0}</td>
                            <td className="actions">
                                <button
                                    className="view-btn"
                                    onClick={() => handleViewDetails(order.id)}
                                >
                                    Просмотр
                                </button>
                                <button
                                    className="edit-btn"
                                    onClick={() => handleEdit(order)}
                                >
                                    Редактировать
                                </button>
                                <button
                                    className="delete-btn"
                                    onClick={() => handleDelete(order.id)}
                                >
                                    Удалить
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

                        <h2>{editingOrder ? 'Редактировать заказ' : 'Добавить новый заказ'}</h2>

                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Статус:</label>
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

                            <h3>Товары в заказе:</h3>
                            {formData.orderItems.map((item, index) => (
                                <div key={index} className="product-row">
                                    <div className="form-group">
                                        <label>Товар:</label>
                                        <select
                                            value={item.productId}
                                            onChange={(e) => handleProductSelect(e, index)}
                                            required
                                        >
                                            <option value="">Выберите товар</option>
                                            {products.map(product => (
                                                <option
                                                    key={product.id}
                                                    value={product.id}
                                                    disabled={formData.orderItems.some((i, idx) =>
                                                        idx !== index && i.productId === product.id
                                                    )}
                                                >
                                                    {product.name} ({product.price} ₽, доступно: {product.quantity})
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    <div className="form-group">
                                        <label>Количество:</label>
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
                                        <label>Цена за единицу:</label>
                                        <input
                                            type="number"
                                            value={item.price || 0}
                                            readOnly
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Сумма:</label>
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
                                        Удалить
                                    </button>
                                </div>
                            ))}

                            <button
                                type="button"
                                className="add-product-btn"
                                onClick={addProductRow}
                            >
                                Добавить товар
                            </button>

                            <div className="form-group total-price">
                                <label>Общая сумма:</label>
                                <input
                                    type="number"
                                    value={formData.totalPrice.toFixed(2)}
                                    readOnly
                                />
                                <span>₽</span>
                            </div>

                            <button type="submit" className="submit-btn">
                                {editingOrder ? 'Обновить' : 'Создать'}
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default OrderList;