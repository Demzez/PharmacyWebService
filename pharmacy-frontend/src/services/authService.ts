import { api } from './api';
import { LoginRequest, RegisterRequest, User } from '../types';

export const authService = {
    async login(credentials: LoginRequest) {
        const response = await api.post('/auth/login', credentials);
        const { token, username, role } = response.data;

        localStorage.setItem('authToken', token);
        localStorage.setItem('currentUser', JSON.stringify({
            username,
            role,
            token
        }));

        return response.data;
    },

    async register(userData: RegisterRequest) {
        const response = await api.post('/auth/register', userData);
        return response.data;
    },

    logout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('currentUser');
    },

    isAuthenticated(): boolean {
        const token = localStorage.getItem('authToken');
        return !!token;
    },

    getCurrentUser(): User | null {
        const userStr = localStorage.getItem('currentUser');
        if (!userStr) return null;

        try {
            const user = JSON.parse(userStr);
            // Добавляем проверку срока действия токена (опционально)
            if (this.isTokenExpired()) {
                this.logout();
                return null;
            }
            return user;
        } catch (error) {
            this.logout();
            return null;
        }
    },

    getToken(): string | null {
        return localStorage.getItem('authToken');
    },

    // Простая проверка истечения токена (без полной валидации)
    isTokenExpired(): boolean {
        const token = this.getToken();
        if (!token) return true;

        try {
            // Парсим JWT токен (без проверки подписи на клиенте)
            const payload = JSON.parse(atob(token.split('.')[1]));
            const exp = payload.exp * 1000; // Конвертируем в миллисекунды
            return Date.now() > exp;
        } catch (error) {
            return true;
        }
    },

    // Обновление информации о пользователе (если нужно)
    updateUserInfo(userInfo: Partial<User>) {
        const currentUser = this.getCurrentUser();
        if (currentUser) {
            const updatedUser = { ...currentUser, ...userInfo };
            localStorage.setItem('currentUser', JSON.stringify(updatedUser));
        }
    }
};