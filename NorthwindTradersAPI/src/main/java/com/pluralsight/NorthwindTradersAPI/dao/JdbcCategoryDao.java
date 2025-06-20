package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Category;
import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCategoryDao {

    // This is the DataSource that we will use to connect to the database.
    // The DataSource is created in our DbConfiguration class.
    private DataSource dataSource;

    // This is a constructor.
    // Spring will automatically call this constructor and pass in the DataSource.
    // The @Autowired annotation tells Spring to "inject" the DataSource Bean here.
    @Autowired
    public JdbcCategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    //Create / Post / Add
    public Category add(Category category){
        String sql = "INSERT category (CategoryID, CategoryName) VALUES (?, ?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, category.getCategoryId());

            preparedStatement.setString(2,category.getCategoryName());

            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()){
                if (keys.next()){
                    int newId = keys.getInt(1);
                    category.setCategoryId(newId);
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return category;
    }

    @Override
    public List<Category> getAll() {
        // Create an empty list to hold the Film objects we will retrieve.
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT CategoryID, CategoryName FROM categories";

        // This is a "try-with-resources" block.
        // It ensures that the Connection, Statement, and ResultSet are closed automatically after we are done.
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement(); // change ot prepared
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Loop through each row in the ResultSet.
            while (resultSet.next()) {
                // Create a new Product object.
                Category category = new Category();

                category.setCategoryId(resultSet.getInt("CategoryID"));

                category.setCategoryName(resultSet.getString("CategoryName"));

                // Add the Product object to our list.
                categories.add(category);
            }

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            e.printStackTrace();
        }

        // Return the list of Product objects.
        return categories;
    }

    public Category update(Category category){
        String sql = "UPDATE categories SET CategoryID = ?, CategoryName = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1,category.getCategoryId());
            preparedStatement.setString(2, category.getCategoryName());

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0 ){
                return category;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return category;
       }

       public Category delete(int id){
          Category categoryToDelete = findById(id);

          if(categoryToDelete == null){
              return null;
          }

           String sql = "DELETE FROM products WHERE categoryID = ?";

           try(Connection connection = dataSource.getConnection();
               PreparedStatement preparedStatement = connection.prepareStatement(sql)){

               preparedStatement.setInt(1,id);
               preparedStatement.executeUpdate();

           } catch (SQLException e){
               e.printStackTrace();
               return null;
           }

           return categoryToDelete;

    }

    @Override
    public Product findById(int id) {
        Product product = new Product();

        String sql = "SELECT CategoryID, CategoryName FROM categories WHERE categoryID = ?";

        // This is a "try-with-resources" block.
        // It ensures that the Connection, Statement, and ResultSet are closed automatically after we are done.
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            {// change ot prepared

                preparedStatement.setInt(1,id);

                ResultSet resultSet = preparedStatement.executeQuery();


                // Loop through each row in the ResultSet.
                while (resultSet.next()) {
                    // Create a new Product object.

                    // Set the Product's ID from the "film_id" column.
                    product.setProductId(resultSet.getInt("ProductID"));

                    // Set the Product's title from the "title" column.
                    product.setName(resultSet.getString("ProductName"));

                    // Set the Product's rental rate from the "rental_rate" column.
                    product.setCategoryId(resultSet.getInt("CategoryID"));

                    product.setPrice(resultSet.getDouble("UnitPrice"));

                }
            }

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            e.printStackTrace();
        }

        return product;
    }

}
