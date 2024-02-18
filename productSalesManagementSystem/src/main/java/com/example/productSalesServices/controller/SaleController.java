package com.example.productSalesServices.controller;

import com.example.productSalesServices.entity.Sale;
import com.example.productSalesServices.exception.SaleNotFoundException;
import com.example.productSalesServices.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @GetMapping("/{pageNo}/{pageSize}")
    public ResponseEntity<Map<Object, Object>> getAllSales(@PathVariable int pageNo, @PathVariable int pageSize) {
        return new ResponseEntity<>(saleService.getAllSales(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.getSalesById(id);
        if (sale != null) {
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } else {
            throw new SaleNotFoundException("Sale not found with id: " + id);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Sale> addSale(@RequestBody Sale sale) {
        Sale newSale = saleService.addSales(sale);
        return new ResponseEntity<>(newSale, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        Sale updatedSale = saleService.updateSales(id, sale);
        return new ResponseEntity<>(updatedSale, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSales(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}