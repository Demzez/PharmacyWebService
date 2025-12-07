import React, { useState, useEffect } from 'react';
import Navbar from './components/Common/NavBar';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import ProductList from './components/Pharmacy/ProductList';
import ProductManagement from './components/Admin/ProductManagement';
import UserReservationsManagement from './components/Admin/UserReservationsManagement';
import AdminReports from './components/Admin/AdminReports';
import MyReservations from './components/Pharmacy/MyReservations';
import { authService } from './services/authService';
import { User } from './types';
import './App.css';

export type Page = 'login' | 'register' | 'pharmacy' | 'my-reservations' | 'admin-products' | 'admin-reports' | 'user-reservations';;

const App: React.FC = () => {
    const [currentPage, setCurrentPage] = useState<Page>('login');
    const [currentUser, setCurrentUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Проверяем наличие валидного токена при загрузке
        const checkAuth = () => {
            setLoading(true);
            const user = authService.getCurrentUser();
            if (user && !authService.isTokenExpired()) {
                setCurrentUser(user);
                // Автоматический редирект на нужную страницу
                if (user.role === 'ROLE_ADMIN') {
                    setCurrentPage('admin-products');
                } else {
                    setCurrentPage('pharmacy');
                }
            } else {
                // Если токен просрочен, очищаем
                authService.logout();
                setCurrentPage('login');
            }
            setLoading(false);
        };

        checkAuth();

        // Можно добавить периодическую проверку токена
        const interval = setInterval(() => {
            if (authService.isTokenExpired()) {
                handleLogout();
            }
        }, 60000); // Проверяем каждую минуту

        return () => clearInterval(interval);
    }, []);

    const handleLogin = () => {
        const user = authService.getCurrentUser();
        if (user && !authService.isTokenExpired()) {
            setCurrentUser(user);
            if (user.role === 'ROLE_ADMIN') {
                setCurrentPage('admin-products');
            } else {
                setCurrentPage('pharmacy');
            }
        } else {
            // Если токен не валиден, остаемся на странице логина
            setCurrentPage('login');
        }
    };

    const handleRegister = () => {
        setCurrentPage('login');
    };

    const handleLogout = () => {
        authService.logout();
        setCurrentUser(null);
        setCurrentPage('login');
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Загрузка...</p>
            </div>
        );
    }

    // Защита маршрутов
    const renderPage = () => {
        // Если пользователь не авторизован и пытается получить доступ к защищенным страницам
        if (!currentUser && currentPage !== 'login' && currentPage !== 'register') {
            setCurrentPage('login');
            return null;
        }

        // Проверка прав администратора
        if (currentUser && currentUser.role !== 'ROLE_ADMIN' &&
            (currentPage === 'admin-products' || currentPage === 'admin-reports' || currentPage === 'user-reservations')) {
            // Если обычный пользователь пытается получить доступ к админским страницам
            alert('У вас нет прав для доступа к этой странице');
            setCurrentPage('pharmacy');
            return null;
        }

        switch (currentPage) {
            case 'login':
                return <Login onLogin={handleLogin} onSwitchToRegister={() => setCurrentPage('register')} />;
            case 'register':
                return <Register onRegister={handleRegister} onSwitchToLogin={() => setCurrentPage('login')} />;
            case 'pharmacy':
                return <ProductList />;
            case 'my-reservations':
                return <MyReservations />;
            case 'admin-products':
                return currentUser?.role === 'ROLE_ADMIN' ? <ProductManagement /> : null;
            case 'admin-reports':
                return currentUser?.role === 'ROLE_ADMIN' ? <AdminReports /> : null;
            case 'user-reservations':
                return currentUser?.role === 'ROLE_ADMIN' ? <UserReservationsManagement /> : null;
            default:
                return <Login onLogin={handleLogin} onSwitchToRegister={() => setCurrentPage('register')} />;
        }
    };

    return (
        <div className="App">
            {currentUser && (
                <Navbar
                    currentUser={currentUser}
                    onLogout={handleLogout}
                    onNavigate={setCurrentPage}
                />
            )}
            <main className="main-content">
                {renderPage()}
            </main>
        </div>
    );
};

export default App;