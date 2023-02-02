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

.simple_container {
	width: 90%;
	margin: auto;
}

.welcome_title {
	text-align: left;
}

#change_password > button {
	border: 1px solid black;
	color: #15185E;
	background-color: transparent;
	display: block;
	border: 3px outset #988FFF;
	font-size: 17px;
	border-radius: 15px 15px 15px 15px;
	color: #988FFF;
}

#password_form {
	display: none;
}

#password_form button {
	width: 100px;
	height: 40px;
	border: 3px outset #988FFF;
	font-size: 17px;
	border-radius: 15px 15px 15px 15px;
	color: #988FFF;
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
	
	// get cookie information
	String loginName = "";
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("loginname")) {
		loginName = URLDecoder.decode(cookie.getValue(), "UTF-8");
		//System.err.println("Before: " + loginName);
		//loginName = loginName.replaceAll("\\.", " ");
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
				out.print("<a class='active' href='#'>Profile</a>");
				out.print("<a href= 'create_bucket_vis.jsp'> Create </a>");
				out.print("<a href= 'my_bucket_vis.jsp'> My Buckets</a>");

			}
			%>
			<a  href='index.jsp'> Explore </a>
			<%
			if (!loginName.isEmpty()) {
				out.print("<a id = \"welcome\">  User: " + loginName + "</a>");
			}
			%>
		</nav>
	</header>
	
	<%
	Object messageObject = request.getAttribute("message");
	if (messageObject != null) {
		System.err.println("Message isnt null");
		String message = (String)request.getAttribute("message");
		out.print("<div class='simple_banner'>");
		out.print("<p>" + message + "</p>");
		out.print("</div>");
	}
	%>
	
	<div class='simple_container'>
		<h2 class='welcome_title'>Welcome to <% out.print(loginName); %>'s profile page</h2>
		
		<div id='change_password'>
			<h4>Password management</h4>
			<button id='change_password_button'>Change password</button>
			<form action='ChangePasswordDispatcher' method='POST' id='password_form'>
				<input type='email' class='textbox' name='email' placeholder='enter your email...'><br><br>
				<input type='password' class='textbox' name='password' placeholder='enter your new password'><br><br>
				<input type='password' class='textbox' name='password_confirm' placeholder='enter your password again'><br><br>
				<button type='submit'>Submit</button>
			</form>
		</div>
	</div>

	<script>
	document.getElementById("change_password_button").onclick = function() {
		var button = document.getElementById("change_password_button");
		button.style.display = "none";
		var form = document.getElementById("password_form");
		form.style.display = "block";
	}
	</script>

</body>
</html>