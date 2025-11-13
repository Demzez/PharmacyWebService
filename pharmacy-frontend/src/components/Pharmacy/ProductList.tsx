import React, { useState, useEffect } from 'react';
import { Product } from '../../types';
import { productService } from '../../services/productService';
import ProductCard from './ProductCard';
import SearchBar from './SearchBar';

const ProductList: React.FC = () => {
    const [products, setProducts] = useState<Product[]>([]);
    const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        try {
            setLoading(true);
            const data = await productService.getCatalog();
            setProducts(data);
            setFilteredProducts(data);
        } catch (error) {
            console.error('Error loading products:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async (query: string) => {
        if (!query.trim()) {
            setFilteredProducts(products);
            return;
        }

        try {
            const results = await productService.searchProducts(query);
            setFilteredProducts(results);
        } catch (error) {
            console.error('Search error:', error);
        }
    };

    if (loading) {
        return <div className="loading">Загрузка товаров...</div>;
    }

    return (
        <div className="pharmacy-page">
            <div className="page-header">
                <h1>Аптека - Каталог товаров</h1>
                <SearchBar onSearch={handleSearch} />
            </div>

            <div className="products-grid">
                {filteredProducts.map(product => (
                    <ProductCard
                        key={product.id}
                        product={product}
                        onReservationSuccess={loadProducts}
                    />
                ))}
            </div>

            {filteredProducts.length === 0 && (
                <div className="no-products">
                    Товары не найдены
                </div>
            )}
        </div>
    );
};

export default ProductList;