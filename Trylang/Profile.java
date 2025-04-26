package Trylang;

import java.sql.*;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;


import java.awt.*;

public class Profile {
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
    
    public Integer getUserIdByDisplayName(String displayName) {
        String sql = "SELECT user_id FROM users WHERE user_displayname = ?";

        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, displayName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding user_id for display name " + displayName, e);
        }
    }
    
    public void profile(int userId, String userFirstName, String userLastName, String userPhoneNum) {
    	String query = """
    			UPDATE user_credentials
    				SET user_first_name = ?,
    					user_last_name = ?,
    					user_phone_number = ?
    				WHERE user_id = ?		
    				""";
    			
    
    
    try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
    	PreparedStatement pstmtQuery = conn.prepareStatement(query);
    	pstmtQuery.setString(1,userFirstName );
    	pstmtQuery.setString(2,userLastName );
    	pstmtQuery.setString(3,userPhoneNum );
    	pstmtQuery.setInt(4,userId );
    	int rows = pstmtQuery.executeUpdate();
    	System.out.println(rows + "updated");
    }catch(SQLException e) {
    	e.printStackTrace();
    	throw new RuntimeException("Error updating user with ID " + userId, e);
    }
    }
    
    
    public void selectRole(int userId, String userRole) {
    	String query = """
    			UPDATE users
    			SET user_role = ?
    			WHERE user_id = ?
    			""";
    	
    	try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
        	PreparedStatement pstmtQuery = conn.prepareStatement(query);
        	pstmtQuery.setString(1, userRole );
        	pstmtQuery.setInt(2, userId );
        	int rows = pstmtQuery.executeUpdate();
        	System.out.println(rows + "row updated");
    	 }catch(SQLException e) {
    	    	e.printStackTrace();
    	    	throw new RuntimeException("Error updating user with ID " + userId, e);
    	    }
        	
    }
    
    public void editProfile(String username, String userRole) {
    	int id = getUserIdByDisplayName(username);
    	selectRole(id, userRole);
    	
    }
    
    static class RoleRenderer extends DefaultListCellRenderer {
        private static final Color SEL_BG   = new Color(40,  120, 120);
        private static final Color BG       = new Color(60,   80,  90);
        private static final Color FG       = Color.WHITE;
        private static final Color INACTIVE = Color.LIGHT_GRAY;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus)
        {
            JLabel lbl = (JLabel)super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

            // padding + center text
            lbl.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            // colors
            lbl.setOpaque(true);
            lbl.setBackground(isSelected ? SEL_BG : BG);
            lbl.setForeground(isSelected ? FG : INACTIVE);

            // bold
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            return lbl;
        }
    }
    public void editRole(JComboBox combo) {
         combo.setSelectedIndex(0);    
         combo.setRenderer(new PlaceholderRenderer(new RoleRenderer()));
         combo.setUI(new RoundedComboUI());
         combo.setBounds(493, 449, 200, 27);
         combo.setFont(combo.getFont().deriveFont(Font.PLAIN, 14f));
         combo.setForeground(Color.LIGHT_GRAY);
         combo.setBackground(new Color(60, 80,  90));  // the closed‑combo bg
         combo.setFocusable(false);
    }
    
    static class PlaceholderRenderer implements ListCellRenderer<String> {
        private final ListCellRenderer<? super String> delegate;
        private final String placeholder = "Select a role";

        public PlaceholderRenderer(ListCellRenderer<? super String> delegate) {
            this.delegate = delegate;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list,
                                                      String value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            // delegate the heavy lifting to your RoleRenderer
            JLabel lbl = (JLabel)delegate.getListCellRendererComponent(
                              list, value, index, isSelected, cellHasFocus);

            // if it’s the placeholder (index==0 when painting the list,
            // or index==-1 when painting the “closed” combo), gray it out:
            if (placeholder.equals(value)) {
                lbl.setForeground(Color.LIGHT_GRAY);
                lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC));
            }
            return lbl;
        }
    }
    static class RoundedComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton b = new JButton();
            b.setBorder(null);
            b.setContentAreaFilled(false);
            b.setFocusable(false);
            // draw a simple down‑triangle ourselves:
            b.setIcon(new Icon() {
                private final int size = 12;
                @Override public int getIconWidth()  { return size; }
                @Override public int getIconHeight() { return size; }
                @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                    g.setColor(Color.LIGHT_GRAY);
                    int mid = size/2;
                    int[] xs = {0, size, mid};
                    int[] ys = {2, 2, size-2};
                    g.fillPolygon(xs, ys, 3);
                }
            });
            return b;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(comboBox.getBackground());
            g.fillRoundRect(bounds.x, bounds.y,
                            bounds.width-1, bounds.height-1,
                            20, 20);
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            comboBox.setBorder(new EmptyBorder(0, 8, 0, 0));
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup pop = (BasicComboPopup)super.createPopup();
            pop.setBorder(new LineBorder(new Color(40,120,120), 1, true));
            return pop;
        }
    }
    
    public boolean isAdmin(String username){
    	boolean isAdmin = false;
    	String sql = "SELECT user_role FROM users WHERE user_displayname = ?";

        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	String storedRole = rs.getString("user_role");
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
            throw new RuntimeException("Error finding user_id for display name " + username, e);
        }
      return isAdmin;
        
}
    
    public String getRole(String displayName) {
        String sql = "SELECT user_role FROM users WHERE user_displayname = ?";
        String storedRole = null;
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, displayName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    storedRole = rs.getString("user_role");
                    return storedRole;
                } 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding user_id for display name " + displayName, e);
        }
        return storedRole;
    }
}



