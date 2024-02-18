package com.example.productSalesServices.service;

import com.example.productSalesServices.entity.Sale;

import java.util.Map;

public interface SaleService {

    Map<Object, Object> getAllSales(int page, int size);

    Sale getSalesById(Long id);

    Sale addSales(Sale sale);

    Sale updateSales(Long id, Sale sale);

    void deleteSales(Long id);
}
