import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;

import java.util.*;
/**
 * Servlet implementation class SearchDispatcher
 */

@WebServlet("/ExploreDispatcher")
public class ExploreDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ExploreDispatcher() {
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO
    	
    	response.setContentType("text/html");
    	
    	//get form data from explore query
    	String search_query = (String)request.getParameter("search");
    	
    	//update request attribute
    	request.setAttribute("search", search_query);
   	
    	//forward request & redirect to results page
    	request.getRequestDispatcher("results.jsp").forward(request, response);
    	
    	
    	//CODE TO ADD TO RESULTS.JSP!!!
    	//ArrayList<Bucket> bucket_list = SQLAgent.getBucketsResults(search_query);
    	
    	
    	//CODE TO ADD TO SQLAgent!!!!!!!
    	//getBucketsResults(String) implementation
  
   /*
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();

    	String sql = "SELECT* FROM Buckets WHERE Buckets.bucket_name LIKE ? ORDER BY bucket_name";
        
    	 try(Connection conn=DriverManager.getConnection(url, user, pwd);
	          		PreparedStatement ps = conn.prepareStatement(sql);){
    		 ps.setString(1, "%" + search_query + "%");
    		 ResultSet rs = ps.executeQuery(sql);
    		 
    		 //parse result set, instantiate buckets
    		 while(rs.next()) {
    			 buckets.add(new Bucket(rs.getInt("bucket_id"), rs.getString("bucket_name"), rs.getInt("likes")));
    		 }
    	 }
    	 catch (SQLException e)
    	 {
    		 System.err.println("SQLException: " + e.getMessage());
    	 }
    	 return buckets;
   */ 	 
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