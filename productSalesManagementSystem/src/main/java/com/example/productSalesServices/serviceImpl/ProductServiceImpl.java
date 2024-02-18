package com.example.productSalesServices.serviceImpl;

import com.example.productSalesServices.entity.Sale;
import com.example.productSalesServices.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.productSalesServices.entity.Product;
import com.example.productSalesServices.repository.ProductRepository;
import com.example.productSalesServices.service.ProductService;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Override
	public Map<Object, Object> getAllProducts(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, 10);
		Page<Product> productList = productRepository.findAll(pageable);
		Map<Object, Object> productsMap = new HashMap<>();
		productsMap.put("totalElements", productList.getTotalElements());
		productsMap.put("totalPages", productList.getTotalPages());
		productsMap.put("numberOfElements", productList.getNumberOfElements());
		productsMap.put("currentPage", productList.getNumber() + 1);
		if (!CollectionUtils.isEmpty(productList.getContent())) {
			productsMap.put("products", productList.getContent());
		} else {
			productsMap.put("products", "No products present!!!!");
		}
		return productsMap;
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Long id, Product product) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

		// Update the existing product with the new data
		existingProduct.setName(product.getName());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setQuantity(product.getQuantity());

		// Save and return the updated product
		return productRepository.save(existingProduct);
	}

	@Override
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public double getTotalRevenue() {
		List<Sale> sales = saleRepository.findAll();
		return sales.stream()
				.mapToDouble(sale -> sale.getProduct().getPrice() * sale.getQuantity())
				.sum();
	}

	@Override
	public double getRevenueByProduct(Long productId) {
		List<Sale> sales = saleRepository.findByProductId(productId);
		return sales.stream()
				.mapToDouble(sale -> sale.getProduct().getPrice() * sale.getQuantity())
				.sum();
	}

}
