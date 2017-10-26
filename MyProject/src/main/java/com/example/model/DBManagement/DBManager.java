package com.example.model.DBManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component
final class DBManager {

	//private static DBManager instance;
	private Connection con;

	public DBManager(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			//TODO
			System.out.println("Driver not found or failed to load. Check your libraries");
		}
		
		//to be initialized with proper data:
		final String DB_IP = "127.0.0.1";
		final String DB_PORT = "3306";
		final String DB_DBNAME = "traveller";
		final String DB_USER = "root";
		final String DB_PASS = "poncho";
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_DBNAME, DB_USER, DB_PASS);
		} catch (SQLException e) {
			//TODO
			System.out.println("Error with your this.getConnection() to database");
		}
	}


	public Connection getCon(){
		return this.con;
	}

	
	void closeConnection(){
			if(this.con != null){
				try {
					this.con.close();
				} catch (SQLException e) {
					//TODO
					e.printStackTrace();
				}
			}
	}
	
}