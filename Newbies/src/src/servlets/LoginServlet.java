package src.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		email = email.replace("%40", "@");
		LinkItSql lsql = new LinkItSql();
		lsql.connect();
		PrintWriter out = response.getWriter();
		if (lsql.userExists(email, 2)) {
			ResultSet rs = lsql.queryEmail(email);
			int uid = 0;
			String username = "";
			String pwd = "";
			try {
				while (rs.next()) {
					uid = rs.getInt(1);
					username = rs.getString(2);
					pwd = rs.getString(3);
					email = rs.getString(4);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (pwd.equals(password)) {
				User currUser = new User();
				currUser.setUsername(username);
				currUser.setEmail(email);
				currUser.setPassword(password);
				currUser.setUserID(uid);
				session.setAttribute("currUser", currUser);
				LinkItClient linkitClient = new LinkItClient("localhost", 6789);
				session.setAttribute("LinkItClient", linkitClient);
				out.print("entry_linkit.jsp");
			} else {
				out.print("Invalid password!");
			}
		} else {
			out.print("A user with this email does not exist.");
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
