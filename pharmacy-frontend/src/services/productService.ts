import { api } from './api';
import { Product } from '../types';

export const productService = {
    async getPublicCatalog(): Promise<Product[]> {
        const response = await api.get('/products/catalog');
        return response.data;
    },

    async searchProducts(query: string): Promise<Product[]> {
        const response = await api.get(`/products/search?query=${encodeURIComponent(query)}`);
        return response.data;
    },

    async getProduct(id: number): Promise<Product> {
        const response = await api.get(`/products/${id}`);
        return response.data;
    },

    async getAnalogs(productId: number) {
        const response = await api.get(`/products/${productId}/analogs`);
        return response.data;
    },

    async getAvailableProducts(): Promise<Product[]> {
        const response = await api.get('/products/available');
        return response.data;
    },

    async getPopularProducts(): Promise<Product[]> {
        const response = await api.get('/products/popular');
        return response.data;
    }
};

export const adminService = {

    async getCatalog(): Promise<Product[]> {
        const response = await api.get('/admin/products/all_catalog');
        return response.data;
    },

    async adminSearchProducts(query: string): Promise<Product[]> {
        const response = await api.get(`/admin/products/search?query=${encodeURIComponent(query)}`);
        return response.data;
    },

    async createProduct(productData: any) {
        const response = await api.post('/admin/products/create', productData);
        return response.data;
    },

    async updateProduct(id: number, productData: any) {
        const response = await api.put(`/admin/products/${id}`, productData);
        return response.data;
    },

    async toggleProductVisibility(id: number) {
        const response = await api.put(`/admin/products/${id}/visibility`);
        return response.data;
    },

    async getSalesReport(startDate: string, endDate: string) {
        const response = await api.get('/admin/reports/sales', {
            params: { startDate, endDate }
        });
        return response.data;
    },

    async getPopularProducts() {
        const response = await api.get('/admin/reports/popular');
        return response.data;
    },

    async getSystemStatistics() {
        const response = await api.get('/admin/statistics');
        return response.data;
    }
};