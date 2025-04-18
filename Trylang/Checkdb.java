package Trylang;
import java.sql.*;

public class Checkdb {
	public static void main(String[] args) throws SQLException {
		
		String url = UserService.getURL();
		String user = UserService.getUser();
		String password = UserService.getPassword();
		
		try(Connection conn = DriverManager.getConnection(url, user, password)){
			String query = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Get metadata for column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columns; i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();

            // Print rows
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }

            // Close connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();

		}
		
	}
}
