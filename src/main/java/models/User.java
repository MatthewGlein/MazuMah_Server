package models;

public class User {
	private final String id;
	private final String username;
	private final String password;
	
	public User(String username, String password) {
		this(null, username, password);
	}
	
	public User(String id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
