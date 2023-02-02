<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Roboto"
	rel="stylesheet" type="text/css">
<link rel="stylesheet" href="index.css">
<script src="https://kit.fontawesome.com/3204349982.js"
	crossorigin="anonymous"></script>
<%@ page import="java.net.URLDecoder"%>
<%
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
%>
</head>
<body>
	<!-- TODO -->
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
	<br>
	<h1>Link Bucket</h1>
	<br>
	<h2>Organize and share your links. All for free.</h2>
	<br>
	<div class="searchbar">
		<form action="ExploreDispatcher">
			<input type="text" placeholder="Explore Buckets..." name="search">
			<button type="submit">
				<i class="fa fa-search"></i>
			</button>
		</form>
	</div>
</body>
</html>