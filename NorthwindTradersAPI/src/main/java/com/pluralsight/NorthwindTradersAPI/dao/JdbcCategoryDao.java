package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCategoryDao implements CategoryDao {

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
    @Override
    public Category add(Category category) {

        String sql = "INSERT INTO Categories (CategoryName) VALUES (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getCategoryName());

            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    category.setCategoryId(newId); // set the generated ID on the category object
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }


    // Read / Get
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
                // Create a new Category object.
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

    // Update
    public Category update(Category category) {
        String sql = "UPDATE categories SET CategoryID = ?, CategoryName = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, category.getCategoryId());
            preparedStatement.setString(2, category.getCategoryName());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return category;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }

    public Category delete(int id) {
        Category categoryToDelete = findById(id);

        if (categoryToDelete == null) {
            return null;
        }

        String sql = "DELETE FROM categories WHERE categoryID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return categoryToDelete;

    }

    public Category findById(int id) {
        Category category = null;

        String sql = "SELECT CategoryID, CategoryName FROM categories WHERE categoryID = ?";

        // This is a "try-with-resources" block.
        // It ensures that the Connection, Statement, and ResultSet are closed automatically after we are done.
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            {

                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();


                // Loop through each row in the ResultSet.
                while (resultSet.next()) {
                    // Create a new Product object.

                    // Set the Product's rental rate from the "rental_rate" column.
                    category.setCategoryId(resultSet.getInt("CategoryID"));

                    // Set the Product's title from the "title" column.
                    category.setCategoryName(resultSet.getString("CategoryName"));

                }
            }

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            e.printStackTrace();
        }

        return category;
    }

    @Override
    public Category findByName(String name) {
        Category category = null;

        String sql = "SELECT CategoryID, CategoryName FROM categories WHERE categoryName = ?";

        // This is a "try-with-resources" block.
        // It ensures that the Connection, Statement, and ResultSet are closed automatically after we are done.
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            {

                preparedStatement.setString(1, name);

                ResultSet resultSet = preparedStatement.executeQuery();


                // Loop through each row in the ResultSet.
                while (resultSet.next()) {
                    // Create a new Product object.

                    // Set the Product's rental rate from the "rental_rate" column.
                    category.setCategoryId(resultSet.getInt("CategoryID"));

                    // Set the Product's title from the "title" column.
                    category.setCategoryName(resultSet.getString("CategoryName"));

                }
            }

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            e.printStackTrace();
        }

        return category;
    }

}
