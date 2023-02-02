<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta
	content="301645777112-2rlc9gth0f5d4reimjcm9bf0kj7ahec0.apps.googleusercontent.com"
	name="google-signin-client_id">
<link href="https://fonts.googleapis.com" rel="preconnect">
<link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
<link href='https://fonts.googleapis.com/css?family=Roboto Condensed'
	rel="stylesheet">
<script crossorigin="anonymous"
	src="https://kit.fontawesome.com/3204349982.js"></script>
<script async defer src="https://apis.google.com/js/platform.js"></script>
<link href="index.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Roboto"
	rel="stylesheet" type="text/css">
<script src="https://apis.google.com/js/api:client.js"></script>
<title>My Buckets</title>
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
<%@ page import="java.net.URLDecoder"%>
</head>
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
	flex-wrap: nowrap;
}

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
	justify-content: start-flex;
	column-gap: 20px;
	row-gap: 20px;
	margin: 10px 0px;
	width: 90%;
	margin: 0 auto;
	font-size: 15px;
}

#list_block>a {
	margin: 0;
	padding: 0;
	text-decoration: none;
}

#my_buckets_title {
	color: slateblue;
	text-align: center;
	font-size: 40px;
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
</style>
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

	Cookie[] cookies = request.getCookies();
	String loginName = "";
	if (cookies != null) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("loginname")) {
		loginName = URLDecoder.decode(cookie.getValue(), "UTF-8");
		break;
			}
		}
	}
	if (loginName.equals("")) {
		System.err.println("username not found in buckets page");
	}
	ArrayList<Bucket> buckets = SQLAgent.getBuckets(loginName);

	System.err.println("buckets: " + buckets.size());
	%>
	<%
	String welcome = "LOG IN";
	%>
	<header>
		<nav class="navbar">
			<img class="logo" onclick="href" alt="LinkBucket" src="LOGO.png">
			<%
			if (loginName.isEmpty()) {
				out.print("<a href = 'auth.jsp' method = GET> Log In / Register </a>");
			} else {
				/* out.print("<a>  Hi " + loginName + "!</a>");   */
				out.print("<a href= 'LogoutDispatcher'> Log Out </a>");
				out.print("<a href='profile.jsp'>Profile</a>");
				out.print("<a href= 'create_bucket_vis.jsp'> Create </a>");
				out.print("<a class='active' href= 'my_bucket_vis.jsp'> My Buckets</a>");

			}
			%>
			<a href='index.jsp'> Explore </a>
			<%
			if (!loginName.isEmpty()) {
				out.print("<a id = \"welcome\">  User: " + loginName + "</a>");
			}
			%>
		</nav>
	</header>
	<h1 id="my_buckets_title">My Buckets</h1>
	<div id="list_block">
		<!-- UNCOMMENT IF YOU WANT TO SEE BUCKET STYLING
		<a href='#' class='bucketbox'>
			<span class='bucket_name'>Bucket name placeholder</span> <span><img
					src="LOGO.png"></span>
		</a>
		<a href='#' class='bucketbox'>
			<span class='bucket_name'>Short</span> <span><img
					src="LOGO.png"></span>
		</a>
		-->
		<%
		for (Bucket b : buckets) {
			out.print("<a href='temp_bucket_details.jsp?id=" + b.getBucketId());
			out.print("' class='bucketbox'> <span class='bucket_name'>" + b.getBucketName());
			out.print("</span><span><img src='LOGO.png'></span></a>");
		}
		%>
	</div>
</body>
</html>