package com.example.productSalesServices.service;

import org.springframework.data.domain.Page;

import com.example.productSalesServices.entity.Product;

import java.util.Map;

public interface ProductService {

	Map<Object,Object> getAllProducts(int page, int size);

	Product getProductById(Long id);

	Product addProduct(Product product);

	Product updateProduct(Long id, Product product);

	void deleteProduct(Long id);

	double getTotalRevenue();

	double getRevenueByProduct(Long productId);
}
