package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Category;
import com.pluralsight.NorthwindTradersAPI.models.Product;

import java.util.List;

public interface ProductDao {
    //CRUD - Post(C), Get(R), Put(U), Delete(D)
    List<Product> getAll(); //Get
    Product add(Product product); // Post
    Product update(Product product); // Put
    Product delete(int id);

    Product findById(int id);
    Product findByName(String name);
    Product findByCatId(int catId);
    Product findByPrice(double price);
}
