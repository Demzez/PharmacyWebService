import React from 'react';
import {Page} from '../../App'

// Определяем тип для пользователя
interface User {
    username: string;
    role: 'ROLE_ADMIN' | 'ROLE_USER';
}

interface NavbarProps {
    currentUser: User | null;
    onLogout: () => void;
    onNavigate: (page: Page) => void;
}

const Navbar: React.FC<NavbarProps> = ({ currentUser, onLogout, onNavigate }) => {
    const isAdmin = currentUser?.role === 'ROLE_ADMIN';

    return (
        <nav className="navbar">
            <div className="nav-brand">
                <h2>💊zezWorld</h2>
            </div>

            <div className="nav-links">
                {currentUser ? (
                    <>
                        <button onClick={() => onNavigate('pharmacy')} className="nav-link">
                            Каталог
                        </button>
                        {!isAdmin && (
                            <button onClick={() => onNavigate('my-reservations')} className="nav-link">
                                Мои бронирования
                            </button>
                        )}

                        {isAdmin && (
                            <>
                                <button onClick={() => onNavigate('admin-products')} className="nav-link">
                                    Управление товарами
                                </button>
                                <button onClick={() => onNavigate('sales-reports')} className="nav-link">
                                    Отчеты по продажам
                                </button>
                                <button onClick={() => onNavigate('popular-products')} className="nav-link">
                                    Популярные товары
                                </button>
                            </>
                        )}

                        <span className="user-info">Добро пожаловать, {currentUser.username}!</span>
                        <button onClick={onLogout} className="btn-logout">
                            Выйти
                        </button>
                    </>
                ) : (
                    <div className="auth-links">
                        <button onClick={() => onNavigate('login')} className="nav-link">
                            Вход
                        </button>
                        <button onClick={() => onNavigate('register')} className="nav-link">
                            Регистрация
                        </button>
                    </div>
                )}
            </div>
        </nav>
    );
};

export default Navbar;