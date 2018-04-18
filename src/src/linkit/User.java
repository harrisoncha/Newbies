package src.linkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class User {
	private String username;
	private String password;
	private String email;
	private int userID;
	private Set<Integer> votedFor;

	public User() {
		votedFor = new HashSet<Integer>();
	}

	public void populateVoted() {
		LinkItSql sql = new LinkItSql();
		sql.connect();

		ArrayList<Integer> list = sql.getRatedPackages(userID);
		for (Integer i : list) {
			votedFor.add(i);
		}
	}

	public boolean voted(Integer p) {
		return votedFor.contains(p);
	}

	public void addVoted(Integer p) {
		votedFor.add(p);
	}

	// GETTERS
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	// SETTERS
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
}
