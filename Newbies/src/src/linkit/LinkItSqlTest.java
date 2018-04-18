package src.linkit;

import java.util.ArrayList;

public class LinkItSqlTest {
	private static LinkItSql linkitSql = new LinkItSql();

	public static void main(String[] args) {
		try {
			// Connect to the sql server.
			linkitSql.connect();
			/*
			 * if (!validConnection) { throw new
			 * Exception("Could not connect to server"); }
			 */

			// Run tests.
			userTests(linkitSql);
			packageRatings(linkitSql);
			courseNameParseTests();
			queryTests(linkitSql);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		System.out.println("All tests passed!");
	}

	private static void userTests(LinkItSql linkitSql) throws Exception {
		int userId = linkitSql.getUserId("jmiller");
		if (userId == 0) {
			throw new Exception("User jmiller not found.");
		}

		String passcode = linkitSql.getPasscode(userId);
		if (passcode == null) {
			throw new Exception("User jmiller passcode not found.");
		}
	}

	private static void packageRatings(LinkItSql linkitSql) throws Exception {
		String packageTitle = linkitSql.getPackageTitle(1);
		if (packageTitle == null) {
			throw new Exception("package 1 does not have a title.");
		}

		int rating1 = linkitSql.getPackageRating(1);
		if (rating1 != -3) {
			throw new Exception("package 1 has incorrect rating of " + rating1);
		}

		int rating2 = linkitSql.getPackageRating(2);
		if (rating2 != -3) {
			throw new Exception("package 2 has incorrect rating of " + rating2);
		}

		int rating3 = linkitSql.getPackageRating(3);
		if (rating3 != -1) {
			throw new Exception("package 3 has incorrect rating of " + rating3);
		}

		int rating4 = linkitSql.getPackageRating(4);
		if (rating4 != 5) {
			throw new Exception("package 4 has incorrect rating of " + rating4);
		}

		int rating5 = linkitSql.getPackageRating(5);
		if (rating5 != 0) {
			throw new Exception("package 5 has incorrect rating of " + rating5);
		}

		int rating6 = linkitSql.getPackageRating(6);
		if (rating6 != 1) {
			throw new Exception("package 6 has incorrect rating of " + rating6);
		}

		int rating7 = linkitSql.getPackageRating(7);
		if (rating7 != 1) {
			throw new Exception("package 7 has incorrect rating of " + rating7);
		}
	}

	private static void queryTests(LinkItSql linkitSql) throws Exception {
		String query = "Battle of Waterloo";
		ArrayList<Integer> tagPackages = linkitSql.searchPackages(query);
		if (tagPackages.size() != 1) {
			throw new Exception("searchPackages should have one entry for " + query);
		} else if (tagPackages.get(0) != 1) {
			throw new Exception("Wrong package id " + tagPackages.get(0) + " for query " + query);
		}

		query = "hist 101";
		ArrayList<Integer> coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 2) {
			throw new Exception(
					"searchPackages should have two entry for " + query + ", but has " + coursePackages.size());
		} else if (coursePackages.get(0) != 3 || coursePackages.get(1) != 1) {
			throw new Exception("Wrong package ids for query " + query);
		}

		query = "CSCI201";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 3) {
			throw new Exception("searchPackages should have three entries for " + query);
		} else if (coursePackages.get(0) != 4 || coursePackages.get(1) != 5 || coursePackages.get(2) != 2) {
			throw new Exception("Wrong package ids for query " + query);
		}

		// jmiller should return 4 then 2 since 4 has a higher rating.
		query = "jmiller";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 2) {
			throw new Exception("searchPackages should have two entries for " + query);
		} else if (coursePackages.get(0) != 4 || coursePackages.get(1) != 2) {
			throw new Exception("Wrong package ids for query " + query);
		}

		// Should return 4, 5, then 2 in that rating order.
		query = "csci 201 OR jmiller";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 3) {
			throw new Exception("searchPackages should have two entries for " + query);
		} else if (coursePackages.get(0) != 4 || coursePackages.get(1) != 5 || coursePackages.get(2) != 2) {
			throw new Exception("Wrong package ids for query " + query);
		}

		query = "jmiller AND filipsan";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 0) {
			throw new Exception("searchPackages should have no entries for " + query);
		}

		query = "csci 201 AND CSCI104";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 1) {
			throw new Exception("searchPackages should have one entry for " + query);
		} else if (coursePackages.get(0) != 4) {
			throw new Exception("Wrong package ids for query " + query);
		}

		query = "csci 201 AND jmiller";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 2) {
			throw new Exception("searchPackages should have two entries for " + query);
		} else if (coursePackages.get(0) != 4 || coursePackages.get(1) != 2) {
			throw new Exception("Wrong package ids for query " + query);
		}

		query = "csci 201 AND jmiller OR filipsan AND HIST101";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 3) {
			throw new Exception("searchPackages should have three entries for " + query);
		} else if (coursePackages.get(0) != 4 || coursePackages.get(1) != 1) {
			throw new Exception("Wrong package ids for query " + query);
		}

		query = "csci 201 AND jmiller OR csci 201 AND CSCI104";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 2) {
			throw new Exception("searchPackages should have three entries for " + query);
		} else if (coursePackages.get(0) != 4 || coursePackages.get(1) != 2) {
			throw new Exception("Wrong package ids for query " + query);
		}

		query = "csci 201 OR jmiller OR CSCI104";
		coursePackages = linkitSql.searchPackages(query);
		if (coursePackages.size() != 3) {
			throw new Exception("searchPackages should have two entries for " + query);
		} else if (coursePackages.get(0) != 4 || coursePackages.get(1) != 5 || coursePackages.get(2) != 2) {
			throw new Exception("Wrong package ids for query " + query);
		}
	}

	private static void courseNameParseTests() throws Exception {
		String course = LinkItSql.getNormalizedCourseName("csci201");
		if (!course.equals("CSCI 201")) {
			throw new Exception("Could not parse csci201");
		}

		course = LinkItSql.getNormalizedCourseName("CSCI 201");
		if (!course.equals("CSCI 201")) {
			throw new Exception("Could not parse CSCI 201");
		}

		// Two spaces should not match.
		course = LinkItSql.getNormalizedCourseName("csci  201");
		if (course != null) {
			throw new Exception("'csci  201' should not have parsed as a course.");
		}

		course = LinkItSql.getNormalizedCourseName("cscicsci201");
		if (course != null) {
			throw new Exception("cscicsci201 should not have parsed as a course.");
		}

		course = LinkItSql.getNormalizedCourseName("a201");
		if (course != null) {
			throw new Exception("a201 should not have parsed as a course.");
		}

		course = LinkItSql.getNormalizedCourseName("asdfg201");
		if (course != null) {
			throw new Exception("asdfg201 should not have parsed as a course.");
		}
	}
}
