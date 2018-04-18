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
 * Servlet implementation class CreatePackageServlet
 */
@WebServlet("/CreatePackageServlet")
public class CreatePackageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreatePackageServlet() {
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
		String msg = "";
		HttpSession session = request.getSession(true);
		User currUser = (User) session.getAttribute("currUser");
		String packagename = request.getParameter("packagename");
		int userID = currUser.getUserID();
		LinkItSql lsql = new LinkItSql();
		lsql.connect();
		PrintWriter out = response.getWriter();

		String bio = request.getParameter("bio");
		String tag = request.getParameter("tag");
		lsql.insertPackage(userID, packagename, bio);
		int packageId = lsql.getPackageId(packagename);
		lsql.insertTag(packageId, tag);
		for (int i = 0; i < 10; i++) {
			String link = request.getParameter("link" + i);
			System.out.println(link);
			if (link.startsWith("www")) {
				link = "http://" + link;
			}
			if (!link.equals("")) {
				lsql.insertLink(packageId, link);
			}
		}
		out.print("ViewPackage.jsp?p=" + packagename);
		out.flush();
		lsql.stop();

		msg = " created a package named " + packagename;
		LinkItMessage m = new LinkItMessage(currUser.getUsername(), msg);
		LinkItClient client = (LinkItClient) session.getAttribute("LinkItClient");
		client.sendMessage(m);
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
