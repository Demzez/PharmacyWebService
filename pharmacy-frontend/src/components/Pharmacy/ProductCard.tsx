import React, { useState } from 'react';
import { Product } from '../../types';
import { productService } from '../../services/productService';
import { reservationService } from '../../services/reservationService';
import { authService } from "../../services/authService";

interface ProductCardProps {
    product: Product;
    onReservationSuccess: () => void;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, onReservationSuccess}) => {
    const [quantity, setQuantity] = useState(1);
    const [showAnalogs, setShowAnalogs] = useState(false);
    const [analogs, setAnalogs] = useState<Product[]>([]);
    const [loadingAnalogs, setLoadingAnalogs] = useState(false);
    const [reserving, setReserving] = useState(false);
    const user = authService.getCurrentUser();
    const isAdmin = user?.role == 'ROLE_ADMIN';



    const handleReservation = async () => {
        setReserving(true);
        try {
            await reservationService.createReservation(product.id, quantity);
            alert('Товар успешно забронирован!');
            onReservationSuccess();
        } catch (error: any) {
            const errorMessage = error.response?.data?.error ||
                error.message ||
                'Ошибка бронирования';
            alert(errorMessage);
        } finally {
            setReserving(false);
        }
    };

    const handleShowAnalogs = async () => {
        if (showAnalogs) {
            setShowAnalogs(false);
            return;
        }

        try {
            setLoadingAnalogs(true);
            const analogsData = await productService.getAnalogs(product.id);
            setAnalogs(analogsData);
            setShowAnalogs(true);
        } catch (error: any) {
            const errorMessage = error.response?.data?.error ||
                error.message ||
                'Ошибка загрузки аналогов';
            alert(errorMessage);
        } finally {
            setLoadingAnalogs(false);
        }
    };

    return (
        <div className="product-card">
            <div className="product-header">
                <h3>{product.name}</h3>
                <span className={`status ${product.available ? 'available' : 'unavailable'}`}>
                    {product.available ? 'В наличии' : 'Нет в наличии'}
                </span>
            </div>

            <div className="product-details">
                <p><strong>Производитель:</strong> {product.manufacturer}</p>
                <p><strong>Форма выпуска:</strong> {product.releaseForm}</p>
                <p><strong>Действующее вещество:</strong> {product.activeSubstance}</p>
                <p><strong>Категория:</strong> {product.category || 'Не указана'}</p>
                <p><strong>Рецептурный:</strong> {product.prescriptionStatus === 'PRESCRIPTION' ? 'Да' : 'Нет'}</p>
                <p><strong>Цена:</strong> {product.price.toFixed(2)} BYN</p>
                <p><strong>В наличии:</strong> {product.stockQuantity} шт.</p>
            </div>

            {product.available ? (
                <div className="product-actions">
                    <div className="quantity-selector">
                        <label>Количество:</label>
                        <input
                            type="number"
                            min="1"
                            max={product.stockQuantity}
                            value={quantity}
                            onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                            className="quantity-input"
                        />
                    </div>
                    <button
                        onClick={handleReservation}
                        className="btn-primary"
                        disabled={quantity > product.stockQuantity || reserving || isAdmin}
                    >
                        {reserving ? 'Бронирование...' : 'Забронировать'}
                    </button>
                    <button
                        onClick={handleShowAnalogs}
                        className="btn-secondary"
                        disabled={loadingAnalogs}
                    >
                        {loadingAnalogs ? 'Загрузка...' : showAnalogs ? 'Скрыть аналоги' : 'Показать аналоги'}
                    </button>
                </div>
            ) : (
                <div className="product-actions">
                    <button
                        onClick={handleShowAnalogs}
                        className="btn-primary"
                        disabled={loadingAnalogs}
                    >
                        {loadingAnalogs ? 'Загрузка...' : 'Найти аналоги'}
                    </button>
                </div>
            )}
            {showAnalogs && analogs.length > 0 && (
                <div className="analogs-section">
                    <h4>Аналоги:</h4>
                    {analogs.map(analog => (
                        <div key={analog.id} className="analog-card">
                            <div className="analog-info">
                                <h4 className="analog-name">
                                    {analog.name}
                                </h4>
                            </div>
                            <button
                                className="btn-primary btn-small"
                                style={{
                                    width: '140px',
                                    height: '36px',
                                    fontSize: '14px',
                                    padding: '8px 12px',
                                    flexShrink: 0
                                }}
                                onClick={() => {
                                    navigator.clipboard.writeText(analog.name)
                                }}
                            >
                                Копировать имя
                            </button>
                        </div>
                    ))}
                </div>
            )}

            {showAnalogs && analogs.length === 0 && (
                <div className="analogs-section">
                    <h4>Аналоги:</h4>
                    <p className="no-analogs">Аналоги не найдены</p>
                </div>
            )}
        </div>
    );
};

export default ProductCard;