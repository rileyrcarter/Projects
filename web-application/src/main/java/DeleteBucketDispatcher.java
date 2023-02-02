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

@WebServlet("/DeleteBucketDispatcher")
public class DeleteBucketDispatcher extends HttpServlet {
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
		doPost(request, response);
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
		System.err.println("got username");

		// use bucket id to get information
		String bucketIdString = request.getParameter("id");
		int bucketId = -1;
		try {
			bucketId = Integer.parseInt(bucketIdString);
		} catch (NumberFormatException e) {
			System.err.println("error reading bucketId");
		}
		if (bucketId != -1) {
			Bucket b = SQLAgent.getBucket(bucketId);
			
			if (b.getUsername().equals(username)) {
				SQLAgent.deleteBucket(bucketId);
			} else {
				// wrong user
				System.err.println("Cannot delete");
			}
		}
		
		System.err.println("redirecting");
		
		//response.sendRedirect("profile.jsp");
		request.getRequestDispatcher("my_bucket_vis.jsp?id=" + bucketId).include(request, response);

	}
}
