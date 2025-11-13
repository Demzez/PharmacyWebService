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

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await authService.register(userData);
            onRegister();
        } catch (err: any) {
            setError(err.response?.data?.error || 'Registration failed');
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
                    />
                </div>
                <div className="form-group">
                    <label>Пароль:</label>
                    <input
                        type="password"
                        value={userData.password}
                        onChange={(e) => setUserData({...userData, password: e.target.value})}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input
                        type="email"
                        value={userData.email}
                        onChange={(e) => setUserData({...userData, email: e.target.value})}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Телефон:</label>
                    <input
                        type="tel"
                        value={userData.phone}
                        onChange={(e) => setUserData({...userData, phone: e.target.value})}
                    />
                </div>
                {error && <div className="error-message">{error}</div>}
                <button type="submit" className="btn-primary">Зарегистрироваться</button>
            </form>
            <p>
                Уже есть аккаунт?{' '}
                <button onClick={onSwitchToLogin} className="link-button">
                    Войти
                </button>
            </p>
        </div>
    );
};

export default Register;