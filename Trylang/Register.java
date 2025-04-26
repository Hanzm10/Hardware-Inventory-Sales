package Trylang;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class Register {
	private static final String URL = "jdbc:mysql://localhost:3306/murico"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "ueybtjh2012"; 
    
    public static String getURL() {
    	return URL;
    }
    public static String getUser() {
    	return USER;
    }
    public static String getPassword() {
    	return PASSWORD;
    }
	/*
	 * Password requirements:
	 * must be minimum of 8 characters
	 * must have one Uppercase and special characters
	 * */
	
	public boolean isPasswordValid(String password){
		
        String passLowercase = password.toLowerCase();
        boolean isValid = false;
        boolean hasSpecialCharac = false;
        int passLength = password.length();
        
        String specialCharac = "!@#$%&*_-";
        
        if(passLength >= 8) {
            for(int i = 0; i < passLength; i++) {
            	char ch = password.charAt(i);
             	if(specialCharac.indexOf(ch) >= 0) {
             		hasSpecialCharac = true;
            	
             	}}
            
             if(hasSpecialCharac == false && (passLowercase.equals(password))) {
            	 System.out.println("password should have atleast 1 special character");
            	 System.out.println("password should have atleat 1 uppercase");
            	 //JOptionPane.showMessageDialog(null, "Password should have atleat 1 uppercase and 1 special characters." ,"Notice", JOptionPane.INFORMATION_MESSAGE);
            	 showMessage("Password should have atleat 1 uppercase and 1 special characters.");
             }
        	
             else if((passLowercase.equals(password))) {
             	System.out.println("password should have atleat 1 uppercase");
             	showMessage("password should have atleat 1 uppercase");

             } 
             else if(hasSpecialCharac == false) {
            	 System.out.println("password should have atleast 1 special character");
            	 showMessage("password should have atleast 1 special character");
             }
             else {
            	 isValid = true;
             }
        }
        else {
        	System.out.print("password should be atleast 8 characters");
        	showMessage("password should be atleast 8 characters");
        }
		return isValid;
        
	}
	public boolean isEmailValid(String email) {
		if(email.contains("@") && (email.contains("gmail.com")) || email.contains("edu.ph")) {
			System.out.println("Valid Email");
			
			try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
				String checkEmail = "SELECT COUNT(*) FROM user_credentials WHERE user_email = ?";
				PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmail);
				checkEmailStmt.setString(1, email);
				ResultSet rsEmail = checkEmailStmt.executeQuery();
				

				if (rsEmail.next() && rsEmail.getInt(1) > 0) {
				    System.out.println("Email address already exists. Choose another one.");
				    showMessage("Email address already exists. Choose another one.");
				}
				else {
					return true;
				}
				}catch(SQLException e) {
					e.printStackTrace();
				}		
		}

		
		else{System.out.println("Invalid Email");
			showMessage("Invalid Email");}
		return false;
	}
	public boolean isUsernameValid(String username) {
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
    		String checkQuery = "SELECT COUNT(*) FROM users WHERE user_displayName = ?";
    		PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
    		checkStmt.setString(1, username);
    		ResultSet rs = checkStmt.executeQuery();

    		if (rs.next() && rs.getInt(1) > 0) {
    		    System.out.println("Username already exists. Enter another one.");
    		    showMessage("Username already exists. Enter another one.");
    		    return false;
    		}
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void registerAuth(String username, String password, String email){
		boolean validUsername = isUsernameValid(username);
		boolean validPassword = isPasswordValid(password);
		boolean validEmail = isEmailValid(email);
		
		if(validUsername && validPassword && validEmail) {
		User user = new User(username, email, password);
        
        UserService userService = new UserService();
        try {
        	userService.registerUSer(user);
        	System.out.println("Registered Sucessfully\nusername: "+ username +"\nemail: "+ email+ "\npassword: " + password+"\nis added to the database");
        	JOptionPane.showMessageDialog(null, "Thank you for signing up! Your account is under review, and we will notify you as soon as it’s approved.","Notice", JOptionPane.INFORMATION_MESSAGE);
        }catch(SQLException e1) {
        	System.out.println("Failed to register");
        	//System.out.println("username: "+username +"\nemail: "+ email+ "\npassword: " + password+"\nhas been already used");
        	//JOptionPane.showMessageDialog(null, "Failed to register username: "+username +"\nemail: "+ email+ "\npassword: " + password+"\nhas been already used","Notice", JOptionPane.INFORMATION_MESSAGE);
        	e1.printStackTrace();
	}
	}
	}
	
	public void showMessage(String text) {
		JOptionPane.showMessageDialog(null, text, "Notice", JOptionPane.INFORMATION_MESSAGE);
		
	}
}
