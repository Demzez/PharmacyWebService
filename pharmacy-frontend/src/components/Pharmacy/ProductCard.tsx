import React, { useState } from 'react';
import { Product } from '../../types';
import { productService } from '../../services/productService'; // правильный импорт
import { reservationService } from '../../services/reservationService'; // отдельный импорт

interface ProductCardProps {
    product: Product;
    onReservationSuccess: () => void;
}

interface ApiError {
    response?: {
        data?: {
            error?: string;
        };
    };
    message?: string;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, onReservationSuccess }) => {
    const [quantity, setQuantity] = useState(1);
    const [showAnalogs, setShowAnalogs] = useState(false);
    const [analogs, setAnalogs] = useState<Product[]>([]);
    const [loadingAnalogs, setLoadingAnalogs] = useState(false);

    const handleReservation = async () => {
        try {
            // Временное решение - в реальном приложении брать ID из контекста пользователя
            const userId = 1;
            await reservationService.createReservation(product.id, quantity, userId);
            alert('Товар забронирован!');
            onReservationSuccess();
        } catch (error: unknown) {
            const apiError = error as ApiError;
            alert(apiError.response?.data?.error || apiError.message || 'Ошибка бронирования');
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
        } catch (error: unknown) {
            const apiError = error as ApiError;
            alert(apiError.response?.data?.error || apiError.message || 'Ошибка загрузки аналогов');
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
                <p><strong>Категория:</strong> {product.category}</p>
                <p><strong>Рецептурный:</strong> {product.prescriptionStatus === 'PRESCRIPTION' ? 'Да' : 'Нет'}</p>
                <p><strong>Цена:</strong> ${product.price}</p>
                <p><strong>В наличии:</strong> {product.stockQuantity} шт.</p>
            </div>

            {product.available && (
                <div className="product-actions">
                    <div className="quantity-selector">
                        <label>Количество:</label>
                        <input
                            type="number"
                            min="1"
                            max={product.stockQuantity}
                            value={quantity}
                            onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                        />
                    </div>
                    <button
                        onClick={handleReservation}
                        className="btn-primary"
                        disabled={quantity > product.stockQuantity}
                    >
                        Забронировать
                    </button>
                    <button
                        onClick={handleShowAnalogs}
                        className="btn-secondary"
                        disabled={loadingAnalogs}
                    >
                        {loadingAnalogs ? 'Загрузка...' : showAnalogs ? 'Скрыть аналоги' : 'Показать аналоги'}
                    </button>
                </div>
            )}

            {showAnalogs && (
                <div className="analogs-section">
                    <h4>Аналоги:</h4>
                    {analogs.length > 0 ? (
                        analogs.map(analog => (
                            <div key={analog.id} className="analog-card">
                                <span>{analog.name}</span>
                                <span>${analog.price}</span>
                                <button
                                    onClick={() => {
                                        setQuantity(1);
                                        // Здесь можно добавить логику для быстрого переключения на аналог
                                    }}
                                    className="btn-primary btn-small"
                                >
                                    Выбрать
                                </button>
                            </div>
                        ))
                    ) : (
                        <p>Аналоги не найдены</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default ProductCard;