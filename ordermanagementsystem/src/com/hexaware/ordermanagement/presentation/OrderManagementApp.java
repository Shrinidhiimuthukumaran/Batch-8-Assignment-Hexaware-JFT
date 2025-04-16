package com.hexaware.ordermanagement.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.hexaware.ordermanagement.entity.Clothing;
import com.hexaware.ordermanagement.entity.Electronics;
import com.hexaware.ordermanagement.entity.Product;
import com.hexaware.ordermanagement.entity.User;
import com.hexaware.ordermanagement.exception.OrderNotFoundException;
import com.hexaware.ordermanagement.exception.UserNotFoundException;
import com.hexaware.ordermanagement.service.IOrderManagementRepository;
import com.hexaware.ordermanagement.service.OrderProcessor;

public class OrderManagementApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        IOrderManagementRepository service = new OrderProcessor();

        boolean exit = false;

        while (!exit) {
            System.out.println("\nOMS");
            System.out.println("1. Create User");
            System.out.println("2. Create Product");
            System.out.println("3. Create Order");
            System.out.println("4. Cancel Order");
            System.out.println("5. Get All Products");
            System.out.println("6. Get Orders by User");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter user ID: ");
                        int userId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter username: ");
                        String username = sc.nextLine();
                        System.out.print("Enter password: ");
                        String password = sc.nextLine();
                        System.out.print("Enter user role (admin/user): ");
                        String role = sc.nextLine();

                        User user = new User(userId, username, password, role);
                        int userStatus = service.createUser(user);
                        System.out.println(userStatus > 0 ? "User created successfully." : "User creation failed.");
                        break;

                    case 2:
                        System.out.print("Enter admin ID: ");
                        int adminId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter product ID: ");
                        int productId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter product name: ");
                        String productName = sc.nextLine();

                        System.out.print("Enter description: ");
                        String description = sc.nextLine();

                        System.out.print("Enter price: ");
                        double price = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Enter quantity in stock: ");
                        int quantity = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter product type (electronics/clothing): ");
                        String type = sc.nextLine();

                        Product product = null;

                        if (type.equalsIgnoreCase("electronics")) {
                            System.out.print("Enter brand: ");
                            String brand = sc.nextLine();
                            System.out.print("Enter warranty period (months): ");
                            int warranty = sc.nextInt();
                            sc.nextLine();
                            product = new Electronics(productId, productName, description, price, quantity, brand, warranty);
                        } else if (type.equalsIgnoreCase("clothing")) {
                            System.out.print("Enter size: ");
                            String size = sc.nextLine();
                            System.out.print("Enter color: ");
                            String color = sc.nextLine();
                            product = new Clothing(productId, productName, description, price, quantity, size, color);
                        } else {
                            System.out.println("Invalid product type.");
                            break;
                        }

                        User admin = new User(adminId, "", "", "admin");
                        int prodStatus = service.createProduct(admin, product);
                        System.out.println(prodStatus > 0 ? "Product created successfully." : "Product creation failed.");
                        break;

                    case 3:
                        System.out.print("Enter user ID: ");
                        int buyerId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter username: ");
                        String buyerName = sc.nextLine();
                        System.out.print("Enter password: ");
                        String buyerPass = sc.nextLine();

                        User buyer = new User(buyerId, buyerName, buyerPass, "user");

                        List<Product> orderProducts = new ArrayList<>();

                        while (true) {
                            System.out.print("Enter product ID to order (0 to finish): ");
                            int pid = sc.nextInt();
                            if (pid == 0)
                                break;
                            orderProducts.add(new Product(pid, null, null, 0, 0, null));
                        }

                        int orderStatus = service.createOrder(buyer, orderProducts);
                        System.out.println(orderStatus > 0 ? "Order placed successfully." : "Order placement failed.");
                        break;

                    case 4:
                        System.out.print("Enter user ID: ");
                        int cancelUserId = sc.nextInt();
                        System.out.print("Enter order ID to cancel: ");
                        int orderId = sc.nextInt();
                        int cancelStatus = service.cancelOrder(cancelUserId, orderId);
                        System.out.println(cancelStatus > 0 ? "Order cancelled successfully." : "Order cancellation failed.");
                        break;

                    case 5:
                        List<Product> allProducts = service.getAllProducts();
                        System.out.println("Available Products:");
                        for (Product p : allProducts) {
                            System.out.println(p);
                        }
                        break;

                    case 6:
                        System.out.print("Enter user ID: ");
                        int orderUserId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter username: ");
                        String orderUserName = sc.nextLine();
                        System.out.print("Enter password: ");
                        String orderUserPassword = sc.nextLine();

                        User orderUser = new User(orderUserId, orderUserName, orderUserPassword, "user");
                        List<Product> userOrders = service.getOrderByUser(orderUser);
                        System.out.println("Orders for User ID " + orderUserId + ":");
                        for (Product p : userOrders) {
                            System.out.println(p);
                        }
                        break;

                    case 7:
                        exit = true;
                        System.out.println("Thank you");
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            } catch (UserNotFoundException | OrderNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }

        sc.close();
    }
}
