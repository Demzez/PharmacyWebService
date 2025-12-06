import React, { useState, useEffect } from 'react';
import './ProductManagement.css'
import { Product } from '../../types';
import {adminService} from '../../services/productService';
import SearchBar from '../Pharmacy/SearchBar'

const ProductManagement: React.FC = () => {
    const [products, setProducts] = useState<Product[]>([]);
    const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
    const [showForm, setShowForm] = useState(false);
    const [editingProduct, setEditingProduct] = useState<Product | null>(null);
    const [formData, setFormData] = useState({
        name: '',
        manufacturer: '',
        releaseForm: '',
        expiryDate: '',
        prescriptionStatus: 'NON_PRESCRIPTION' as 'NON_PRESCRIPTION' | 'PRESCRIPTION',
        price: 0,
        stockQuantity: 0,
        activeSubstance: '',
        category: '',
        visible: true // Только visible
    });

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        try {
            const data = await adminService.getCatalog();
            setProducts(data);
            setFilteredProducts(data);
        } catch (error) {
            console.error('Error loading products:', error);
        }
    };

    const handleSearch = async (query: string) => {
        if (!query.trim()) {
            setFilteredProducts(products);
            return;
        }

        try {
            const results = await adminService.adminSearchProducts(query);
            setFilteredProducts(results);
        } catch (error) {
            console.error('Search error:', error);
        }
    };

    // Функция для переключения только visibility статуса
    const toggleVisibility = async (id: number) => {
        try {
            // Отправляем запрос на бэкенд для переключения visibility
            await adminService.toggleProductVisibility(id);

            // Обновляем состояние локально - ТОЛЬКО поле visible
            setProducts(prevProducts =>
                prevProducts.map(product =>
                    product.id === id
                        ? { ...product, visible: !product.visible }
                        : product
                )
            );

            setFilteredProducts(prevFilteredProducts =>
                prevFilteredProducts.map(product =>
                    product.id === id
                        ? { ...product, visible: !product.visible }
                        : product
                )
            );
        } catch (error) {
            console.error('Error toggling visibility:', error);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (editingProduct) {
                await adminService.updateProduct(editingProduct.id, formData);
            } else {
                await adminService.createProduct(formData);
            }
            setShowForm(false);
            setEditingProduct(null);
            setFormData({
                name: '',
                manufacturer: '',
                releaseForm: '',
                expiryDate: '',
                prescriptionStatus: 'NON_PRESCRIPTION',
                price: 0,
                stockQuantity: 0,
                activeSubstance: '',
                category: '',
                visible: true
            });
            loadProducts();
        } catch (error) {
            console.error('Error saving product:', error);
        }
    };

    const handleEdit = (product: Product) => {
        setEditingProduct(product);
        setFormData({
            name: product.name,
            manufacturer: product.manufacturer,
            releaseForm: product.releaseForm,
            expiryDate: product.expiryDate,
            prescriptionStatus: product.prescriptionStatus,
            price: product.price,
            stockQuantity: product.stockQuantity,
            activeSubstance: product.activeSubstance,
            category: product.category || '',
            visible: product.visible // Используем visible
        });
        setShowForm(true);
    };

    return (
        <div className="admin-page">
            <div className="page-header">
                <h1>Управление товарами</h1>
                <button
                    onClick={() => setShowForm(true)}
                    className="btn-primary"
                >
                    Добавить товар
                </button>
            </div>
            <div>
                <SearchBar onSearch={handleSearch} />
            </div>

            {showForm && (
                <div className="modal-overlay">
                    <div className="modal">
                        <h2>{editingProduct ? 'Редактировать' : 'Добавить'} товар</h2>
                        <form onSubmit={handleSubmit} className="product-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Название:</label>
                                    <input
                                        type="text"
                                        value={formData.name}
                                        onChange={(e) => setFormData({...formData, name: e.target.value})}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Производитель:</label>
                                    <input
                                        type="text"
                                        value={formData.manufacturer}
                                        onChange={(e) => setFormData({...formData, manufacturer: e.target.value})}
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label>Форма выпуска:</label>
                                    <input
                                        type="text"
                                        value={formData.releaseForm}
                                        onChange={(e) => setFormData({...formData, releaseForm: e.target.value})}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Срок годности:</label>
                                    <input
                                        type="date"
                                        value={formData.expiryDate}
                                        onChange={(e) => setFormData({...formData, expiryDate: e.target.value})}
                                        required
                                    />
                                </div>
                            </div>
                            <div className="form-row">

                                <div className="form-group">
                                    <label>Цена:</label>
                                    <input
                                        type="number"
                                        step="0.01"
                                        min="0"
                                        value={formData.price}
                                        onChange={(e) => setFormData({...formData, price: parseFloat(e.target.value) || 0})}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Категория:</label>
                                    <input
                                        type="text"
                                        value={formData.category}
                                        onChange={(e) => setFormData({...formData, category: e.target.value})}
                                    />
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Количество на складе:</label>
                                    <input
                                        type="number"
                                        min="0"
                                        value={formData.stockQuantity}
                                        onChange={(e) => setFormData({...formData, stockQuantity: parseInt(e.target.value) || 0})}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Действующее вещество:</label>
                                    <input
                                        type="text"
                                        value={formData.activeSubstance}
                                        onChange={(e) => setFormData({...formData, activeSubstance: e.target.value})}
                                        required
                                    />
                                </div>
                            </div>
                            <div className="form-row">

                            </div>
                            <div className="form-group">
                                <label>Рецептурный статус:</label>
                                <select
                                    value={formData.prescriptionStatus}
                                    onChange={(e) => setFormData({...formData, prescriptionStatus: e.target.value as 'PRESCRIPTION' | 'NON_PRESCRIPTION'})}
                                >
                                    <option value="NON_PRESCRIPTION">Без рецепта</option>
                                    <option value="PRESCRIPTION">По рецепту</option>
                                </select>
                            </div>
                            <div className="form-actions">
                                <button type="submit" className="btn-primary">
                                    {editingProduct ? 'Обновить' : 'Создать'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => {
                                        setShowForm(false);
                                        setEditingProduct(null);
                                    }}
                                    className="btn-secondary"
                                >
                                    Отмена
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <div className="products-table">
                <table style={{ tableLayout: 'fixed', width: '100%' }}>
                    <thead>
                    <tr>
                        <th style={{ width: '15%' }}>Название</th>
                        <th style={{ width: '18%' }}>Производитель</th>
                        <th style={{ width: '10%' }}>Цена</th>
                        <th style={{ width: '10%' }}>В наличии</th>
                        <th style={{ width: '15%' }}>Статус видимости</th>
                        <th style={{ width: '15%' }}>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredProducts.map(product => (
                        <tr key={product.id}>
                            <td>{product.name}</td>
                            <td>{product.manufacturer}</td>
                            <td>{product.price.toFixed(2)} BYN</td>
                            <td>{product.stockQuantity}</td>
                            <td>
                                <button
                                    onClick={() => toggleVisibility(product.id)}
                                    className={`status-toggle-btn ${product.visible ? 'visible' : 'hidden'}`}
                                    title="Кликните чтобы изменить видимость товара"
                                >
                                    {product.visible ? 'Видимый' : 'Скрытый'}
                                </button>
                            </td>
                            <td>
                                <button
                                    onClick={() => handleEdit(product)}
                                    className="btn-edit"
                                >
                                    Редактировать
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ProductManagement;