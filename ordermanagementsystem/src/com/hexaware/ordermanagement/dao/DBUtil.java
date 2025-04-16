package com.hexaware.ordermanagement.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil { // Factory class

	public static Connection getDBConnection() throws SQLException {// Factory method

		FileReader fr = null;
		Properties prop =null ;
		try {
			fr = new FileReader("src/Database.properties");
			
			prop = new Properties();

			prop.load(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		String url = prop.getProperty("url");

		String username = prop.getProperty("username");

		String password = prop.getProperty("password");

		DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

		Connection conn = DriverManager.getConnection(url, username, password);

		return conn;

	}

}
