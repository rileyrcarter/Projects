import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Util.Constants;
import Util.SQLAgent;
import Util.Bucket;
import Util.Link;

import java.io.IOException;
import java.io.Serial;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;

@WebServlet("/ChangePasswordDispatcher")
public class ChangePasswordDispatcher extends HttpServlet {
	private static final String url = Constants.DBConnect;
	final String user = Constants.DBUserName;
	final String pwd = Constants.DBPassword;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}

		// add bucket using username cookie
		
		// get username
		String username = "";
		String message = "";
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("loginname")) {
				username = URLDecoder.decode(cookie.getValue(), "UTF-8");
				break;
			}
		}
		
		
		String inputEmail = request.getParameter("email");
		String inputPassword = request.getParameter("password");
		String inputConfirm = request.getParameter("password_confirm");
		
		String email = SQLAgent.getEmail(username);
		
		int success = 0;
		if (email.equals(inputEmail) && inputPassword.equals(inputConfirm)) {
			success = SQLAgent.changePassword(username, inputPassword);
		} else {
			message += "Email was not accurate or passwords did not match.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("profile.jsp").include(request, response);
			return;
		}
		
		if (success == -1) {
			message += "Error with inserting into SQL.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("profile.jsp").include(request, response);
			return;
		}
		
		System.err.println("redirecting");
		
		//response.sendRedirect("profile.jsp");
		message = "Successfully changed passwords.";
		request.setAttribute("message", message);
		request.getRequestDispatcher("profile.jsp").include(request, response);

	}
}
