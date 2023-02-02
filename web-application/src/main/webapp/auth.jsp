<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login/Register Page</title>
<link rel="stylesheet" href="./index.css">
<%@ page import="java.net.URLDecoder"%>
</head>
<header>
	<nav class="navbar">
		<img class="logo" alt="LinkBucket" src="LOGO.png" onclick='href='''>
		<a class="active" href='auth.jsp'> Log in / Register </a>
		<a href='index.jsp'> Explore </a>
	</nav>
</header>
<body>
	<div style="color: purple">
		<%
		String er = (String) request.getAttribute("error");
		if (er != null)
			out.print("<p>" + er + "</p>");
		%>
	</div>
	<%
	String ema = (String) request.getParameter("email");
	if (ema == null)
		ema = "";

	String na = (String) request.getParameter("name");
	if (na == null)
		na = "";

	String pass = (String) request.getParameter("password");
	if (pass == null)
		pass = "";

	String confirmpass = (String) request.getParameter("confirmpassword");
	if (confirmpass == null)
		confirmpass = "";
	%>
	<h1>Welcome To LinkBucket</h1>
	<h2>Sign in if a returning user or register a new account.</h2>
	<div class="columncontainer">
		<div class="LogInColumn">
			<h3>Sign In</h3>
			<form action="LoginDispatcher" method="GET">
				<!-- 				<label for="email">Email</label>
 -->
				<input class="textbox" type="text" name="username"
					placeholder="Username" required>
				<br>
				<!-- 				<label for="password">Password</label>
 -->
				<br>
				<input class="textbox" type="password" name="password"
					placeholder="Password" required>
				<br>
				<br>
				<button class="loginbutton" type="submit">Log In</button>
				<br>
				<!-- 				<button class="googlebutton" type="submit">
					<i class='fa fa-Google' style="font-size: 20px"> Sign In With
						Google</i>
				</button> -->
			</form>
			<!-- data-onsuccess="onSignIn" data-width="534" -->
			<div id="my-signin2"></div>
			<script>
				function onSuccess(googleUser) {
					console.log("OnSuccess");
					var profile = googleUser.getBasicProfile();
					let gname = profile.getName();
					let gmail = profile.getEmail();
					let tempgname = gname.replace(' ', '.');
					let newgname = tempgname.replace(' ', '.');
					let cookie = "loginname=" + newgname;
					let aCookie = "gmail=" + gmail;
					document.cookie = cookie;
					document.cookie = aCookie;
					var auth2 = gapi.auth2.getAuthInstance();
					auth2.disconnect();
					location.href = 'index.jsp';
				}
				function onFailure(error) {
					console.log(error);
					/* 						let cookies = "gError=Bad+Google+Login";
					 document.cookie = cookies; */
					location.href = 'auth.jsp';
				}
				function renderButton() {
					gapi.signin2.render('my-signin2', {
						'scope' : 'profile email',
						'width' : 534,
						'height' : 40,
						'longtitle' : true,
						'theme' : 'dark',
						'onsuccess' : onSuccess,
						'onfailure' : onFailure,
					});
				}
			</script>
			<script
				src="https://apis.google.com/js/platform.js?onload=renderButton"
				async defer></script>
		</div>
		<div class="RegisterColumn">
			<h3>Create Account</h3>
			<form action="RegisterDispatcher" method="GET">
				<!-- 				<label for="email">Email</label>
 -->
				<input class="textbox" type="email" name="email" placeholder="Email"
					required>
				<br>
				<!-- 				<label for="name">Name</label>
 -->
				<br>
				<input class="textbox" type="text" name="username"
					placeholder="Username" required>
				<br>
				<!-- 				<label for="password">Password</label>
 -->
				<br>
				<input class="textbox" type="password" name="password"
					placeholder="Password" required>
				<br>
				<!-- 				<label for="confirmpassword">Confirm Password</label>
 -->
				<br>
				<input class="textbox" type="password" name="confirmpassword"
					placeholder="Confirm Password" required>
				<br>
				<br>
				<button class="loginbutton" type="submit">Sign Up</button>
			</form>
		</div>
	</div>
</body>
</html>