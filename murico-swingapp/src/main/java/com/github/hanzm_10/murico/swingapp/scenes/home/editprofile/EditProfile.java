package com.github.hanzm_10.murico.swingapp.scenes.home.editprofile;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.scenes.home.profile.Profile;

public class EditProfile extends Profile {
	static class PlaceholderRenderer implements ListCellRenderer<String> {
		private final ListCellRenderer<? super String> delegate;
		private final String placeholder = "Select a gender";

		public PlaceholderRenderer(ListCellRenderer<? super String> delegate) {
			this.delegate = delegate;
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			// delegate the heavy lifting to your RoleRenderer
			JLabel lbl = (JLabel) delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			// if it’s the placeholder (index==0 when painting the list,
			// or index==-1 when painting the “closed” combo), gray it out:
			if (placeholder.equals(value)) {
				lbl.setForeground(Color.LIGHT_GRAY);
				lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC));
			}
			return lbl;
		}
	}

	static class RoleRenderer extends DefaultListCellRenderer {
		private static final Color SEL_BG = new Color(40, 120, 120);
		private static final Color BG = new Color(60, 80, 90);
		private static final Color FG = Color.WHITE;
		private static final Color INACTIVE = Color.LIGHT_GRAY;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

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

				@Override
				public int getIconHeight() {
					return size;
				}

				@Override
				public int getIconWidth() {
					return size;
				}

				@Override
				public void paintIcon(Component c, Graphics g, int x, int y) {
					g.setColor(Color.LIGHT_GRAY);
					int mid = size / 2;
					int[] xs = { 0, size, mid };
					int[] ys = { 2, 2, size - 2 };
					g.fillPolygon(xs, ys, 3);
				}
			});
			return b;
		}

		@Override
		protected ComboPopup createPopup() {
			BasicComboPopup pop = (BasicComboPopup) super.createPopup();
			pop.setBorder(new LineBorder(new Color(40, 120, 120), 1, true));
			return pop;
		}

		@Override
		public void installUI(JComponent c) {
			super.installUI(c);
			comboBox.setBorder(new EmptyBorder(0, 8, 0, 0));
		}

		@Override
		public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
			g.setColor(comboBox.getBackground());
			g.fillRoundRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 20, 20);
		}
	}

	public void editProfile(String username, String userRole) {
		int id = getUserIdByDisplayName(username);
		selectRole(id, userRole);

	}

	public void editRole(JComboBox combo) {
		combo.setSelectedIndex(0);
		combo.setRenderer(new PlaceholderRenderer(new RoleRenderer()));
		combo.setUI(new RoundedComboUI());
		combo.setBounds(432, 524, 257, 31);
		combo.setFont(combo.getFont().deriveFont(Font.PLAIN, 14f));
		combo.setForeground(Color.LIGHT_GRAY);
		combo.setBackground(new Color(60, 80, 90)); // the closed‑combo bg
		combo.setFocusable(false);
	}
public class EditProfile extends Profile {
	static class PlaceholderRenderer implements ListCellRenderer<String> {
		private final ListCellRenderer<? super String> delegate;
		private final String placeholder = "Select a gender";

		public PlaceholderRenderer(ListCellRenderer<? super String> delegate) {
			this.delegate = delegate;
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			// delegate the heavy lifting to your RoleRenderer
			JLabel lbl = (JLabel) delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			// if it’s the placeholder (index==0 when painting the list,
			// or index==-1 when painting the “closed” combo), gray it out:
			if (placeholder.equals(value)) {
				lbl.setForeground(Color.LIGHT_GRAY);
				lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC));
			}
			return lbl;
		}
	}

	static class RoleRenderer extends DefaultListCellRenderer {
		private static final Color SEL_BG = new Color(40, 120, 120);
		private static final Color BG = new Color(60, 80, 90);
		private static final Color FG = Color.WHITE;
		private static final Color INACTIVE = Color.LIGHT_GRAY;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

	public void selectRole(int userId, String userRole) {
		String query = """
				UPDATE users
				SET gender = ?
				WHERE _user_id = ?
				""";
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

				@Override
				public int getIconHeight() {
					return size;
				}
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

				@Override
				public int getIconHeight() {
					return size;
				}
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

				@Override
				public int getIconHeight() {
					return size;
				}
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

				@Override
				public int getIconHeight() {
					return size;
				}

		try {
			var conn = MySqlFactoryDao.createConnection();
			// var query = MySqlQueryLoader.getInstance().get("update_user_gender", "users",
			// SqlQueryType.UPDATE);
			PreparedStatement pstmtQuery = conn.prepareStatement(query);
			pstmtQuery.setString(1, userRole);
			pstmtQuery.setInt(2, userId);
			int rows = pstmtQuery.executeUpdate();
			System.out.println(rows + "row updated");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error updating user with ID " + userId, e);
		}
				@Override
				public int getIconWidth() {
					return size;
				}
				@Override
				public int getIconWidth() {
					return size;
				}

				@Override
				public void paintIcon(Component c, Graphics g, int x, int y) {
					g.setColor(Color.LIGHT_GRAY);
					int mid = size / 2;
					int[] xs = { 0, size, mid };
					int[] ys = { 2, 2, size - 2 };
					g.fillPolygon(xs, ys, 3);
				}
			});
			return b;
		}

		@Override
		protected ComboPopup createPopup() {
			BasicComboPopup pop = (BasicComboPopup) super.createPopup();
			pop.setBorder(new LineBorder(new Color(40, 120, 120), 1, true));
			return pop;
		}

	            // bold
	            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
	            return lbl;
	        }
	    }
	    public void editRole(JComboBox combo) {
	         combo.setSelectedIndex(0);    
	         combo.setRenderer(new PlaceholderRenderer(new RoleRenderer()));
	         combo.setUI(new RoundedComboUI());
	         combo.setBounds(432, 524, 257, 31);
	         combo.setFont(combo.getFont().deriveFont(Font.PLAIN, 14f));
	         combo.setForeground(Color.LIGHT_GRAY);
	         combo.setBackground(new Color(60, 80,  90));  // the closed‑combo bg
	         combo.setFocusable(false);
	    }
	    
	    static class PlaceholderRenderer implements ListCellRenderer<String> {
	        private final ListCellRenderer<? super String> delegate;
	        private final String placeholder = "Select a gender";

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
	    
}
