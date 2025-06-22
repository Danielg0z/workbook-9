package com.pluralsight.NorthwindTradersAPI.controllers;

import com.pluralsight.NorthwindTradersAPI.dao.ProductDao;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping("/api/products")
    public List<Product> getAll(){
        return productDao.getAll();
    }

    @GetMapping("/api/products/{id}")
    public Product findById(@PathVariable int id){
        return productDao.findById(id);
    }

    @PostMapping("/api/products")
    public Product add(@RequestBody Product product){
        return productDao.add(product);
    }

    //
    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Product> delete(@PathVariable int id){
        Product deleted = productDao.delete(id);

        if(deleted == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deleted);

    }

}

