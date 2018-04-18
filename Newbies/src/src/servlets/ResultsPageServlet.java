package src.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.LinkItServer.LinkItClient;
import src.LinkItServer.LinkItMessage;
import src.linkit.LinkItSql;
import src.linkit.User;

/**
 * Servlet implementation class ResultsPageServlet
 */
@WebServlet("/ResultsPageServlet")
public class ResultsPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResultsPageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		String up = (String) request.getParameter("up");
		User user = (User) session.getAttribute("currUser");
		String p = request.getParameter("package");

		LinkItSql lsql = new LinkItSql();
		lsql.connect();

		String msg = "";
		if (up.equals("true")) {
			lsql.addVote(user.getUserID(), Integer.parseInt(p), true);
			msg = "up voted package \"" + lsql.getPackageName(Integer.parseInt(p)) + "\"";
		} else {
			lsql.addVote(user.getUserID(), Integer.parseInt(p), false);
			msg = "down voted package \"" + lsql.getPackageName(Integer.parseInt(p)) + "\"";
		}

		PrintWriter out = response.getWriter();
		out.print(lsql.getPackageRating(Integer.parseInt(p)));
		out.flush();
		lsql.stop();

		LinkItMessage m = new LinkItMessage(user.getUsername(), msg);
		LinkItClient client = (LinkItClient) session.getAttribute("LinkItClient");
		client.sendMessage(m);
	}
}
