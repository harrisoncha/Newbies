package src.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.LinkItServer.LinkItClient;

/**
 * Servlet implementation class RefreshFeedServlet
 */
@WebServlet("/RefreshFeedServlet")
public class RefreshFeedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RefreshFeedServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		LinkItClient client = (LinkItClient) session.getAttribute("LinkItClient");
		if (client != null) {
			Queue<String> q = client.getFeedElements();
			String feedHTML = "<li>";
			for (String m : q) {
				feedHTML += "<p>";
				feedHTML += m;
				feedHTML += "</p>";
			}
			feedHTML += "</li>";
			out.print(feedHTML);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
