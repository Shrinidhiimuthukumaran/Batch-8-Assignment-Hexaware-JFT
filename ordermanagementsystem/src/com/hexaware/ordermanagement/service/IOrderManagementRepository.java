package com.hexaware.ordermanagement.service;

import java.util.List;

import com.hexaware.ordermanagement.entity.Product;
import com.hexaware.ordermanagement.entity.User;
import com.hexaware.ordermanagement.exception.OrderNotFoundException;
import com.hexaware.ordermanagement.exception.UserNotFoundException;

public interface IOrderManagementRepository {

	int createUser(User user);

    int createProduct(User user, Product product) throws UserNotFoundException;

    int createOrder(User user, List<Product> products) throws UserNotFoundException;

    int cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException;

    List<Product> getAllProducts();

    List<Product> getOrderByUser(User user) throws UserNotFoundException;
}
