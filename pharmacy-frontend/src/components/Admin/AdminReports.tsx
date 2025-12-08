import React, { useState, useEffect } from 'react';
import { adminService } from '../../services/productService';
import './AdminReports.css';
import { BasicProductInfo, BasicStatistics } from '../../types';

const AdminReports: React.FC = () => {
    const [popularProducts, setPopularProducts] = useState<BasicProductInfo[]>([]);
    const [statistics, setStatistics] = useState<BasicStatistics | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string>('');

    const demoData = {
        popularProducts: [
            { id: 1, name: 'Нурофен', manufacturer: 'Reckitt Benckiser', salesCount: 45 },
            { id: 2, name: 'Парацетамол', manufacturer: 'Фармстандарт', salesCount: 38 },
            { id: 3, name: 'Амоксиклав', manufacturer: 'Lek d.d.', salesCount: 32 },
            { id: 4, name: 'Кларитин', manufacturer: 'Bayer', salesCount: 28 },
            { id: 5, name: 'Эналаприл', manufacturer: 'Гедеон Рихтер', salesCount: 25 }
        ],
        statistics: {
            totalUsers: 234,
            totalProducts: 156,
            activeReservations: 42,
            totalRevenue: 124560.75
        }
    };

    useEffect(() => {
        loadReports();
    }, []);

    const loadReports = async () => {
        setLoading(true);
        setError('');

        try {
            const [productsData, statsData] = await Promise.allSettled([
                adminService.getPopularProducts(),
                adminService.getSystemStatistics()
            ]);

            let hasRealData = false;

            if (productsData.status === 'fulfilled') {
                const data = productsData.value as any[];
                if (Array.isArray(data) && data.length > 0) {
                    const formattedProducts = data.slice(0, 5).map(item => ({
                        id: item.productId || item.id || 0,
                        name: item.productName || item.name || 'Неизвестный товар',
                        manufacturer: item.manufacturer || 'Неизвестный производитель',
                        salesCount: item.totalSales || item.salesCount || 0
                    }));
                    setPopularProducts(formattedProducts);
                    hasRealData = true;
                }
            }

            if (statsData.status === 'fulfilled') {
                const data = statsData.value as any;
                if (data && typeof data === 'object') {
                    setStatistics({
                        totalUsers: data.totalUsers || 0,
                        totalProducts: data.totalProducts || 0,
                        activeReservations: data.activeReservations || 0,
                        totalRevenue: data.totalRevenue || 0
                    });
                    hasRealData = true;
                }
            }

            if (!hasRealData) {
                setPopularProducts(demoData.popularProducts);
                setStatistics(demoData.statistics);
            }

        } catch (err: any) {
            console.error('Ошибка загрузки отчетов:', err);
            setPopularProducts(demoData.popularProducts);
            setStatistics(demoData.statistics);
            setError('Не удалось загрузить данные с сервера. Отображаются демо-данные.');
        } finally {
            setLoading(false);
        }
    };

    const formatCurrency = (amount: number) => {
        return new Intl.NumberFormat('ru-RU', {
            style: 'currency',
            currency: 'BYN',
            minimumFractionDigits: 2
        }).format(amount);
    };

    if (loading) {
        return (
            <div className="admin-reports">
                <div className="reports-header">
                    <h1>Панель управления</h1>
                    <p className="header-subtitle">Аналитика и статистика системы</p>
                </div>
                <div className="loading-container">
                    <div className="loading-spinner"></div>
                    <p>Загрузка данных...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="admin-reports">
            {/* Заголовок */}
            <div className="reports-header">
                <h1>Панель управления</h1>
                <p className="header-subtitle">Обзор статистики системы и популярных товаров</p>
            </div>

            {/* Сообщение об ошибке */}
            {error && (
                <div className="demo-notice">
                    <span className="notice-icon">ℹ️</span>
                    <p>{error}</p>
                </div>
            )}

            {/* Основные метрики в виде сетки */}
            <div className="metrics-grid">
                <div className="metric-card revenue">
                    <div className="metric-icon">💰</div>
                    <div className="metric-info">
                        <h3>Общая выручка</h3>
                        <p className="metric-value">
                            {statistics ? formatCurrency(statistics.totalRevenue) : '—'}
                        </p>
                        <p className="metric-trend">+12.5% за месяц</p>
                    </div>
                </div>

                <div className="metric-card users">
                    <div className="metric-icon">👥</div>
                    <div className="metric-info">
                        <h3>Пользователи</h3>
                        <p className="metric-value">
                            {statistics ? statistics.totalUsers.toLocaleString() : '—'}
                        </p>
                        <p className="metric-trend">+{statistics ? Math.floor(statistics.totalUsers * 0.15) : 0} новых</p>
                    </div>
                </div>

                <div className="metric-card products">
                    <div className="metric-icon">📦</div>
                    <div className="metric-info">
                        <h3>Товары в каталоге</h3>
                        <p className="metric-value">
                            {statistics ? statistics.totalProducts : '—'}
                        </p>
                        <p className="metric-trend">{statistics ? Math.floor(statistics.totalProducts * 0.7) : 0} на складе</p>
                    </div>
                </div>

                <div className="metric-card reservations">
                    <div className="metric-icon">⏳</div>
                    <div className="metric-info">
                        <h3>Активные брони</h3>
                        <p className="metric-value">
                            {statistics ? statistics.activeReservations : '—'}
                        </p>
                        <p className="metric-trend">{statistics ? Math.floor(statistics.activeReservations * 0.8) : 0} ожидают</p>
                    </div>
                </div>
            </div>

            {/* Контент в одну колонку */}
            <div className="main-content">
                {/* Популярные товары */}
                <div className="content-card">
                    <div className="card-header">
                        <h2>Топ популярных товаров</h2>
                        <div className="card-actions">
                            <span className="badge">Топ 5</span>
                            <span className="period">Это хорошие товары!</span>
                        </div>
                    </div>

                    <div className="card-body">
                        {popularProducts.length > 0 ? (
                            <>
                                <div className="products-table">
                                    <div className="table-header">
                                        <div className="col-rank">#</div>
                                        <div className="col-name">Название товара</div>
                                        <div className="col-manufacturer">Производитель</div>
                                        <div className="col-sales">Продажи</div>
                                    </div>

                                    <div className="table-body">
                                        {popularProducts.map((product, index) => (
                                            <div key={product.id} className="table-row">
                                                <div className="col-rank">
                                                    <span className={`rank-badge rank-${index + 1}`}>
                                                        {index + 1}
                                                    </span>
                                                </div>
                                                <div className="col-name">
                                                    <span className="product-name">{product.name}</span>
                                                </div>
                                                <div className="col-manufacturer">
                                                    <span className="manufacturer">{product.manufacturer}</span>
                                                </div>
                                                <div className="col-sales">
                                                    <div className="sales-info">
                                                        <span className="sales-count">{product.salesCount}</span>
                                                        <div className="sales-bar">
                                                            <div
                                                                className="bar-fill"
                                                                style={{
                                                                    width: `${(product.salesCount / Math.max(...popularProducts.map(p => p.salesCount))) * 100}%`
                                                                }}
                                                            ></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>

                                <div className="products-summary">
                                    <div className="summary-item">
                                        <span className="label">Всего продаж:</span>
                                        <span className="value">
                                            {popularProducts.reduce((sum, product) => sum + product.salesCount, 0).toLocaleString()}
                                        </span>
                                    </div>
                                    <div className="summary-item">
                                        <span className="label">Топ производитель:</span>
                                        <span className="value">
                                            {popularProducts.reduce((max, product) =>
                                                product.salesCount > max.salesCount ? product : max
                                            ).manufacturer}
                                        </span>
                                    </div>
                                </div>
                            </>
                        ) : (
                            <div className="no-data">
                                <p>Нет данных по популярным товарам</p>
                            </div>
                        )}
                    </div>
                </div>

                {/* Футер с информацией */}
                <div className="footer-card">
                    <div className="footer-grid">
                        <div className="footer-section">
                            <h3>🏥 О системе</h3>
                            <p>PharmaWeb Service — современная система управления аптечным бизнесом,
                                предоставляющая полный контроль над товарами, продажами и клиентами.</p>
                            <div className="system-info">
                                <span className="info-item">
                                    <span className="label">Версия:</span>
                                    <span className="value">2.1.4</span>
                                </span>
                                <span className="info-item">
                                    <span className="label">Статус:</span>
                                    <span className="status active">
                                        <span className="status-dot"></span>
                                        Активна
                                    </span>
                                </span>
                            </div>
                        </div>

                        <div className="footer-section">
                            <h3>🎯 Бизнес-цели</h3>
                            <ul className="goals-list">
                                <li>
                                    <span className="goal-icon">📈</span>
                                    <span>Увеличение продаж на 20% ежеквартально</span>
                                </li>
                                <li>
                                    <span className="goal-icon">👥</span>
                                    <span>Привлечение 222 новых клиентов в месяц</span>
                                </li>
                                <li>
                                    <span className="goal-icon">⭐</span>
                                    <span>Достижение 95% удовлетворенности клиентов</span>
                                </li>
                                <li>
                                    <span className="goal-icon">⚡</span>
                                    <span>Оптимизация процессов управления складом</span>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div className="footer-bottom">
                        <p>© 2024 PharmaWeb Service. Все права защищены.</p>
                        <p className="foo">
                            Данные обновляются ежедневно. Для получения актуальной информации перезагрузите страницу.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminReports;