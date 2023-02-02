import javax.servlet.ServletException;

import Util.Helper;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import javax.servlet.ServletConfig;

import java.net.URLEncoder;

/**
 * Servlet implementation class RegisterDispatcher
 */

@WebServlet("/RegisterDispatcher")
public class RegisterDispatcher extends HttpServlet {
	@Serial
	private static final long serialVersionUID = 1L;
	private static final String url = "jdbc:mysql://localhost:3306/CSCI201_project_database";

	/**
	 * Default constructor.
	 */
	public RegisterDispatcher() {
	}

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
		String ema = request.getParameter("email");
		String na = request.getParameter("username");
		String pass = request.getParameter("password");
		String confirmpass = request.getParameter("confirmpassword");

		if (!pass.contentEquals(confirmpass)) {
			error += "<p>Passwords do not match. Please try again.</p>";
		} else if (!Helper.isValidEmail(ema)) {
			error += "<p>Invalid Email. Please try again.</p>";
		}
//		else if(!Helper.validName(na)) {
//			error += "<p>Invalid Name. Please try again.</p>";
//		}
		else {
			String sql = "SELECT COUNT(1) FROM Users where username = ?";
			try (Connection conn = DriverManager.getConnection(db, user, pwd);
					PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, na);
				ResultSet rs = ps.executeQuery();
				while (rs.next())
					if (rs.getInt(1) == 1) {
						error += "<p>Username already in use. Please try again.</p>";
					}

			} catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
			}

			sql = "SELECT COUNT(1) FROM Users where email = ?";
			try (Connection conn = DriverManager.getConnection(db, user, pwd);
					PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, ema);
				ResultSet rs = ps.executeQuery();
				while (rs.next())
					if (rs.getInt(1) == 1) {
						error += "<p>Email already in use. Please try again.</p>";
					}

			} catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
			}
		}

		if (error.equals("")) {
			String sql = "INSERT INTO Users(username, email, password) VALUES(? ,?, ?)";
			try (Connection conn = DriverManager.getConnection(db, user, pwd);
					PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, na);
				ps.setString(2, ema);
				//na = na.replaceAll("\\s", ".");
				ps.setString(3, pass);
				ps.executeUpdate();
			} catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
			}

			Cookie cookiename = new Cookie("loginname", URLEncoder.encode(na, "UTF-8"));
			cookiename.setMaxAge(-1);
			response.addCookie(cookiename);
			// response.sendRedirect("index.jsp");
			// request.getRequestDispatcher("index.jsp").forward(request, response);
			response.sendRedirect("index.jsp");

			// request.getRequestDispatcher("index.jsp").forward(request, response);
		} else {
			request.setAttribute("error", error);
			request.getRequestDispatcher("auth.jsp").include(request, response);
		}

	}

//	request.getParameters("password")==selectedSQL;

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
