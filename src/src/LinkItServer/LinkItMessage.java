package src.LinkItServer;

import java.io.Serializable;

public class LinkItMessage implements Serializable {
	public static final long serialVersionUID = 1;

	private String name;
	private String message;

	public LinkItMessage(String name, String message) {
		this.name = name;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

}