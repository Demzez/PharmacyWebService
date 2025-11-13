import React, { useState, useEffect } from 'react';
import Navbar from './components/Common/NavBar';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import ProductList from './components/Pharmacy/ProductList';
import ProductManagement from './components/Admin/ProductManagement';
import SalesReports from './components/Admin/SalesReports';
import PopularProducts from './components/Admin/PopularProducts';
import { authService } from './services/authService';
import { User } from './types';
import './App.css';

// Экспортируем тип Page, чтобы использовать в других компонентах
export type Page = 'login' | 'register' | 'pharmacy' | 'admin-products' | 'sales-reports' | 'popular-products';

const App: React.FC = () => {
    const [currentPage, setCurrentPage] = useState<Page>('login');
    const [currentUser, setCurrentUser] = useState<User | null>(null);

    useEffect(() => {
        const user = authService.getCurrentUser();
        if (user) {
            setCurrentUser(user);
            setCurrentPage(user.role === 'ROLE_ADMIN' ? 'admin-products' : 'pharmacy');
        }
    }, []);

    const handleLogin = () => {
        const user = authService.getCurrentUser();
        setCurrentUser(user);
        setCurrentPage(user.role === 'ROLE_ADMIN' ? 'admin-products' : 'pharmacy');
    };

    const handleRegister = () => {
        setCurrentPage('login');
    };

    const handleLogout = () => {
        authService.logout();
        setCurrentUser(null);
        setCurrentPage('login');
    };

    const renderPage = () => {
        switch (currentPage) {
            case 'login':
                return <Login onLogin={handleLogin} onSwitchToRegister={() => setCurrentPage('register')} />;
            case 'register':
                return <Register onRegister={handleRegister} onSwitchToLogin={() => setCurrentPage('login')} />;
            case 'pharmacy':
                return <ProductList />;
            case 'admin-products':
                return <ProductManagement />;
            case 'sales-reports':
                return <SalesReports />;
            case 'popular-products':
                return <PopularProducts />;
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