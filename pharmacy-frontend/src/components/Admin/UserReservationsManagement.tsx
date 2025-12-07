import React, { useState} from 'react';
import { Reservation } from '../../types';
import { adminService } from '../../services/productService';
import { reservationService } from '../../services/reservationService';
import './UserReservationsManagement.css';

const UserReservationsManagement: React.FC = () => {
    // Состояния для поиска и отображения
    const [userLogin, setUserLogin] = useState<string>('');
    const [reservations, setReservations] = useState<Reservation[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');
    const [successMessage, setSuccessMessage] = useState<string>('');

    // Загрузка бронирований пользователя по логину
    const loadUserReservations = async () => {
        if (!userLogin.trim()) {
            setError('Введите логин пользователя');
            return;
        }

        setLoading(true);
        setError('');
        setSuccessMessage('');
        setReservations([]);

        try {
            const data = await adminService.getUserReservationsByLogin(userLogin);
            setReservations(data);

            if (data.length === 0) {
                setSuccessMessage(`У пользователя "${userLogin}" нет бронирований`);
            } else {
                setSuccessMessage(`Найдено ${data.length} бронирований для пользователя "${userLogin}"`);
                setTimeout(() => setSuccessMessage(''), 3000);
            }
        } catch (err: any) {
            if (err.response?.status === 404) {
                setError(`Пользователь "${userLogin}" не найден`);
            } else {
                setError(err.response?.data?.error || 'Ошибка загрузки бронирований');
            }
            setReservations([]);
        } finally {
            setLoading(false);
        }
    };

    // Подтверждение бронирования (выкуп)
    const handleComplete = async (reservationId: number) => {
        if (!window.confirm('Подтвердить выкуп товара? Это действие нельзя отменить.')) {
            return;
        }

        try {
            await reservationService.completeReservation(reservationId);

            // Обновляем список бронирований
            await loadUserReservations();
            setSuccessMessage('Бронь успешно подтверждена (товар выкуплен)');

            // Убираем сообщение через 3 секунды
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (err: any) {
            setError(err.response?.data?.error || 'Не удалось подтвердить бронь');
        }
    };

    // Отмена бронирования
    const handleCancel = async (reservationId: number) => {
        if (!window.confirm('Отменить бронирование? Товар вернется на склад.')) {
            return;
        }

        try {
            await reservationService.cancelReservation(reservationId);

            // Обновляем список бронирований
            await loadUserReservations();
            setSuccessMessage('Бронирование успешно отменено');

            // Убираем сообщение через 3 секунды
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (err: any) {
            setError(err.response?.data?.error || 'Не удалось отменить бронь');
        }
    };

    // Форматирование даты
    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleString('ru-RU', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    // Получение текста статуса
    const getStatusText = (status: string, completed: boolean) => {
        if (completed) return 'Выкуплено';
        if (status === 'EXPIRED') return 'Просрочено';
        if (status === 'CANCELLED') return 'Отменено';
        if (status === 'ACTIVE') return 'Активно';
        return status;
    };

    // Получение класса CSS для статуса
    const getStatusClass = (status: string, completed: boolean) => {
        if (completed) return 'status-completed';
        if (status === 'EXPIRED') return 'status-expired';
        if (status === 'CANCELLED') return 'status-cancelled';
        if (status === 'ACTIVE') return 'status-active';
        return '';
    };

    // Проверка, доступны ли действия для бронирования
    const canPerformActions = (reservation: Reservation) => {
        return reservation.status === 'ACTIVE' && !reservation.completed;
    };

    // Очистка формы
    const handleClear = () => {
        setUserLogin('');
        setReservations([]);
        setError('');
        setSuccessMessage('');
    };

    return (
        <div className="user-reservations-management">
            <h1>Управление бронированиями пользователей</h1>

            {/* Панель поиска */}
            <div className="search-panel">
                <div className="search-input-group">
                    <label htmlFor="userLoginInput">Логин пользователя:</label>
                    <input
                        id="userLoginInput"
                        type="text"
                        value={userLogin}
                        onChange={(e) => setUserLogin(e.target.value)}
                        placeholder="Введите логин пользователя..."
                        className="search-input"
                        onKeyPress={(e) => e.key === 'Enter' && loadUserReservations()}
                    />
                    <button
                        onClick={loadUserReservations}
                        className="btn-primary"
                        disabled={loading || !userLogin.trim()}
                    >
                        {loading ? 'Поиск...' : 'Найти бронирования'}
                    </button>
                    {userLogin && (
                        <button
                            onClick={handleClear}
                            className="btn-secondary"
                        >
                            Очистить
                        </button>
                    )}
                </div>

                <p className="search-hint">
                    Для поиска бронирований введите логин пользователя (например: user123, admin, иванов)
                </p>
            </div>

            {/* Сообщения об ошибках/успехе */}
            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            {successMessage && !error && (
                <div className="success-message">
                    {successMessage}
                </div>
            )}

            {/* Список бронирований */}
            {reservations.length > 0 && (
                <div className="reservations-section">
                    <div className="section-header">
                        <h2>
                            Бронирования пользователя: <span className="username">{userLogin}</span>
                        </h2>
                        <span className="reservation-count">({reservations.length} шт.)</span>
                    </div>

                    <div className="reservations-list">
                        {reservations.map((reservation) => (
                            <div
                                key={reservation.id}
                                className={`reservation-card ${getStatusClass(reservation.status, reservation.completed)}`}
                            >
                                <div className="reservation-header">
                                    <div className="product-info">
                                        <h3>{reservation.productName}</h3>
                                        <span className="product-id">ID товара: {reservation.productId}</span>
                                    </div>

                                    <div className="status-info">
                                        <span className={`status-badge ${getStatusClass(reservation.status, reservation.completed)}`}>
                                            {getStatusText(reservation.status, reservation.completed)}
                                        </span>
                                        <span className="quantity">Количество: {reservation.quantity} шт.</span>
                                    </div>
                                </div>

                                <div className="reservation-details">
                                    <div className="detail-row">
                                        <span className="detail-label">Пользователь:</span>
                                        <span className="detail-value">
                                            {reservation.username} (ID: {reservation.userId})
                                        </span>
                                    </div>

                                    <div className="detail-row">
                                        <span className="detail-label">Дата бронирования:</span>
                                        <span className="detail-value">
                                            {formatDate(reservation.reservationDate)}
                                        </span>
                                    </div>

                                    <div className="detail-row">
                                        <span className="detail-label">Действует до:</span>
                                        <span className="detail-value">
                                            {formatDate(reservation.expiryDate)}
                                        </span>
                                    </div>

                                    <div className="detail-row">
                                        <span className="detail-label">ID бронирования:</span>
                                        <span className="detail-value reservation-id">
                                            #{reservation.id}
                                        </span>
                                    </div>
                                </div>

                                {/* Кнопки действий */}
                                {canPerformActions(reservation) && (
                                    <div className="reservation-actions">
                                        <button
                                            onClick={() => handleComplete(reservation.id)}
                                            className="btn-complete"
                                            title="Подтвердить выкуп товара"
                                        >
                                            <span className="icon">✓</span>
                                            <span>Подтвердить выкуп</span>
                                        </button>

                                        <button
                                            onClick={() => handleCancel(reservation.id)}
                                            className="btn-cancel"
                                            title="Отменить бронирование"
                                        >
                                            <span className="icon">✕</span>
                                            <span>Отменить бронь</span>
                                        </button>
                                    </div>
                                )}

                                {!canPerformActions(reservation) && (
                                    <div className="no-actions-message">
                                        <span className="icon">ⓘ</span>
                                        <span>Действия недоступны для этого статуса</span>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>

                    {/* Статистика */}
                    <div className="reservations-stats">
                        <div className="stat-item">
                            <span className="stat-label">Всего бронирований:</span>
                            <span className="stat-value">{reservations.length}</span>
                        </div>

                        <div className="stat-item">
                            <span className="stat-label">Активных:</span>
                            <span className="stat-value">
                                {reservations.filter(r => r.status === 'ACTIVE' && !r.completed).length}
                            </span>
                        </div>

                        <div className="stat-item">
                            <span className="stat-label">Выкупленных:</span>
                            <span className="stat-value">
                                {reservations.filter(r => r.completed).length}
                            </span>
                        </div>

                        <div className="stat-item">
                            <span className="stat-label">Просроченных:</span>
                            <span className="stat-value">
                                {reservations.filter(r => r.status === 'EXPIRED').length}
                            </span>
                        </div>
                    </div>
                </div>
            )}

            {/* Сообщение при отсутствии данных */}
            {!loading && reservations.length === 0 && userLogin && !error && !successMessage && (
                <div className="no-reservations">
                    <div className="empty-state">
                        <span className="empty-icon">📋</span>
                        <p>У пользователя "{userLogin}" нет бронирований</p>
                        <button
                            onClick={handleClear}
                            className="btn-secondary"
                        >
                            Попробовать другой логин
                        </button>
                    </div>
                </div>
            )}

            {/* Инструкция */}
            {!userLogin && (
                <div className="instructions">
                    <h3>Как использовать:</h3>
                    <ol>
                        <li>Введите логин пользователя в поле поиска выше</li>
                        <li>Нажмите "Найти бронирования" или клавишу Enter</li>
                        <li>Просмотрите список всех бронирований пользователя</li>
                        <li>Для <strong>активных бронирований</strong> доступны кнопки управления:</li>
                        <ul>
                            <li><span className="action-example complete">✓ Подтвердить выкуп</span> - товар помечается как выкупленный</li>
                            <li><span className="action-example cancel">✕ Отменить бронь</span> - бронирование отменяется, товар возвращается на склад</li>
                        </ul>
                    </ol>
                    <div className="status-explanation">
                        <h4>Обозначения статусов:</h4>
                        <div className="status-list">
                            <div className="status-item">
                                <span className="status-dot active"></span>
                                <span>Активно</span>
                            </div>
                            <div className="status-item">
                                <span className="status-dot completed"></span>
                                <span>Выкуплено</span>
                            </div>
                            <div className="status-item">
                                <span className="status-dot expired"></span>
                                <span>Просрочено</span>
                            </div>
                            <div className="status-item">
                                <span className="status-dot cancelled"></span>
                                <span>Отменено</span>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserReservationsManagement;