package com.pluralsight.NorthwindTradersSpringBoot.dao;

import com.pluralsight.NorthwindTradersSpringBoot.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// This is a Spring Component.
// The @Component annotation tells Spring to create an instance of this class and manage it as a Bean.
// This allows us to inject it into other classes (like FilmApp).
@Component
public class JdbcProductDao implements ProductDao {

    // This is the DataSource that we will use to connect to the database.
    // The DataSource is created in our DbConfiguration class.
    private DataSource dataSource;

    // This is a constructor.
    // Spring will automatically call this constructor and pass in the DataSource.
    // The @Autowired annotation tells Spring to "inject" the DataSource Bean here.
    @Autowired
    public JdbcProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(Product product) {

        // This is the SQL INSERT statement we will run.
        // We are inserting the film title, rental rate, and language_id.
        String sql = "INSERT INTO product (ProductID, ProductName, CategoryID, UnitPrice) VALUES (?, ?, ?)";


        try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement  = connection.prepareStatement(sql)) {


            preparedStatement.setInt(1,product.getProductId());


            preparedStatement.setString(2, product.getName());


            preparedStatement.setInt(3, product.getCategoryId());


            preparedStatement.setDouble(4, product.getPrice());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            // If something goes wrong (SQL error), print the stack trace to help debug.
            e.printStackTrace();
        }

    }

    // This method will return a list of all Films from the database.
    // It is required because we are implementing the FilmDao interface.
    @Override
    public List<Product> getAll() {
        // Create an empty list to hold the Film objects we will retrieve.
        List<Product> products = new ArrayList<>();

        String sql = "SELECT ProductID, ProductName, CategoryID, UnitPrice FROM products";

        // This is a "try-with-resources" block.
        // It ensures that the Connection, Statement, and ResultSet are closed automatically after we are done.
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement(); // change ot prepared
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Loop through each row in the ResultSet.
            while (resultSet.next()) {
                // Create a new Product object.
                Product product = new Product();

                // Set the Product's ID from the "film_id" column.
                product.setProductId(resultSet.getInt("ProductID"));

                // Set the Product's title from the "title" column.
                product.setName(resultSet.getString("ProductName"));

                // Set the Product's rental rate from the "rental_rate" column.
                product.setCategoryId(resultSet.getInt("CategoryID"));

                product.setPrice(resultSet.getDouble("UnitPrice"));

                // Add the Product object to our list.
                products.add(product);
            }

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            e.printStackTrace();
        }

        // Return the list of Product objects.
        return products;
    }

}
