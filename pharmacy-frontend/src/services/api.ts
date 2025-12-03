import axios from 'axios';
import { authService } from './authService';

const API_BASE_URL = 'http://localhost:2222/api';

export const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Интерцептор для добавления токена
api.interceptors.request.use((config) => {
    const token = authService.getToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Интерцептор для обработки ошибок
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            const { status } = error.response;

            // Если токен просрочен или невалиден
            if (status === 401) {
                authService.logout();
                // Перенаправляем на страницу логина
                window.location.href = '/';
            }

            // Если доступ запрещен
            if (status === 403) {
                alert('У вас нет прав для выполнения этого действия');
            }
        }

        return Promise.reject(error);
    }
);