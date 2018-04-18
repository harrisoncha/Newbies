package src.linkit;

import java.util.HashSet;
import java.util.Set;

public class Package {
	private Set<String> links;
	private String author;
	private String date;
	private int score;
	private String bio;
	private String tag;
	private int id;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void addBio(String bio) {
		this.bio = bio;
	}

	public void addTag(String tag) {
		this.tag = tag;
	}

	public String getBio() {
		return bio;
	}

	public String getTag() {
		return tag;
	}

	public Package() {
		links = new HashSet<String>();
	}

	public void addLinks(String link) {
		links.add(link);
	}

	// GETTERS
	public Set<String> getLinks() {
		return links;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public int getScore() {
		return score;
	}

	// SETTERS
	public void setLinks(Set<String> links) {
		this.links = links;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
