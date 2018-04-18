package src.LinkItServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class LinkItClient extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Scanner scan = null;
	private Queue<String> feedElements;

	public LinkItClient(String hostname, int port) {
		Socket s = null;
		try {
			s = new Socket(hostname, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			feedElements = new LinkedList<>();
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} finally {
			try {
				if (scan != null) {
					scan.close();
				}
			} catch (Exception e) {
				System.out.println("ioe: " + e.getMessage());
			}
		}
	}

	public void run() {
		try {
			while (true) {
				LinkItMessage message = (LinkItMessage) ois.readObject();
				System.out.println(message.getName() + "::::::: " + message.getMessage());
				String msg = message.getName() + " " + message.getMessage();
				// feed should only display most recent 3 elements
				feedElements.add(msg);
				if (feedElements.size() > 3) {
					feedElements.remove();
				}
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	public Queue<String> getFeedElements() {
		return feedElements;
	}

	public void sendMessage(LinkItMessage msg) {
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}

	public void clientWrite() {
		scan = new Scanner(System.in);
		String line = scan.nextLine();
		LinkItMessage message = new LinkItMessage("Prof. Miller", line);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
}