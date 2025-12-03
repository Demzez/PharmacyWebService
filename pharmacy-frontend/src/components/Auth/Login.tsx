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
    const [isLoading, setIsLoading] = useState(false);
    // const navigate = useNavigate(); // Если используете React Router

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const response = await authService.login(credentials);

            // Успешный логин
            onLogin();

            // Если используете React Router:
            // navigate('/pharmacy');

        } catch (err: any) {
            // Обработка ошибок
            if (err.response?.status === 401) {
                setError('Неверное имя пользователя или пароль');
            } else if (err.response?.data?.error) {
                setError(err.response.data.error);
            } else if (err.message === 'Network Error') {
                setError('Сервер недоступен. Проверьте подключение.');
            } else {
                setError('Произошла ошибка при входе');
            }
            console.error('Login error:', err);
        } finally {
            setIsLoading(false);
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
                        disabled={isLoading}
                    />
                </div>
                <div className="form-group">
                    <label>Пароль:</label>
                    <input
                        type="password"
                        value={credentials.password}
                        onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                        required
                        disabled={isLoading}
                    />
                </div>
                {error && <div className="error-message">{error}</div>}
                <button
                    type="submit"
                    className="btn-primary"
                    disabled={isLoading}
                >
                    {isLoading ? 'Вход...' : 'Войти'}
                </button>
            </form>
            <p>
                Нет аккаунта?{' '}
                <button
                    onClick={onSwitchToRegister}
                    className="link-button"
                    disabled={isLoading}
                >
                    Зарегистрироваться
                </button>
            </p>
        </div>
    );
};

export default Login;