package com.pluralsight.NorthwindTradersAPI.controllers;

import com.pluralsight.NorthwindTradersAPI.dao.CategoryDao;
import com.pluralsight.NorthwindTradersAPI.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
   @Autowired
   private CategoryDao categoryDao;

   @GetMapping("/api/categories")
    public List<Category> getAll(){
       return categoryDao.getAll();
   }

   @GetMapping("/api/categories/{id}")
    public Category findById(int id){
       return categoryDao.findById(id);
    }


    @PostMapping("/api/categories")
    public Category add(@RequestBody Category category ){
        return categoryDao.add(category);
   }



    @PutMapping("/api/categories")
    public Category update(@RequestBody Category category){
        return categoryDao.update(category);
    }

    //
    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<Category> delete(@PathVariable int id){
        Category deleted = categoryDao.delete(id);

        if(deleted == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deleted);

    }

}
