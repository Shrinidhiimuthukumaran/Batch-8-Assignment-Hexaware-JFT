package com.hexaware.ordermanagement.service;

import java.util.List;

import com.hexaware.ordermanagement.dao.IOrderManagementRepositorydao;
import com.hexaware.ordermanagement.dao.OrderProcessordao;
import com.hexaware.ordermanagement.entity.Product;
import com.hexaware.ordermanagement.entity.User;
import com.hexaware.ordermanagement.exception.OrderNotFoundException;
import com.hexaware.ordermanagement.exception.UserNotFoundException;

public class OrderProcessor implements IOrderManagementRepository{

	IOrderManagementRepositorydao repo=new OrderProcessordao();
	
	
	@Override
	public int createUser(User user) {
		// TODO Auto-generated method stub
		return repo.createUser(user);
	}

	@Override
	public int createProduct(User user, Product product) throws UserNotFoundException {
		// TODO Auto-generated method stub
		return repo.createProduct(user, product);
	}

	@Override
	public int createOrder(User user, List<Product> products) throws UserNotFoundException {
		// TODO Auto-generated method stub
		return repo.createOrder(user, products);
	}

	@Override
	public int cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException {
		// TODO Auto-generated method stub
		return repo.cancelOrder(userId, orderId);
	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return repo.getAllProducts();
	}

	@Override
	public List<Product> getOrderByUser(User user) throws UserNotFoundException {
		// TODO Auto-generated method stub
		return repo.getOrderByUser(user);
	}
	
	

}
