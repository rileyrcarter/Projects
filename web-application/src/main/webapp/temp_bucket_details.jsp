<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="index.css">
<title>Bucket Details</title>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="Util.Constants"%>
<%@ page import="Util.Bucket"%>
<%@ page import="Util.Link"%>
<%@ page import="Util.SQLAgent"%>

<style>

#links_block {
	width: 100%;
	text-align: center;
	display: flex;
	flex-direction: column;
	align-items: center;
	row-gap: 20px;
}

.link_box {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	width: 90%;
	text-align: left;
	color: white;
	background-color: #15185E;
	border-radius: 10px;
	cursor: pointer;
	z-index: 0;
}

.link_box p {
	margin: 10px;
}

.link_box_id {
	visibility: collapse;
}

.link_box_desc {
	font-size: 15px;
}

.link_box_link, .link_box_link a {
	font-size: 10px;
	color: gray;
	text-decoration: none;
	margin: 0;
	padding: 0;
	line-height: 20%;
}

.link_box_name {
	float: left;
}

.link_box div {
	clear: both;
}

.delete_link_button {
	z-index: 3;
	height: 30px;
	text-decoration: none;
	color: white;
}

.right_block {
	padding: 25px;
}

#edit_button {
	text-decoration: none;
	text-align: center;
	border-radius: 10px;
	background-color: #15185E;
	border: 2px solid white;
	color: white;
	width: 100px;
	padding: 1px 6px;
	height: 30px;
	float: right;
}

#delete_bucket_button {
	text-decoration: none;
	text-align: center;
	border-radius: 10px;
	background-color: #15185E;
	border: 2px solid white;
	color: white;
	width: 150px;
	padding: 1px 6px;
	height: 30px;
	float: right;
}

.back_button {
	text-decoration: none;
	text-align: center;
	border-radius: 10px;
	background-color: #15185E;
	border: 2px solid white;
	color: white;
	width: 100px;
	padding: 1px 6px;
	height: 30px;
	float: left;
}

#bucket_title {
}

#button_container {
	width: 90%;
	text-align: center;
	margin: auto;
}


</style>

</head>
<body>
	<%
	final String url = Constants.DBConnect;
	final String user = Constants.DBUserName;
	final String pwd = Constants.DBPassword;

	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.err.println("ClassNotFoundException: " + e.getMessage());
	}

	String loginName = "";
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (Cookie aCookie : cookies) {
			if (aCookie.getName().equals("loginname")) {
		loginName = URLDecoder.decode(aCookie.getValue(), "UTF-8");
		//System.err.println("Before: " + loginName);
		//loginName = loginName.replaceAll("\\.", " ");
		//System.err.println("Result: " + loginName);
			}
		}
	}

	Bucket bucket = new Bucket();
	int bucketId = -1;
	ArrayList<Link> links = new ArrayList<Link>();

	// use bucket id to get information
	String bucketIdString = request.getParameter("id");
	try {
		bucketId = Integer.parseInt(bucketIdString);
	} catch (NumberFormatException e) {
		System.err.println("error reading bucketId");
	}
	if (bucketId != -1) {
		links = SQLAgent.getLinks(bucketId);
		bucket = SQLAgent.getBucket(bucketId);

	}
	%>

	<!-- Header -->
	<header>
		<nav class="navbar">
			<img class="logo" alt="LinkBucket" src="LOGO.png">
			<%
			if (loginName.isEmpty()) {
				out.print("<a href = 'auth.jsp' method = GET> Log In / Register </a>");
			} else {
				/* out.print("<a>  Hi " + loginName + "!</a>");   */
				out.print("<a href= 'LogoutDispatcher'> Log Out </a>");
				out.print("<a href='profile.jsp'>Profile</a>");
				out.print("<a href= 'create_bucket_vis.jsp'> Create </a>");
				out.print("<a href= 'my_bucket_vis.jsp'> My Buckets</a>");

			}
			%>
			<a class="active" href='index.jsp'> Explore </a>
			<%
			if (!loginName.isEmpty()) {
				out.print("<a id = \"welcome\">  User: " + loginName + "</a>");
			}
			%>
		</nav>
	</header>
	<%
	out.print("<h2 id='bucket_title'>" + bucket.getBucketName() + "</h2>");
	out.print("<div id='button_container'>");
	out.print("<a class='back_button' href='javascript:history.back()'>Back</a>");
	// only output edit button if user is owner
	if (bucket.getUsername().equals(loginName)) {
		out.print("<a id='edit_button' href='details.jsp?id=" + bucketId + "'>Edit</a>");
		out.print("<a id='delete_bucket_button' href='DeleteBucketDispatcher?id=" + bucketId + "' method='POST'>Delete Bucket</a>");
	}
	
	out.print("<br><br></div>");
	
	
	%>
	
	<div id='links_block'>
		<%
		for (Link l : links) {
			out.print(String.format("<div class='link_box' onclick=\"window.open('%s', '_blank');\">", l.getClickLink()));
			out.print(String.format("<div class='left_block'><p class='link_box_name'>%s</p>", l.getLinkName()));
			out.print(String.format("<p class='link_box_id' hidden='hidden'>Link id: %s</p>", l.getLinkId()));
			out.print(String.format("<br><br><p class='link_box_link'><a href='%s'>%s</a></p>", l.getLink(), l.getLink()));
			out.print(String.format("<p class='link_box_desc'>%s</p></div>", l.getDescription()));
			out.print("<div class='right_block'>");
			if (bucket.getUsername().equals(loginName)) {
				out.print(String.format("<a class='delete_link_button' href='DeleteLinkDispatcher?id=" + bucketId + "&lid=" + l.getLinkId() + "' onclick=\"event.stopPropagation()\">Delete link</button>"));
			}
			out.print("</div></div>");
		}
		

		%>
		
		
	</div>

</body>
</html>