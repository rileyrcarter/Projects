<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search</title>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="Util.Constants"%>
<%@ page import="Util.Bucket"%>
<%@ page import="Util.SQLAgent"%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
	rel="stylesheet">
<link rel="stylesheet" href="index.css">
<script src="https://kit.fontawesome.com/3204349982.js"
	crossorigin="anonymous"></script>
<style>
.bucketbox {
	position: relative;
	border: 1px;
	border-radius: 25px;
	background: #15185E;
	width: 200px;
	height: 150px;
	color: white;
	text-align: center;
	font-size: 15px;
	flex-wrap: nowrap;
}

/*** link icon dimensions, 65.95 x 50 ***/
.bucketbox img {
	height: 50px;
	box-sizing: border-box;
	position: absolute;
	bottom: 0;
	left: 67px;
}

.bucketbox h2 {
	text-align: center;
}

#list_block {
	display: flex;
	flex-wrap: wrap;
	justify-content: flex-start;
	column-gap: 20px;
	row-gap: 20px;
	margin: 10px 0px;
	width: 90%;
	margin: 0 auto;
}

#list_block>a {
	margin: 0;
	padding: 0;
	text-decoration: none;
}

#my_buckets_title {
	color: slateblue;
	text-align: center;
}

.bucket_name {
	display: block;
	font-size: 1.5em;
	margin-top: 0.83em;
	margin-bottom: 0.83em;
	margin-left: 0;
	margin-right: 0;
	font-weight: bold;
	text-decoration: none;
}

.searchbar {
	width: 90%;
	text-align: center;
	margin: auto;
}

.page_title {
	width: 90%;
	margin: auto;
	font-size: 25px;
}

.page_title span {
	font-style: italic;
}

.divider {
	margin: 10px auto;
}

</style>

</head>
<body>
	<!-- TODO -->
	<%
	//perform search
	final String url = "jdbc:mysql://localhost:3306/CSCI201_project_database";
	final String user = "root";
	final String pwd = "root";

	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.err.println("ClassNotFoundException: " + e.getMessage());
	}
	String search_q = "";
	ArrayList<Bucket> bucketlist = new ArrayList<Bucket>();

	search_q = (String) request.getParameter("search");

	bucketlist = SQLAgent.searchBuckets(search_q);
	
	// get cookie information
	String loginName = "";
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (Cookie aCookie : cookies) {
			if (aCookie.getName().equals("loginname")) {
		loginName = URLDecoder.decode(aCookie.getValue(), "UTF-8");
		//System.err.println("Before: " + loginName);
		//oginName = loginName.replaceAll("\\.", " ");
		//System.err.println("Result: " + loginName);
			}
		}
	}
	%>
				
				
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
	
	
	<div class="searchbar">
		<br><br>
		<form action="ExploreDispatcher">
			<input type="text" placeholder="Explore Buckets..." name="search" 
			<% if (!search_q.equals("")) out.print(String.format("value=\"%s\"", search_q)); %>>
			<button type="submit">
				<i class="fa fa-search"></i>
			</button>
		</form>
	</div>
	
	<hr class='divider'>
	
	<div class="page_title">

		<p>
			Showing results for 
			<% out.print("<span>" + search_q + "</span>"); %>
		</p>
	</div>

	<div id='list_block'>
		<%
		for (Bucket b : bucketlist) {
			out.print("<a href='temp_bucket_details.jsp?id=" + b.getBucketId());
			out.print("' class='bucketbox'> <span class='bucket_name'>" + b.getBucketName());
			out.print("</span><span><img src='LOGO.png'></span></a>");
		}
		%>
	</div>

</body>
</html>