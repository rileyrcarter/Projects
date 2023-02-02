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

@WebServlet("/EditBucketDispatcher")
public class EditBucketDispatcher extends HttpServlet {
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
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("loginname")) {
				username = URLDecoder.decode(cookie.getValue(), "UTF-8");
				break;
			}
		}

		
		//search by bucketId
		int bucketId = Integer.parseInt(request.getParameter("bucket_id"));		
		
		Bucket b = SQLAgent.getBucket(bucketId);
		
		if (!b.getUsername().equals(username)) {
			System.err.println("Error: user editing bucket is not bucket creator");
			response.sendRedirect("temp_bucket_details.jsp?id=" + bucketId);
			return;
		}
		
		String bucketName = request.getParameter("bucket_name");
		int i = 1;
		while (request.getParameter("link" + i) != null) {
			String link1 = request.getParameter("link" + i);
			String link1Name = request.getParameter("link" + i + "_name");
			String link1Desc = request.getParameter("link" + i + "_desc");
			int linkId = Integer.parseInt(request.getParameter("link" + i + "_id"));
			System.err.println("desc: " + link1Desc);
			Link link = new Link(linkId, link1, link1Name, link1Desc);
			
			if (linkId == -1) {
				// only add link since did not exist before
				if (!link1.equals("") && !link1Name.equals("")) {
					SQLAgent.addLink(link, bucketId);
				}
			} else {
				SQLAgent.editLink(link);
			}
			
			i++;
		}		
		
		System.err.println("attempting testing getParameter");
		System.err.println("testing: " + request.getParameter("trash string"));

		System.err.println("redirecting");
		response.sendRedirect("temp_bucket_details.jsp?id=" + bucketId);

	}
}
