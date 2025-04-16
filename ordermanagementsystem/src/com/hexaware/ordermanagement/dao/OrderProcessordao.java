package com.hexaware.ordermanagement.dao;

import java.sql.*;
import java.util.*;

import com.hexaware.ordermanagement.entity.Clothing;
import com.hexaware.ordermanagement.entity.Electronics;
import com.hexaware.ordermanagement.entity.Product;
import com.hexaware.ordermanagement.entity.User;
import com.hexaware.ordermanagement.exception.OrderNotFoundException;
import com.hexaware.ordermanagement.exception.UserNotFoundException;

public class OrderProcessordao implements IOrderManagementRepositorydao {

    @Override
    public int createUser(User user) {
        int count = 0;

        try (Connection conn = DBUtil.getDBConnection()) {
            String insert = "INSERT INTO users VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert);
            pstmt.setInt(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());

            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }

        return count;
    }

    @Override
    public int createProduct(User user, Product product) throws UserNotFoundException {
        int count = 0;

        try (Connection conn = DBUtil.getDBConnection()) {
            if (!isAdmin(user.getUserId(), conn)) {
                throw new UserNotFoundException("Admin user not found.");
            }

            String insert = "INSERT INTO products VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert);

            pstmt.setInt(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getQuantityInStock());
            pstmt.setString(6, product.getType());

            // Subclass-specific values
            if (product instanceof Electronics) {
                Electronics e = (Electronics) product;
                pstmt.setString(7, e.getBrand());
                pstmt.setInt(8, e.getWarrantyPeriod());
                pstmt.setNull(9, Types.VARCHAR);
                pstmt.setNull(10, Types.VARCHAR);
            } else if (product instanceof Clothing) {
                Clothing c = (Clothing) product;
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setNull(8, Types.INTEGER);
                pstmt.setString(9, c.getSize());
                pstmt.setString(10, c.getColor());
            } else {
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setNull(8, Types.INTEGER);
                pstmt.setNull(9, Types.VARCHAR);
                pstmt.setNull(10, Types.VARCHAR);
            }

            count = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error creating product: " + e.getMessage());
        }

        return count;
    }

    @Override
    public int createOrder(User user, List<Product> products) throws UserNotFoundException {
        int count = 0;

        try (Connection conn = DBUtil.getDBConnection()) {
            if (!userExists(user.getUserId(), conn)) {
                createUser(user); 
            }

            String insert = "INSERT INTO orders (user_id, product_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert);

            for (Product product : products) {
                pstmt.setInt(1, user.getUserId());
                pstmt.setInt(2, product.getProductId());

                
                count += pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
        }

        return count;
    }

    @Override
    public int cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException {
        int count = 0;

        try (Connection conn = DBUtil.getDBConnection()) {
            if (!userExists(userId, conn)) {
                throw new UserNotFoundException("User not found");
            }

            String checkOrder = "SELECT * FROM orders WHERE order_id = ? AND user_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkOrder);
            checkStmt.setInt(1, orderId);
            checkStmt.setInt(2, userId);

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                throw new OrderNotFoundException("Order not found");
            }

            String delete = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(delete);
            deleteStmt.setInt(1, orderId);
            count = deleteStmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error cancelling order: " + e.getMessage());
        }

        return count;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtil.getDBConnection()) {
            String select = "SELECT * FROM products";
            PreparedStatement pstmt = conn.prepareStatement(select);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");

                if ("Electronics".equalsIgnoreCase(type)) {
                    Electronics e = new Electronics(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getString("brand"),
                        rs.getInt("warranty_period")
                    );
                    list.add(e);
                } else if ("Clothing".equalsIgnoreCase(type)) {
                    Clothing c = new Clothing(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getString("size"),
                        rs.getString("color")
                    );
                    list.add(c);
                } else {
                    Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        type
                    );
                    list.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }

        return list;
    }

    @Override
    public List<Product> getOrderByUser(User user) throws UserNotFoundException {
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtil.getDBConnection()) {
            if (!userExists(user.getUserId(), conn)) {
                throw new UserNotFoundException("User not found");
            }

            String select = "SELECT p.* FROM products p JOIN orders o ON p.product_id = o.product_id WHERE o.user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(select);
            pstmt.setInt(1, user.getUserId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");

                if ("Electronics".equalsIgnoreCase(type)) {
                    Electronics e = new Electronics(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getString("brand"),
                        rs.getInt("warranty_period")
                    );
                    list.add(e);
                } else if ("Clothing".equalsIgnoreCase(type)) {
                    Clothing c = new Clothing(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getString("size"),
                        rs.getString("color")
                    );
                    list.add(c);
                } else {
                    Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        type
                    );
                    list.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching user orders: " + e.getMessage());
        }

        return list;
    }

    

    private boolean userExists(int userId, Connection conn) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    private boolean isAdmin(int userId, Connection conn) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ? AND role = 'Admin'";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
}
