export interface User {
    id: number;
    username: string;
    email: string;
    phone: string;
    role: 'ROLE_ADMIN' | 'ROLE_USER';
    active: boolean;
    createdAt: string;
}

export interface Product {
    id: number;
    name: string;
    manufacturer: string;
    releaseForm: string;
    expiryDate: string;
    prescriptionStatus: 'PRESCRIPTION' | 'NON_PRESCRIPTION';
    price: number;
    stockQuantity: number;
    activeSubstance: string;
    category: string;
    available: boolean;
}

export interface Reservation {
    id: number;
    userId: number;
    username: string;
    productId: number;
    productName: string;
    quantity: number;
    reservationDate: string;
    expiryDate: string;
    completed: boolean;
    status: string;
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    password: string;
    email: string;
    phone: string;
}