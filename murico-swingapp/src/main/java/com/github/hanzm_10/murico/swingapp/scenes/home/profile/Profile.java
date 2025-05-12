package com.github.hanzm_10.murico.swingapp.scenes.home.profile;

import java.sql.*;


import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Profile {
    
    public Integer getUserIdByDisplayName(String displayName) {

        try {
            var conn = MySqlFactoryDao.createConnection();
            var query = MySqlQueryLoader.getInstance().get("get_user_by_display_name", "users", SqlQueryType.SELECT);
         
            var statement = conn.prepareStatement(query);
            statement.setString(1, displayName);

            ResultSet rs = statement.executeQuery(); 
                if (rs.next()) {
                    return rs.getInt("_user_id");
                } else {
                    return null;
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding user_id for display name " + displayName, e);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public void profile(int userId, String userFirstName, String userLastName, String email, String role) {
  
    
    try{var conn = MySqlFactoryDao.createConnection();
    	var query = MySqlQueryLoader.getInstance().get("update_profile", "users", SqlQueryType.UPDATE);
    	var pstmtQuery = conn.prepareStatement(query);
    	pstmtQuery.setString(1, userFirstName );
    	pstmtQuery.setString(2, userLastName );
    	pstmtQuery.setString(3, email);
    	pstmtQuery.setString(4, role);
    	pstmtQuery.setInt(5, userId );
    	int rows = pstmtQuery.executeUpdate();
    	System.out.println(rows + "updated");
    }catch(SQLException e) {
    	e.printStackTrace();
    	throw new RuntimeException("Error updating user with ID " + userId, e);
    } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
    
    public boolean isAdmin(String username){
    	boolean isAdmin = false;

        try {
            var conn = MySqlFactoryDao.createConnection();
            var query = MySqlQueryLoader.getInstance().get("get_user_roles_by_username", "users_roles", SqlQueryType.SELECT);
            var ps = conn.prepareStatement(query);
        
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	String storedRole = rs.getString("name");
                	if(storedRole.equalsIgnoreCase("admin")) {
                		return isAdmin = true;
                	}
                 else {
                	 System.out.println(username + " is not admin");
                    return isAdmin = false;
                 }
            }
        } }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding _user_id for display name " + username, e);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      return isAdmin;
        
}
    
    public String getRoleByUsername(String displayName) {
        String storedRole = null;
        try {
        	var conn = MySqlFactoryDao.createConnection();
        	var query = MySqlQueryLoader.getInstance().get("get_user_roles_by_username", "users_roles", SqlQueryType.SELECT);
            var ps = conn.prepareStatement(query);
        
            ps.setString(1, displayName);

            ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    storedRole = rs.getString("name");
                    return storedRole;
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding _user_id for display name " + displayName, e);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return storedRole;
    }
    
}
