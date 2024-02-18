package com.example.productSalesServices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productSalesServices.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
