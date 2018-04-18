package src.linkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkItSql {
	private Connection connection = null;
	private final static String addPackage = "INSERT INTO PACKAGE(USERID, TITLE, DESCRIPTION) VALUES(?, ?, ?)";
	private final static String addLink = "INSERT INTO LINK(PACKAGEID, URL) VALUES(?, ?)";
	private final static String addTag = "INSERT INTO TAG(PACKAGEID, TAG) VALUES(?, ?)";
	private final static String addUser = "INSERT INTO LINKITUSER(USERNAME, PASSCODE, EMAIL) VALUES(?,?,?)";
	private final static String searchUser = "SELECT * FROM LinkItUser WHERE USERNAME=?";
	private final static String searchEmail = "SELECT * FROM LinkItUser WHERE EMAIL=?";
	private final static String votePackage = "INSERT INTO RatedPackage(userID, packageID, rating) VALUES(?,?,?)";
	private final static String getUsername = "SELECT * FROM LinkItUser WHERE userID=?";
	private final static String getLinks = "SELECT * FROM Link WHERE packageID=?";
	private final static String newSavedPackage = "INSERT INTO SavedPackage(userID, packageID) VALUES(?,?)";

	public Connection connection() {
		return connection;
	}

	// Issue an SQL query statement and returns a ResultSet if successful
	// or null if not. If not null, close() must be called on the returned
	// ResultSet.
	public ResultSet query(String queryString) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(queryString);
			return rs;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			return null;
		} finally {
			// Cannot close statement if returning a ResultSet because
			// that will close the ResultSet. So call closeOnCompletion which
			// closes the statement when all dependent ResultSets are closed.
			try {
				statement.closeOnCompletion();
			} catch (SQLException e) {
				return null;
			}
		}
	}

	public ResultSet queryEmail(String email) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(searchEmail);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			return null;
		} finally {
			// Cannot close statement if returning a ResultSet because
			// that will close the ResultSet. So call closeOnCompletion which
			// closes the statement when all dependent ResultSets are closed.
			try {
				ps.closeOnCompletion();
			} catch (SQLException e) {
				return null;
			}
		}
	}

	// Issue an SQL update statement. Returns true if successful and false
	// otherwise.
	public boolean update(String updateString) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(updateString);
		} catch (SQLException e) {
			System.out.println("SQLException in update: " + e.getMessage());
			return false;
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("SQLException in update closeOnCompletion: " + e.getMessage());
			}
		}

		return true;
	}

	// Connect to the SQL server.
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost/linkit?user=root&password=NewPassword&useSSL=false");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		}
	}

	public void stop() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean userExists(String value, int type) {
		try {
			PreparedStatement ps = null;
			if (type == 1) {
				ps = connection.prepareStatement(searchUser);
			} else {
				ps = connection.prepareStatement(searchEmail);
			}
			ps.setString(1, value);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Unable to find user with: " + value);
		return false;
	}

	public boolean packageExists(String value) {
		try {
			PreparedStatement ps = null;
			ps = connection.prepareStatement("searchTitle");
			ps.setString(1, value);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Unable to find package with: " + value);
		return false;
	}

	public void insertUser(String username, String email, String password) {
		try {
			PreparedStatement ps = connection.prepareStatement(addUser);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.executeUpdate();
			System.out.println("Adding user " + username + " to table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertPackage(int userID, String title, String bio) {
		try {
			PreparedStatement ps = connection.prepareStatement(addPackage);
			ps.setInt(1, userID);
			ps.setString(2, title);
			ps.setString(3, bio);
			ps.executeUpdate();
			System.out.println("Adding package " + title + " to table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertTag(int packageID, String tag) {
		try {
			// If the tag is a course prefix/num then store in a normalized
			// format.
			String courseName = getNormalizedCourseName(tag);
			if (courseName != null) {
				tag = courseName;
			} else {
				tag = tag.toLowerCase();
			}

			PreparedStatement ps = connection.prepareStatement(addTag);
			ps.setInt(1, packageID);
			ps.setString(2, tag);
			ps.executeUpdate();
			System.out.println("Adding tag " + tag + " to table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertLink(int packageID, String link) {
		try {
			PreparedStatement ps = connection.prepareStatement(addLink);
			ps.setInt(1, packageID);
			ps.setString(2, link);
			ps.executeUpdate();
			System.out.println("Adding link " + link + " to table");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Get the user id from username. Returns 0 if user not found.
	public int getUserId(String username) {
		int userId = 0;
		String sqlString = "SELECT * FROM LinkItUser WHERE username='" + username + "'";
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				userId = rs.getInt("userID");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return userId;
	}

	public String getUserName(int userid) {
		String username = "";
		String sqlString = "SELECT * FROM LinkItUser WHERE userID='" + userid + "'";
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				username = rs.getString("username");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return username;
	}

	public int getPackageId(String packagename) {
		int packageId = 0;
		String sqlString = "SELECT * FROM Package WHERE title='" + packagename + "'";
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				packageId = rs.getInt("packageID");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return packageId;
	}

	public String getPackageName(int packageId) {
		String packageTitle = "null";
		String sqlString = "SELECT * FROM Package WHERE packageID='" + packageId + "'";
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				packageTitle = rs.getString("title");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return packageTitle;
	}

	public String getPackageDescription(int packageId) {
		String description = "null";
		String sqlString = "SELECT * FROM Package WHERE packageID='" + packageId + "'";
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				description = rs.getString("description");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return description;
	}

	public int getPackageAuthorID(int packageId) {
		int packageAuthor = -1;
		String sqlString = "SELECT * FROM Package WHERE packageID='" + packageId + "'";
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				packageAuthor = rs.getInt("userID");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return packageAuthor;
	}

	// Get the pass code for a userId. Returns null if user not found.
	public String getPasscode(int userId) {
		String passcode = null;
		String sqlString = "SELECT * FROM LinkItUser WHERE userID=" + userId;
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				passcode = rs.getString("passcode");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("getPasscode failed: " + sqlString);
		}
		return passcode;
	}

	// Get the saved packages for a userId.
	public ArrayList<Integer> getSavedPackages(int userId) {
		ArrayList<Integer> savedPackages = new ArrayList<>();
		String sqlString = "SELECT * FROM SavedPackage WHERE userID=" + userId;
		ResultSet rs = query(sqlString);
		try {
			while (rs.next()) {
				int packageId = rs.getInt("packageID");
				savedPackages.add(packageId);
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return savedPackages;
	}

	// Get the authored packages for a userId.
	public ArrayList<Integer> getAuthoredPackages(int userId) {
		ArrayList<Integer> authoredPackages = new ArrayList<>();
		String sqlString = "SELECT * FROM Package WHERE userID=" + userId;
		ResultSet rs = query(sqlString);
		try {
			while (rs.next()) {
				int packageId = rs.getInt("packageID");
				authoredPackages.add(packageId);
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return authoredPackages;
	}

	public ArrayList<Integer> getRatedPackages(int userId) {
		ArrayList<Integer> ratedPackages = new ArrayList<>();
		String sqlString = "SELECT * FROM RatedPackage WHERE userID=" + userId;
		ResultSet rs = query(sqlString);
		try {
			while (rs.next()) {
				int packageId = rs.getInt("packageID");
				ratedPackages.add(packageId);
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return ratedPackages;
	}

	// Get the title of a package. Returns null if package not found.
	public String getPackageTitle(int packageId) {
		String title = null;
		String sqlString = "SELECT * FROM Package WHERE packageID=" + packageId;
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				title = rs.getString("title");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return title;
	}

	// Get the rating of a package by adding the up and subtracting the down
	// ratings.
	public int getPackageRating(int packageId) {
		String sqlString = "SELECT * FROM RatedPackage WHERE packageID=" + packageId;
		ResultSet rs = query(sqlString);
		int rating = 0;
		try {
			while (rs.next()) {
				String r = rs.getString("rating");
				if (r.equals("U")) {
					rating++;
				} else if (r.equals("D")) {
					rating--;
				} else {
					System.out.println("Illegal rating " + r + " for package " + packageId);
				}
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}
		return rating;
	}

	public enum Rating {
		UP, DOWN, NONE
	}

	// Get the rating of a package by a given user.
	public Rating getPackageUserRating(int packageId, int userId) {
		Rating rating = Rating.NONE;
		String sqlString = "SELECT * FROM RatedPackage WHERE userID=" + userId + " AND packageID=" + packageId;
		ResultSet rs = query(sqlString);
		try {
			if (rs.next()) {
				String r = rs.getString("rating");
				if (r.equals("U")) {
					rating = Rating.UP;
				} else if (r.equals("D")) {
					rating = Rating.DOWN;
				} else {
					System.out.println("Illegal rating " + r + " for package " + packageId + " by user " + userId);
				}
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}

		return rating;
	}

	public boolean isUpvote(int packageId, int userId) {
		if (getPackageUserRating(packageId, userId) == Rating.UP) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Integer> searchPackagesForTag(String tag) {
		// If the query matches to a course search then modify the query
		// string to be in the normalized format.
		// (e.g. Query 'csci 201' for queries 'csci201' or 'CSCI 201').
		String courseName = getNormalizedCourseName(tag);
		if (courseName != null) {
			tag = courseName;
		} else {
			tag = tag.toLowerCase();
		}

		ArrayList<Integer> packages = new ArrayList<>();
		String sqlString = "SELECT * FROM Tag WHERE tag='" + tag + "'";
		ResultSet rs = query(sqlString);
		try {
			while (rs.next()) {
				int packageId = rs.getInt("packageID");
				packages.add(packageId);
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}
		return packages;
	}

	public ArrayList<Integer> searchPackagesForAuthor(String author) {
		int userId = getUserId(author);
		if (userId != 0) {
			return getAuthoredPackages(userId);
		} else {
			return new ArrayList<Integer>();
		}
	}

	// Get a normalized course name from a query if it matches.
	// If it matches, it returns capital prefix, space, and number
	// For example, 'csci201' or 'csci 201' or 'CSCI201' will return 'CSCI 201'
	// The prefix must be 2 to 4 characters and the num exactly 3 digits
	// separated by 0 or 1 spaces.
	public static String getNormalizedCourseName(String query) {
		// Regex pattern 2 to 4 letters, 0 or 1 spaces, 3 digits.
		Pattern p = Pattern.compile("([a-zA-Z]{2,4})\\s?(\\d{3})");
		Matcher m = p.matcher(query);
		if (!m.matches()) {
			return null;
		}
		String prefix = m.group(1).toUpperCase();
		int num = Integer.parseInt(m.group(2));

		return prefix + " " + num;
	}

	private class RatedPackage {
		public Integer packageId;
		public int rating;
	}

	private class SearchPackagesForTag implements Runnable {
		public ArrayList<Integer> packages;
		private String query;

		SearchPackagesForTag(String q) {
			query = q;
		}

		@Override
		public void run() {
			packages = searchPackagesForTag(query);
		}
	}

	private class SearchPackagesForAuthor implements Runnable {
		public ArrayList<Integer> packages;
		private String query;

		SearchPackagesForAuthor(String q) {
			query = q;
		}

		@Override
		public void run() {
			packages = searchPackagesForAuthor(query);
		}
	}

	// Query all the packages for a given phrase.
	// For now we will only search for a single query but this can be
	// expanded to handle AND and OR for union and intersection.
	public ArrayList<Integer> searchPackages(String queryString) {
		// Merge the results into a set.
		HashSet<Integer> allPackages = new HashSet<>();

		// Split up the query by groups of OR.
		String[] orQueries = queryString.split(" OR ");
		for (String orQuery : orQueries) {
			orQuery = orQuery.trim();

			// Split orQuery by AND.
			String[] andQueries = orQuery.split(" AND ");
			HashSet<Integer> intersection = null;
			for (String query : andQueries) {
				query = query.trim();

				// Search for packages by Tag and Author in parallel.
				SearchPackagesForTag r1 = new SearchPackagesForTag(query);
				SearchPackagesForAuthor r2 = new SearchPackagesForAuthor(query);
				Thread t1 = new Thread(r1);
				Thread t2 = new Thread(r2);
				t1.start();
				t2.start();
				try {
					t1.join();
					t2.join();
				} catch (InterruptedException e) {
					System.out.println("Thread error: " + e.getMessage());
				}

				// Combine the packages from tags and authors.
				HashSet<Integer> packages = new HashSet<>(r1.packages);
				packages.addAll(r2.packages);

				// If the first time through the loop then add all the packages
				// to the intersection.
				if (intersection == null) {
					intersection = packages;
				} else {
					// Otherwise intersect the intersection with packages.
					HashSet<Integer> temp = new HashSet<>();
					for (Integer packageId : packages) {
						if (intersection.contains(packageId)) {
							temp.add(packageId);
						}
					}
					intersection = temp;
				}
			}
			allPackages.addAll(intersection);
		}

		// Sort the packages.
		// Put the set into an array of rated packages.
		ArrayList<RatedPackage> ratedPackages = new ArrayList<>();
		ArrayList<Thread> threads = new ArrayList<>();
		for (Integer packageId : allPackages) {
			RatedPackage ratedPackage = new RatedPackage();
			ratedPackage.packageId = packageId;
			ratedPackages.add(ratedPackage);

			// Compute each package rating in a separate thread.
			Thread t = new Thread(() -> ratedPackage.rating = getPackageRating(packageId));
			threads.add(t);
			t.start();
		}

		// Join all the threads. We can only start the sort once all the threads
		// have finished.
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.out.println("Thread error: " + e.getMessage());
			}
		}

		// Sort ratedPackages in reverse rating order using a lambda function.
		// If r1.rating > r2.rating then lambda returns a negative number so
		// r1 sorts before r2. If r1.rating < r2.rating then lambda returns a
		// positive number so that r2 sorts before r1.
		ratedPackages.sort((RatedPackage r1, RatedPackage r2) -> r2.rating - r1.rating);

		// Copy the sorted ratedPackages to the output packages.
		ArrayList<Integer> packages = new ArrayList<>();
		for (RatedPackage ratedPackage : ratedPackages) {
			packages.add(ratedPackage.packageId);
		}

		return packages;
	}

	public void addVote(int id, int p, boolean up) {
		try {
			if (up) {
				PreparedStatement ps = connection.prepareStatement(votePackage);
				ps.setString(1, Integer.toString(id));
				ps.setString(2, Integer.toString(p));
				ps.setString(3, "U");
				ps.executeUpdate();
			} else {
				PreparedStatement ps = connection.prepareStatement(votePackage);
				ps.setString(1, Integer.toString(id));
				ps.setString(2, Integer.toString(p));
				ps.setString(3, "D");
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getPackageAuthor(int packageID) {
		String sqlString = "SELECT * FROM Package WHERE packageID=" + packageID;
		ResultSet rs = query(sqlString);
		String result = "";
		try {
			while (rs.next()) {
				String r = rs.getString("userID");
				result = getUsername(Integer.parseInt(r));
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}
		return result;
	}

	public String getUsername(int userID) {
		String name = "";
		try {
			PreparedStatement ps = connection.prepareStatement(getUsername);
			ps.setString(1, Integer.toString(userID));
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				name = result.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	public String getPackageEdit(int packageID) {
		String sqlString = "SELECT * FROM Package WHERE packageID=" + packageID;
		ResultSet rs = query(sqlString);
		String result = "";
		try {
			while (rs.next()) {
				result = rs.getString("updated");
			}
			rs.close();
		} catch (SQLException | NullPointerException e) {
			System.out.println("Query failed: " + sqlString);
		}
		return result;
	}

	public Vector<String> getPackageLinks(int packageID) {
		Vector<String> results = new Vector<String>();
		try {
			PreparedStatement ps = connection.prepareStatement(getLinks);
			ps.setString(1, Integer.toString(packageID));
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				results.add(result.getString("url"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public void savePackage(int userID, int packageID) {
		try {
			PreparedStatement ps = connection.prepareStatement(newSavedPackage);
			ps.setString(1, Integer.toString(userID));
			ps.setString(2, Integer.toString(packageID));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
