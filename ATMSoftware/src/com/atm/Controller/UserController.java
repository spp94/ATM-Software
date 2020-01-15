package com.atm.Controller;
import java.util.*;

import com.atm.DAO.AtmDAO;
import com.atm.model.*;

public class UserController {
	private static Scanner sc = new Scanner(System.in);
	public static void main(String[] args)throws Exception {
		while(true) {
		System.out.println("1 : To add new user");
		System.out.println("2 : To Withdraw Amount");
		System.out.println("3 : To Deposit Amount");
		System.out.println("4 : To check Account Details");
		System.out.println("5 : To EXIT");
		System.out.println("Enter your choice : ");
		int ch = Integer.parseInt(sc.nextLine());
		AtmDAO.openConnection();
		switch(ch)
		{
		case 1:
			addUser();
			break;
		case 2:
			withdraw();
			break;
		case 3:
			deposit();
			break;
		case 4:
			showUserDetails();
			break;
		case 5:
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice, please select again !!!");
			break;
		}
		AtmDAO.closeConnections();
		}
	}
	
	// to get user details input
	public static User getUserDetails() {
		System.out.println("Account Number : ");
		User user = new User();
		user.setAccountNumber(sc.nextLine());
		System.out.println("PIN : ");
		user.setPin(sc.nextLine());
		return user;
	}

	// to add a new User
	public static void addUser()throws Exception {
		User user = new User();
		System.out.println("Id : ");
		user.setId(Integer.parseInt(sc.nextLine()));
		System.out.println("Account Number : ");
		user.setAccountNumber(sc.nextLine());
		System.out.println("PIN : ");
		user.setPin(sc.nextLine());
		System.out.println("Name : ");
		user.setName(sc.nextLine());
		System.out.println("City : ");
		user.setCity(sc.nextLine());
		System.out.println("Balance : ");
		user.setBalance(Double.parseDouble(sc.nextLine()));
		int count = AtmDAO.addUser(user);;
		System.out.println(count + " row(s) inserted");
	}

	
	// to withdraw money
	public static void withdraw()throws Exception {
		User user = getUserDetails();
		if(AtmDAO.isAuthenticatedUser(user.getAccountNumber(), user.getPin())) {
		System.out.println("Enter withdrawl amount = ");
		int amount = Integer.parseInt(sc.nextLine());
		AtmDAO.withdrawMoney(user.getAccountNumber(), amount);
		}
		else {
			System.out.println("Invalid Credentials please try again");
		}
	}

	// to deposit money
	public static void deposit()throws Exception{
		User user = getUserDetails();
		if(AtmDAO.isAuthenticatedUser(user.getAccountNumber(), user.getPin())) {
			System.out.println("Enter deposit amount = ");
			int amount = Integer.parseInt(sc.nextLine());
			AtmDAO.depositAmount(user.getAccountNumber(), amount);
		}
		else {
			System.out.println("Invalid Credentials please try again");
		}
	}


	public static void showUserDetails()throws Exception {
		User user = getUserDetails();
		if(AtmDAO.isAuthenticatedUser(user.getAccountNumber(), user.getPin())) {
			AtmDAO.printUserDetails(user.getAccountNumber());
		}
		else {
			System.out.println("Invalid Credentials please try again");
		}
	}
}
