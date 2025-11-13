import { api } from './api';
import { Reservation } from '../types';

export const reservationService = {
    async createReservation(productId: number, quantity: number, userId: number) {
        const response = await api.post('/reservations', {
            productId,
            quantity
        }, {
            params: { userId }
        });
        return response.data;
    },

    async getUserReservations(userId: number): Promise<Reservation[]> {
        const response = await api.get(`/reservations/user/${userId}`);
        return response.data;
    },

    async getActiveUserReservations(userId: number): Promise<Reservation[]> {
        const response = await api.get(`/reservations/user/${userId}/active`);
        return response.data;
    },

    async cancelReservation(reservationId: number) {
        const response = await api.delete(`/reservations/${reservationId}`);
        return response.data;
    },

    async completeReservation(reservationId: number) {
        const response = await api.put(`/reservations/${reservationId}/complete`);
        return response.data;
    }
};