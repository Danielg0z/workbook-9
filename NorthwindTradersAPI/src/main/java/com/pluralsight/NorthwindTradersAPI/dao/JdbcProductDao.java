package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Product;
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

    public Product add(Product product) {

        // This is the SQL INSERT statement we will run.
        // We are inserting the film title, rental rate, and language_id.
        String sql = "INSERT INTO products (ProductName, CategoryID, UnitPrice) VALUES (?, ?, ?)";


        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement  = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());

            preparedStatement.setInt(2, product.getCategoryId());

            preparedStatement.setDouble(3, product.getPrice());

            preparedStatement.executeUpdate();

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    product.setProductId(newId); // Set the generated ID on the Film object
                }

            }

        } catch (SQLException e){
            // If something goes wrong (SQL error), print the stack trace to help debug.
            e.printStackTrace();
        }

        return product;

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

    @Override
    public Product update(Product product) {
        String sql = "UPDATE products SET ProductName = ?, CategoryID = ?, UnitPrice = ?";

        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getCategoryId());
            preparedStatement.setDouble(3, product.getPrice());


            int rowsAffected  = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                return product;
            }
        } catch(SQLException e){
            System.out.println("Update failed: no product with ID" + product.getProductId());
        }

        return product;
    }

    @Override
    public Product delete(int id) {
        Product productToDelete  = findById(id);

        if(productToDelete == null){
            return  null;
        }

        String sql = "DELETE FROM products WHERE ProductID = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return productToDelete;
    }



    @Override
    public Product findById(int id) {
        Product product = new Product();

        String sql = "SELECT ProductID, ProductName, CategoryID, UnitPrice FROM products WHERE ProductID = ?";

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

    @Override
    public Product findByName(String name) {
        Product product = new Product();

        String sql = "SELECT ProductID, ProductName, CategoryID, UnitPrice FROM products WHERE ProductName = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            {// change ot prepared

                preparedStatement.setString(1, name);

                ResultSet resultSet = preparedStatement.executeQuery();

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

    @Override
    public Product findByCatId(int catId) {
        Product product = new Product();

        String sql = "SELECT ProductID, ProductName, CategoryID, UnitPrice FROM products WHERE CategoryID = ?";

        // This is a "try-with-resources" block.
        // It ensures that the Connection, Statement, and ResultSet are closed automatically after we are done.
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            {// change ot prepared

                preparedStatement.setInt(1, catId);

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

    @Override
    public Product findByPrice(double price) {
        Product product = new Product();

        String sql = "SELECT ProductID, ProductName, CategoryID, UnitPrice FROM products WHERE ProductID = ?";

        // This is a "try-with-resources" block.
        // It ensures that the Connection, Statement, and ResultSet are closed automatically after we are done.
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            {// change ot prepared

                preparedStatement.setDouble(1, price);

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

        return product; }

    }
