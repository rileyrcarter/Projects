import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletConfig;
import java.net.URLEncoder;


/**
 * Servlet implementation class LoginDispatcher
 */
@WebServlet("/LoginDispatcher")
public class LoginDispatcher extends HttpServlet {
	@Serial
	private static final long serialVersionUID = 1L;
	private static final String url = "jdbc:mysql://localhost:3306/CSCI201_project_database";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}

		response.setContentType("text/html");

		String db = url;
		String user = "root";
		String pwd = "root";

		String error = "";
		String na = request.getParameter("username");
		String pass = request.getParameter("password");
		String name = "";

		String sql = "SELECT * FROM Users where username = ?";
		try (Connection conn = DriverManager.getConnection(db, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, na);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (!rs.getString(3).contentEquals(pass) || rs.getString(3).contentEquals("[CSCI201]")) {
					error += "<p>Invalid email or password. Or, bad Google login. Please try again.</p>";
				}
//				else {
//					name = rs.getString(3);
//				}
			}
			else {
				error += "<p>Invalid email or password. Or, bad Google login. Please try again.</p>";
			}

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}

		if (error.equals("")) {
			Cookie cookiename = new Cookie("loginname", URLEncoder.encode(na, "UTF-8"));
			cookiename.setMaxAge(-1);
			response.addCookie(cookiename);
			//response.sendRedirect("index.jsp");
			//request.getRequestDispatcher("index.jsp").forward(request, response);
			response.sendRedirect("index.jsp");
		} else {
			request.setAttribute("error", error);
			request.getRequestDispatcher("auth.jsp").include(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
