import React, { useState } from 'react';
import { authService } from '../../services/authService';

interface RegisterProps {
    onRegister: () => void;
    onSwitchToLogin: () => void;
}

const Register: React.FC<RegisterProps> = ({ onRegister, onSwitchToLogin }) => {
    const [userData, setUserData] = useState({
        username: '',
        password: '',
        email: '',
        phone: ''
    });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const response = await authService.register(userData);

            // Регистрация успешна, переключаем на логин
            alert('Регистрация успешна! Теперь вы можете войти в систему.');
            onRegister();

        } catch (err: any) {
            if (err.response?.status === 400) {
                setError(err.response.data.error || 'Некорректные данные');
            } else if (err.response?.data?.error) {
                setError(err.response.data.error);
            } else if (err.message === 'Network Error') {
                setError('Сервер недоступен. Проверьте подключение.');
            } else {
                setError('Произошла ошибка при регистрации');
            }
            console.error('Registration error:', err);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <h2>Регистрация</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                <div className="form-group">
                    <label>Имя пользователя:</label>
                    <input
                        type="text"
                        value={userData.username}
                        onChange={(e) => setUserData({...userData, username: e.target.value})}
                        required
                        disabled={isLoading}
                        minLength={3}
                    />
                </div>
                <div className="form-group">
                    <label>Пароль:</label>
                    <input
                        type="password"
                        value={userData.password}
                        onChange={(e) => setUserData({...userData, password: e.target.value})}
                        required
                        disabled={isLoading}
                        minLength={6}
                    />
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input
                        type="email"
                        value={userData.email}
                        onChange={(e) => setUserData({...userData, email: e.target.value})}
                        required
                        disabled={isLoading}
                    />
                </div>
                <div className="form-group">
                    <label>Телефон:</label>
                    <input
                        type="tel"
                        value={userData.phone}
                        onChange={(e) => setUserData({...userData, phone: e.target.value})}
                        disabled={isLoading}
                    />
                </div>
                {error && <div className="error-message">{error}</div>}
                <button
                    type="submit"
                    className="btn-primary"
                    disabled={isLoading}
                >
                    {isLoading ? 'Регистрация...' : 'Зарегистрироваться'}
                </button>
            </form>
            <p>
                Уже есть аккаунт?{' '}
                <button
                    onClick={onSwitchToLogin}
                    className="link-button"
                    disabled={isLoading}
                >
                    Войти
                </button>
            </p>
        </div>
    );
};

export default Register;