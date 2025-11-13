import React, { useState } from 'react';
import { authService } from '../../services/authService';

interface LoginProps {
    onLogin: () => void;
    onSwitchToRegister: () => void;
}

const Login: React.FC<LoginProps> = ({ onLogin, onSwitchToRegister }) => {
    const [credentials, setCredentials] = useState({
        username: '',
        password: ''
    });
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            // Убрали неиспользуемую переменную response
            await authService.login(credentials);
            // Временное решение - в реальном приложении здесь был бы JWT токен
            localStorage.setItem('authToken', 'temp-token');
            localStorage.setItem('currentUser', JSON.stringify({
                username: credentials.username,
                role: credentials.username === 'admin' ? 'ROLE_ADMIN' : 'ROLE_USER'
            }));
            onLogin();
        } catch (err: unknown) { // Исправили any на unknown
            if (err instanceof Error) {
                setError(err.message);
            } else {
                setError('Login failed');
            }
        }
    };

    return (
        <div className="auth-container">
            <h2>Вход в систему</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                <div className="form-group">
                    <label>Имя пользователя:</label>
                    <input
                        type="text"
                        value={credentials.username}
                        onChange={(e) => setCredentials({...credentials, username: e.target.value})}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Пароль:</label>
                    <input
                        type="password"
                        value={credentials.password}
                        onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                        required
                    />
                </div>
                {error && <div className="error-message">{error}</div>}
                <button type="submit" className="btn-primary">Войти</button>
            </form>
            <p>
                Нет аккаунта?{' '}
                <button onClick={onSwitchToRegister} className="link-button">
                    Зарегистрироваться
                </button>
            </p>
        </div>
    );
};

export default Login;