package authentication;

public class AuthData {
	private String username;
	private String password;
	
	public AuthData() {
		
	}
	
	public AuthData(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
