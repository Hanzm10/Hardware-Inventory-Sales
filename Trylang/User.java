package Trylang;

public class User {
	private String userName;
	private String emailAdd;
	private String password;

	
	public User(String userName, String emailAdd, String password) {

		this.userName = userName;
		this.emailAdd = emailAdd;
		this.password = password;
	}
	
	public String getUserName() {return userName;}
	public String getEmailAdd() {return emailAdd;}
	public String getPassword() {return password;}
}
