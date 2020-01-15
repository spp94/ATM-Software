package com.atm.DAO;
import java.sql.*;

import com.atm.model.User;
public class AtmDAO {
	private static Connection con = null;
	private static PreparedStatement pst = null;
	private static CallableStatement cst = null;
	private static String url = "jdbc:mysql://localhost:3306/Shubham", username = "root", password = "Archana*130594";
	
	// to establish the connection
	public static void openConnection()throws Exception {
		con = DriverManager.getConnection(url, username, password);
	}

	// to close all the open connections
	public static void closeConnections()throws Exception {
		if(con != null) con.close();
		if(pst != null) pst.close();
		if(cst != null) cst.close();
	}

	// to create a new User
	public static int addUser(User user)throws Exception{
		String query = "insert into users values(?, ?, ?, ?, ?, ?)";
		pst = con.prepareStatement(query);
		pst.setInt(1, user.getId());
		pst.setString(2, user.getAccountNumber());
		pst.setString(3, user.getPin());
		pst.setString(4, user.getName());
		pst.setString(5,  user.getCity());
		pst.setDouble(6, user.getBalance());
		int count = pst.executeUpdate();
		return count;
	}

	// to fetch details of user based on account number
	public static User getUserDetails(String accountNumber)throws Exception {
		String query = "{call getUserDetails(?)}";
		cst = con.prepareCall(query);
		cst.setString(1, accountNumber);
		cst.execute();
		ResultSet rs = cst.getResultSet();
		User user = new User();
		if(rs.next()) {
			user.setId(rs.getInt("id"));
			user.setAccountNumber(rs.getString("accountnumber"));
			user.setPin(rs.getString("pin"));
			user.setName(rs.getString("name"));
			user.setCity(rs.getString("city"));
			user.setBalance(rs.getDouble("balance"));
		}
		return user;
	}

	// to check if a user with this account number and pin exists or not
	public static boolean isAuthenticatedUser(String accountNumber, String pin)throws Exception {
		String query = "select * from users where accountnumber = ? and pin = ?";
		pst = con.prepareStatement(query);
		pst.setString(1, accountNumber);
		pst.setString(2, pin);
		ResultSet rs = pst.executeQuery();
		boolean flag =  rs.next();
		return flag;
	}

	// to update the user balance 
	public static void updateUserDetails(User user)throws Exception{
		String query = "{call updateUserDetails(?, ?)}";
		cst = con.prepareCall(query);
		cst.setString(1, user.getAccountNumber());
		cst.setDouble(2, user.getBalance());
		cst.execute();
	}
	
	// to print user details
	public static void printUserDetails(String accountNumber)throws Exception {
		User user = getUserDetails(accountNumber);
		System.out.println(user);
	}
	
	// to withdraw money
	public static void withdrawMoney(String accountNumber, double amount)throws Exception {
		User user = getUserDetails(accountNumber);
		if(user.getBalance() >= amount) {
			user.setBalance(user.getBalance() - amount);
			updateUserDetails(user);
			System.out.println("Withdrawl successful");
			printUserDetails(accountNumber);
		}
		else {
			System.out.println("Withdrawl amount cannot exceed account balance");
		}
	}

	// to deposit money
	public static void depositAmount(String accountNumber, double amount)throws Exception {
		User user = getUserDetails(accountNumber);
		String query = "update users set balance = ? where accountnumber = ?";
		pst = con.prepareStatement(query);
		pst.setDouble(1, user.getBalance() + amount);
		pst.setString(2, accountNumber);
		pst.executeUpdate();
		System.out.println("Amount deposited successfully");
		printUserDetails(accountNumber);
	}
}
