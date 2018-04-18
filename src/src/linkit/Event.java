package src.linkit;

import java.io.Serializable;

public class Event implements Serializable {
	public static final long serialVersionUID = 1;
	private String action;
	private String user;
	private String package_name;

	public Event() {

	}

	// GETTERS
	public String getAction() {
		return action;
	}

	public String getUser() {
		return user;
	}

	public String getPackage_name() {
		return package_name;
	}

	// SETTERS
	public void setAction(String action) {
		this.action = action;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

}
