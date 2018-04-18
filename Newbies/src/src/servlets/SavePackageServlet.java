package src.servlets;

import java.io.IOException;

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
 * Servlet implementation class SavePackageServlet
 */
@WebServlet("/SavePackageServlet")
public class SavePackageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SavePackageServlet() {
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
		String title = (String) request.getParameter("title");
		User user = (User) session.getAttribute("currUser");

		LinkItSql lsql = new LinkItSql();
		lsql.connect();

		int id = lsql.getPackageId(title);
		lsql.savePackage(user.getUserID(), id);

		String msg = " saved the package named " + title;
		LinkItMessage m = new LinkItMessage(user.getUsername(), msg);
		LinkItClient client = (LinkItClient) session.getAttribute("LinkItClient");
		client.sendMessage(m);
	}

}
