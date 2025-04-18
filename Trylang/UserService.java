package Trylang;
import java.sql.*;

public class UserService {
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
    
    public int registerUSer(User user) throws SQLException{
    	int userId = -1;
    	try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
    		String username = user.getUserName();
    		String checkQuery = "SELECT COUNT(*) FROM users WHERE user_displayName = ?";
    		PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
    		checkStmt.setString(1, username);
    		ResultSet rs = checkStmt.executeQuery();

    		if (rs.next() && rs.getInt(1) > 0) {
    		    System.out.println("Username already exists. Enter another one.");
    		}
    		else {
    		String insertUsers = "INSERT INTO users(user_displayName) VALUES(?)";
    		try(PreparedStatement pstmtUsers = conn.prepareStatement(insertUsers, Statement.RETURN_GENERATED_KEYS)){
    			
    			pstmtUsers.setString(1, user.getUserName());
    			
    			pstmtUsers.executeUpdate();
    			
    			try(ResultSet genKeys = pstmtUsers.getGeneratedKeys()){
    				if(genKeys.next()) {
    					userId = genKeys.getInt(1);
    				}else {
    					throw new SQLException("Failed to retrieve the user ID.");
    				}
    			}
    			
    		}}
    		
    		String email = user.getEmailAdd();
    		String rawPassword = user.getPassword();
    		
    		String password = Rmethods.convert(rawPassword);
    		String checkEmail = "SELECT COUNT(*) FROM user_credentials WHERE user_email = ?";
    		PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmail);
    		checkEmailStmt.setString(1, email);
    		ResultSet rsEmail = checkEmailStmt.executeQuery();

    		if (rsEmail.next() && rsEmail.getInt(1) > 0) {
    		    System.out.println("Email address already exists. Choose another one.");
    		}else {
    		
    		String insertUserCred = "INSERT INTO user_credentials(user_id, user_email, user_password) VALUES (?, ?, ?)";
			try(PreparedStatement pstmtCreds = conn.prepareStatement(insertUserCred)){
				pstmtCreds.setInt(1, userId);
                pstmtCreds.setString(2, email);
                pstmtCreds.setString(3, password);
                pstmtCreds.executeUpdate();
			}
    		}
    	}
    	return userId;
    	
    }
    
    public boolean login(String userName, String password) throws SQLException{
    	boolean isAuthenticated = false;
    	try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
    		String query = "SELECT u.user_id, u.user_displayName, u.user_role, uc.user_email, uc.user_password " +
    	               "FROM users u " +
    	               "JOIN user_credentials uc ON u.user_id = uc.user_id " +
    	               "WHERE u.user_displayName = ?";

    		
    		try(PreparedStatement pstmtLogin= conn.prepareStatement(query)){
    			pstmtLogin.setString(1, userName);
    			try(ResultSet rsLogin = pstmtLogin.executeQuery()){
    				if(rsLogin.next()) {
    					String storedPassword = rsLogin.getString("user_password");
    					String storedRole = rsLogin.getString("user_role");
    					if(storedPassword.equals(Rmethods.convert(password)) && !(storedRole.equals("to_be_assigned"))) {
    						System.out.println(Rmethods.convert(password));
    						isAuthenticated = true;
    					}
    				}
    			}
    		}
    	}
    	return isAuthenticated;
    }
}
