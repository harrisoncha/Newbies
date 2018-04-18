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
import src.linkit.LinkItSql;
import src.linkit.User;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignUpServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String userQuery = "SELECT * FROM LINKITUSER WHERE USERNAME=" + username;
		LinkItSql lsql = new LinkItSql();
		lsql.connect();
		email = email.replace("%40", "@");
		System.out.println(email);
		PrintWriter out = response.getWriter();
		if (lsql.userExists(username, 1)) {
			out.print("A user with this username already exists.");
		} else if (lsql.userExists(email, 2)) {
			out.print("A user with this email already exists.");
		} else {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setPassword(password);
			newUser.setUsername(username);
			lsql.insertUser(username, email, password);
			newUser.setUserID(lsql.getUserId(username));
			session.setAttribute("currUser", newUser);
			LinkItClient linkitClient = new LinkItClient("localhost", 6789);
			session.setAttribute("LinkItClient", linkitClient);
			out.print("entry_linkit.jsp");
		}
		out.flush();
		lsql.stop();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
