package com.example.productSalesServices.serviceImpl;

import com.example.productSalesServices.entity.Sale;
import com.example.productSalesServices.repository.SaleRepository;
import com.example.productSalesServices.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;


    @Override
    public Map<Object, Object> getAllSales(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Sale> salesList = saleRepository.findAll(pageable);
        Map<Object, Object> salesMap = new HashMap<>();
        salesMap.put("totalElements", salesList.getTotalElements());
        salesMap.put("totalPages", salesList.getTotalPages());
        salesMap.put("numberOfElements", salesList.getNumberOfElements());
        salesMap.put("currentPage", salesList.getNumber() + 1);
        if (!CollectionUtils.isEmpty(salesList.getContent())) {
            salesMap.put("sales", salesList.getContent());
        } else {
            salesMap.put("sales", "No sales present!!!!");
        }
        return salesMap;
    }

    @Override
    public Sale getSalesById(Long id) {
        return saleRepository.findById(id).orElse(null);
    }

    @Override
    public Sale addSales(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public Sale updateSales(Long id, Sale sale) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with id: " + id));

        // Update the existing sale with the new data
        existingSale.setQuantity(sale.getQuantity());
        existingSale.setSaleDate(sale.getSaleDate());

        // Save and return the updated sale
        return saleRepository.save(existingSale);
    }

    @Override
    public void deleteSales(Long id) {
        saleRepository.deleteById(id);
    }
}
