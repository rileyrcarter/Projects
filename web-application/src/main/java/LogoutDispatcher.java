import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;


import javax.servlet.ServletException;
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

import java.net.URLDecoder;
/**
 * Servlet implementation class LogoutDispatcher
 */
@WebServlet("/LogoutDispatcher")
public class LogoutDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
	private static final String url = "jdbc:mysql://localhost:3306/CSCI201_project_database";

    /**
     * @throws ServletException 
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    	
    	String gName = "";
    	String gMail = "";
		String db = url;
		String user = "root";
		String pwd = "root";
    	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		//System.err.println("LOGOUT DISPATCHER");
    	Cookie[] cookies = request.getCookies();
    	if (cookies != null) {
    		for (Cookie aCookie : cookies) {
    			if (aCookie.getName().equals("loginname")) {
    				gName = URLDecoder.decode(aCookie.getValue(), "UTF-8");
					aCookie.setMaxAge(0);
					response.addCookie(aCookie);
					break;
    			}
    		}
    	}
    	
    	if (cookies != null) {
    		for (Cookie aCookie : cookies) {
    			if (aCookie.getName().equals("gmail")) {
    				gMail = URLDecoder.decode(aCookie.getValue(), "UTF-8");
    				String sql = "INSERT IGNORE INTO Users(email, password, name) VALUE(?, ?, ?)";
    				try (Connection conn = DriverManager.getConnection(db, user, pwd);
    						PreparedStatement ps = conn.prepareStatement(sql);) {
    					ps.setString(1, gMail);
    					ps.setString(2, "[CSCI201]");
    					ps.setString(3, gName);
    					ps.executeUpdate();
    				} catch (SQLException ex) {
    					System.out.println("SQLException: " + ex.getMessage());
    				}
    				
    				
					aCookie.setMaxAge(0);

					response.addCookie(aCookie);
					
					break;
    			}
    		}
    	}
    	
    	//request.getRequestDispatcher("index.jsp").forward(request, response);
    	response.sendRedirect("index.jsp");
    }

    /**
     * @throws ServletException 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request, response);
    }

}
