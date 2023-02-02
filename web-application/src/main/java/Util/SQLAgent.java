package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLAgent {
	private static String url = Constants.DBConnect;
	private static String user = Constants.DBUserName;
	private static String pwd = Constants.DBPassword;
	
	/*
	 * pass in bucket with only bucketName and likes filled, id does not matter, and username
	 * returns created bucket id, return -1 if failed to create bucket
	 */
	public static synchronized int addBucket(Bucket b, String username) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		int bucketId = -1;

		String sql = "INSERT INTO Buckets (username, bucket_name, likes) VALUES (?, ?, 0);";

		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, username);
			ps.setString(2, b.getBucketName());
			int row = ps.executeUpdate();

			if (row == 1) {
				ResultSet rs = ps.getGeneratedKeys();
				rs.next();
				bucketId = rs.getInt(1); // get bucket_id
				System.err.println("bucket_id: " + bucketId);
			}
			System.err.println("executed add bucket");
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("did not add bucket execute");
			return bucketId;
		}
		
		return bucketId;
	}
	
	/*
	 * pass in bucket with id and username
	 * for now just updates everything, even if already the same
	 */
	public static synchronized int updateBucket(Bucket b, String username) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}

		String sql = "UPDATE Buckets SET likes=? WHERE bucket_id=?";

		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setInt(1, b.getLikes());
			ps.setInt(2, b.getBucketId());
			ps.executeUpdate();

			System.err.println("executed update bucket");
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("did not execute update bucket");
			return -1;
		}
		
		return 0;
	}
	
	/*
	 * pass in link info and bucketId
	 * need every part of link but id
	 */
	public static synchronized int addLink(Link l, int bucketId) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		String sql = "INSERT INTO Links (bucket_id, link_name, link, description) VALUES (?, ?, ?, ?);";

		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setInt(1, bucketId);
			ps.setString(2, l.getLinkName());
			ps.setString(3, l.getLink());
			ps.setString(4, l.getDescription());
			ps.executeUpdate();

			System.err.println("executed add link");
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("did not execute add link");
			return -1;
		}
		
		return 0;
	}
	
	public static synchronized ArrayList<Bucket> getBuckets(String username) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();
		// only need bucket ids and bucket names, from username
		String sql = "SELECT bucket_id, bucket_name, likes FROM Buckets b WHERE b.username = ?;";
		
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
			PreparedStatement ps = conn.prepareStatement(sql);) {
	    	ps.setString(1, username);
		 	ResultSet rs = ps.executeQuery();
		 	while (rs.next()) {
		 		buckets.add(new Bucket(rs.getInt("bucket_id"), rs.getString("bucket_name"), rs.getInt("likes"), username));
		 	}
		 } catch (SQLException sqle) {
		 	System.err.println("SQLException: " + sqle.getMessage());
		 }
		return buckets;
	}
	
	/*
	 * pass in links and bucketId
	 * does not need linkId
	 */
	public static synchronized int addLinks(List<Link> links, int bucketId) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		String sql = "INSERT INTO Links (bucket_id, link_name, link, description) VALUES (?, ?, ?, ?);";

		for (Link l : links) {
			try (Connection conn = DriverManager.getConnection(url, user, pwd);
					PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
				ps.setInt(1, bucketId);
				ps.setString(2, l.getLinkName());
				ps.setString(3, l.getLink());
				ps.setString(4, l.getDescription());
				ps.executeUpdate();
	
				System.err.println("executed update bucket");
			} catch (SQLException sqle) {
				System.err.println("SQLException: " + sqle.getMessage());
				System.err.println("did not execute update bucket");
				return -1;
			}
		}
		
		return 0;
	}
	
	/*
	 * pass in bucketId
	 * return list of links
	 */
	public static synchronized ArrayList<Link> getLinks(int bucketId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		ArrayList<Link> links = new ArrayList<Link>();
		
		// use bucket id to get information
		if (bucketId != -1) {
			// use bucket id to get bucket name, links, link descriptions
			
			// get links
			String sql = "SELECT link_id, link, link_name, description FROM Links WHERE bucket_id = ?";
			
			try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql);) {
			   	ps.setInt(1, bucketId);
			 	ResultSet rs = ps.executeQuery();
			 	while (rs.next()) {
			 		links.add(new Link(rs.getInt("link_id"), rs.getString("link"), rs.getString("link_name"), rs.getString("description")));
			 	}
			 } catch (SQLException sqle) {
			 	System.err.println("SQLException: " + sqle.getMessage());
			 }
			
			System.err.println("links found: " + links.size());
			
		}
		
		return links;
	}
	
	public static synchronized Bucket getBucket(int bucketId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		String sql = "SELECT bucket_name, likes, username FROM Buckets WHERE bucket_id = ?";
		Bucket b = null;
		
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
			PreparedStatement ps = conn.prepareStatement(sql);) {
	    	ps.setInt(1, bucketId);
		 	ResultSet rs = ps.executeQuery();
		 	if (rs.next()) {
		 		b = new Bucket(bucketId, rs.getString("bucket_name"), rs.getInt("likes"), rs.getString("username"));
		 	}
		 } catch (SQLException sqle) {
		 	System.err.println("SQLException: " + sqle.getMessage());
		 }
		return b;
	}
	
	public static synchronized int updateLink(Link l, int bucketId) {
		return -1;
	}
	
	public static synchronized ArrayList<Bucket> searchBuckets(String searchQuery) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();

    	String sql = "SELECT * FROM Buckets WHERE bucket_name LIKE ? ORDER BY likes, bucket_name";
        
    	 try(Connection conn=DriverManager.getConnection(url, user, pwd);
	          		PreparedStatement ps = conn.prepareStatement(sql);){
    		 ps.setString(1, "%" + searchQuery + "%");
    		 ResultSet rs = ps.executeQuery();
    		 
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
	}
	
	
	
	/*
	 *  Edit information in the buckets
	 */
	public static synchronized int editLink(Link l){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		String sql = "UPDATE Links SET link_name=?, link=?, description=? WHERE link_id=?";

		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
//			ps.setInt(1, bucketId);
			ps.setString(1, l.getLinkName());
			ps.setString(2, l.getLink());
			ps.setString(3, l.getDescription());
			ps.setInt(4, l.getLinkId());
			ps.executeUpdate();

			System.err.println("executed editLink");
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("did not execute editLink");
			return -1;
		}
		
		return 0;
	}
	
	public static synchronized int editBucket(Bucket bucket) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		String sql = "UPDATE Buckets SET bucket_name=? WHERE bucket_id=?";
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, bucket.getBucketName());
			ps.setInt(2, bucket.getBucketId());
			ps.executeUpdate();

			
			
			System.err.println("executed editBucket");
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("did not execute editBucket");
			return -1;
		}
		
		
		return 0;
	}
	
	public static String getEmail(String username) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		String sql = "SELECT email FROM Users WHERE username=?";
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return rs.getString("email");
			}
			System.err.println("Did not find email");
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("failed to get email");
		}
		return "";
		
	}
	
	public static int changePassword(String username, String password) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		String sql = "UPDATE Users SET password=? WHERE username=?";
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, password);
			ps.setString(2, username);
			
			ps.executeUpdate();
			
			System.err.println("Successfully changed password");
			return 0;
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("failed to change password");
		}
		return -1;
		
	}
	
	public static synchronized int deleteBucket(int bucketId) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		
		// delete links
		String sql = "DELETE FROM Links WHERE bucket_id=?";
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setInt(1, bucketId);
			
			ps.executeUpdate();
			
			System.err.println("Successfully deleted links");
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("failed to delete links");
		}
		
		sql = "DELETE FROM Buckets WHERE bucket_id=?";
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setInt(1, bucketId);
			
			ps.executeUpdate();
			
			System.err.println("Successfully deleted bucket");
			return 0;
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("failed to delete bucket");
		}
		
		return -1;
		
		
	}
	
	public static synchronized int deleteLink(int linkId) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException: " + e.getMessage());
		}
		

		// delete links
		String sql = "SELECT * FROM Links WHERE link_id=?";
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setInt(1, linkId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
			} else {
				System.err.println("link already deleted");
				return -1;
			}
			
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("failed to delete link");
		}
		
		
		// delete links
		sql = "DELETE FROM Links WHERE link_id=?";
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setInt(1, linkId);
			
			ps.executeUpdate();
			
			System.err.println("Successfully deleted link");
			return 0;
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
			System.err.println("failed to delete link");
		}
		return -1;
	}
}
