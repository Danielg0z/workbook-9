package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Category;

import java.util.List;

public interface CategoryDao {
    //CRUD - Post(C), Get(R), Put(U), Delete(D)
    List<Category> getAll(); //Get - Create
    Category add(Category category); // Post
    Category update(Category category); // Put
    Category delete(int id); // Delete

    Category findById(int id);
    Category findByName(String name);
}
