package com.example.productSalesServices;

import com.example.productSalesServices.entity.Product;
import com.example.productSalesServices.entity.Sale;
import com.example.productSalesServices.service.ProductService;
import com.example.productSalesServices.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ProductSalesServicesApplication implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    public static void main(String[] args) {
        SpringApplication.run(ProductSalesServicesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create sample Product objects
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(10.0);
        product1.setQuantity(100);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(20.0);
        product2.setQuantity(50);

        // Add products to the ProductService
        productService.addProduct(product1);
        productService.addProduct(product2);

        // Create sample Sale objects
        Sale sale1 = new Sale();
        sale1.setProduct(product1);
        sale1.setQuantity(5);

        Sale sale2 = new Sale();
        sale2.setProduct(product2);
        sale2.setQuantity(10);

        // Add sales to the SaleService
        saleService.addSales(sale1);
        saleService.addSales(sale2);

        // Use WebClient to make HTTP requests to REST API endpoints
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.AUTHORIZATION, createBasicAuthHeader())
                .build();

        // Get all products
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("pageNo", 1);
        queryParams.put("pageSize", 10);
        String allProductsResponse = webClient.get()
                .uri("/api/products/{pageNo}/{pageSize}", queryParams)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("All Products: " + allProductsResponse);

        // Get product by ID
        String productByIdResponse = webClient.get()
                .uri("/api/products/{id}", 1)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("Product by ID: " + productByIdResponse);

        // Add a new product
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setDescription("New Description");
        newProduct.setPrice(30.0);
        newProduct.setQuantity(20);
        Product addedProduct = webClient.post()
                .uri("/api/products/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newProduct))
                .retrieve()
                .bodyToMono(Product.class)
                .block();
        System.out.println("Added Product: " + addedProduct);

        // Update product
        product1.setName("Updated Product 1");
        Product updatedProduct = webClient.put()
                .uri("/api/products/update/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(product1))
                .retrieve()
                .bodyToMono(Product.class)
                .block();
        System.out.println("Updated Product: " + updatedProduct);

        // Delete product
        webClient.delete()
                .uri("/api/products/delete/{id}", 2)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        System.out.println("Product deleted successfully");

        // Calculate total revenue
        double totalRevenue = productService.getTotalRevenue();
        System.out.println("Total Revenue: " + totalRevenue);

        // Calculate revenue by product
        double revenueByProduct1 = productService.getRevenueByProduct(product1.getId());
        double revenueByProduct2 = productService.getRevenueByProduct(product2.getId());
        System.out.println("Revenue for Product 1: " + revenueByProduct1);
        System.out.println("Revenue for Product 2: " + revenueByProduct2);
    }

    private String createBasicAuthHeader() {
        String auth = "admin:admin@123";
        byte[] encodedAuth = Base64Utils.encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }
}
