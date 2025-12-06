import React, { useState, useEffect } from 'react';
import { reservationService } from '../../services/reservationService';
import { Reservation } from '../../types';
import './MyReservations.css';

const MyReservations: React.FC = () => {
    const [reservations, setReservations] = useState<Reservation[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [expandedId, setExpandedId] = useState<number | null>(null);

    // Если id пользователя не сохранён — временно берём из localStorage или показываем ошибку

    useEffect(() => {
        if (loading){
            loadReservations();
            setLoading(false)
        }

    }, [loading]);

    const loadReservations = async () => {
        try {
            setLoading(true);
            const data = await reservationService.getUserReservations();
            setReservations(data);
        } catch (err: any) {
            setError(err.response?.data?.error || 'Ошибка загрузки бронирований');
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async (reservationId: number) => {
        if (!window.confirm('Вы уверены, что хотите отменить бронь?')) return;

        try {
            await reservationService.cancelReservation(reservationId);
            alert('Бронь успешно отменена');
            loadReservations(); // обновляем список
        } catch (err: any) {
            alert(err.response?.data?.error || 'Не удалось отменить бронь');
        }
    };

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleString('ru-RU');
    };

    const getStatusClass = (status: string) => {
        switch (status) {
            case 'ACTIVE': return 'status-active';
            case 'EXPIRED': return 'status-expired';
            case 'COMPLETED': return 'status-completed';
            default: return '';
        }
    };

    if (loading) return <div className="loading">Загрузка бронирований...</div>;
    if (error) return <div className="error-message">{error}</div>;

    return (
        <div className="my-reservations-page">
            <h1>Мои бронирования</h1>

            {reservations.length === 0 ? (
                <p className="no-reservations">У вас пока нет бронирований</p>
            ) : (
                <div className="reservations-list">
                    {reservations.map((res) => (
                        <div
                            key={res.id}
                            className={`reservation-card ${expandedId === res.id ? 'expanded' : ''}`}
                            onClick={() => setExpandedId(expandedId === res.id ? null : res.id)}
                        >
                            <div className="reservation-header">
                                <div>
                                    <strong>{res.productName}</strong>
                                    <span className={`status ${getStatusClass(res.status)}`}>
                    {res.status === 'ACTIVE' && 'Активно'}
                                        {res.status === 'EXPIRED' && 'Просрочено'}
                                        {res.status === 'COMPLETED' && 'Выкуплено'}
                                    </span>
                                </div>

                                <div className='reservation-info'>
                                    <span>Кол-во: {res.quantity} шт.</span>
                                    <span className="date">
                                        Забронировано: {formatDate(res.reservationDate)}
                                    </span>
                                </div>
                            </div>

                            {expandedId === res.id && (
                                <div className="reservation-details">
                                    <div className="detail-row">
                                        <span>Статус:</span>
                                        <strong className={getStatusClass(res.status)}>
                                            {res.status === 'ACTIVE' && 'Активно до ' + formatDate(res.expiryDate)}
                                            {res.status === 'EXPIRED' && 'Просрочено'}
                                            {res.status === 'COMPLETED' && 'Выкуплено'}
                                        </strong>
                                    </div>
                                    <div className="detail-row">
                                        <span>Дата бронирования:</span>
                                        <span>{formatDate(res.reservationDate)}</span>
                                    </div>
                                    <div className="detail-row">
                                        <span>Действует до:</span>
                                        <span>{formatDate(res.expiryDate)}</span>
                                    </div>

                                    {res.status === 'ACTIVE' && (
                                        <button
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleCancel(res.id);
                                            }}
                                            className="btn-cancel"
                                        >
                                            Отменить бронь
                                        </button>
                                    )}

                                    {res.status === 'EXPIRED' && (
                                        <p className="expired-note">
                                            Бронь просрочена — товар вернулся на склад.
                                        </p>
                                    )}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyReservations;