package com.pluralsight.NorthwindTradersSpringBoot;

import com.pluralsight.NorthwindTradersSpringBoot.dao.ProductDao;
import com.pluralsight.NorthwindTradersSpringBoot.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

// This is a Spring Bean that runs when the app starts.
// Because it implements CommandLineRunner, Spring Boot will call its run() method.
// We use this to run our "Film Admin Menu" in the console.

@Component
public class ProductApp implements CommandLineRunner {
    // since the only filmDao is a interface
    // and the only thing implementing filmDao is simpleFilmDao,
    //the Autowired is setting filmDao to simpleFilmDao

    // We are asking Spring to "inject" a FilmDao here.
    // This is called Dependency Injection — Spring will give us the correct FilmDao automatically.
    @Autowired
    @Qualifier("jdbcProductDao")
    private ProductDao productDao;

    // This is the main method that will run when the app starts.
    @Override
    public void run(String... args) throws Exception {
        // We create a Scanner object so we can read user input from the console.
        Scanner scanner = new Scanner(System.in);

        // This is a "loop" that will keep showing the menu until the user chooses to exit.
        while(true){

            System.out.println("\n=== Products Admin Menu ===");
            System.out.println("1. List Products");
            System.out.println("2. Add Products");
            System.out.println("3. Delete Products");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice){

                case "1":
                    //List all the Products

                    // Call the DAO to get a list of al the Products
                    List<Product> products = productDao.getAll();

                    // Print the products to the screen
                    System.out.println("\nProducts: " );
                    for (Product product : products) {
                        System.out.println(product); // for each product in product list, print each
                    }

                    break;

                case "2":

                    //Ask the user for the Product name
                    System.out.println("Enter the product name ");
                    String productName = scanner.nextLine();

                    System.out.println("Enter the category ID");
                    int categoryID = scanner.nextInt();

                    System.out.println("Enter the product price ");
                    double unitPrice  = Double.parseDouble(scanner.nextLine());

                    //Create a new Product object and set its data.
                    Product newProduct = new Product();
                    newProduct.setName(productName);
                    newProduct.setPrice(unitPrice);
                    newProduct.setCategoryId(categoryID);

                    productDao.add(newProduct);

                    System.out.println("Product Added successfully");

                    break;

                case "3":
                    //Delete Logic
                    break;

                case "4":
                    // The user chose option 4, Exit the program.

                    // Print a goodbye message.
                    System.out.println("Bye Bye!");

                    // End the program with a success status (0).
                    System.exit(0);
                    break;
                default:
                  // invalid user input
                 // Tell the user the input was invalid and show the menu again.
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
