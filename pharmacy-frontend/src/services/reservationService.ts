import { api } from './api';
import { Reservation, ReservationRequest } from '../types';
import { authService } from './authService';

export const reservationService = {
    async createReservation(productId: number, quantity: number) {
        // Получаем ID пользователя из текущей сессии
        const user = authService.getCurrentUser();
        if (!user) {
            throw new Error('Пользователь не авторизован');
        }

        const request: ReservationRequest = {
            productId,
            quantity
        };

        // Теперь не передаем userId как параметр, он берется из токена
        const response = await api.post('/reservations/create', request);
        return response.data;
    },

    async getUserReservations(): Promise<Reservation[]> {
        // Используем эндпоинт без указания userId в URL
        const response = await api.get('/reservations/user');
        return response.data;
    },

    async getActiveUserReservations(): Promise<Reservation[]> {
        const response = await api.get('/reservations/user/active');
        return response.data;
    },

    async cancelReservation(reservationId: number) {
        const response = await api.delete(`/reservations/${reservationId}/cancel`);
        return response.data;
    },

    async completeReservation(reservationId: number) {
        const response = await api.put(`/reservations/${reservationId}/complete`);
        return response.data;
    }
};