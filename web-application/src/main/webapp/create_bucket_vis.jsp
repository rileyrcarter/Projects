<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta
	content="301645777112-2rlc9gth0f5d4reimjcm9bf0kj7ahec0.apps.googleusercontent.com"
	name="google-signin-client_id">
<title>Create Bucket</title>
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
<%@ page import="java.net.URLDecoder"%>
	<style>

#roundbox {
	border-radius: 25px;
	background: #15185E;
	width: 600px;
	margin: 0 auto;
	padding: 20px;
	font-size: 15px;
}

#roundbox input[type=text] {
	width: 300px;
	padding: 12px 20px;
	margin: 0px 0px;
	display: inline-block;
	border: 1px solid #ccc;
	box-sizing: border-box;
	border-radius: 4px;
}

#roundbox textarea {
	border: 1px solid #ccc;
	margin: 0px 0px;
	padding: 6px 20px;
	border-radius: 4px;
	max-width: 500px;
}

.link_block {
	width: 300px;
	margin: 0px 60px;
}

.details {
	margin: 20px 100px;
}

.desc {
	margin: 0px 100px;
	color: slateblue;
}

#roundbox input[type="button"], input[type="submit"] {
	border-radius: 10px;
	margin: auto;
	background-color: #CCCCFF;
	border: 2px solid white;
	color: white;
	width: 100px;
	padding: 1px 6px;
	height: 30px;
}

#create_bucket_title {
	color: slateblue;
	text-align: center;
}
</style>
</head>
<body>

		<%
		// get cookie
		Cookie[] cookies = request.getCookies();
		String loginName = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("loginname")) {
			//loginName = cookie.getValue();
			loginName = URLDecoder.decode(cookie.getValue(), "UTF-8");
			break;
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
				out.print("<a class='active' href= 'create_bucket_vis.jsp'> Create </a>");
				out.print("<a href= 'my_bucket_vis.jsp'> My Buckets</a>");

			}
			%>
			<a href='index.jsp'> Explore </a>
			<%
			if (!loginName.isEmpty()) {
				out.print("<a id = \"welcome\">  Hi " + loginName + "!</a>");
			}
			%>
		</nav>
	</header>
	<form action="CreateBucketDispatcher" method="POST"
		id="create_bucket_id" autocomplete="off">
		<h1 id="create_bucket_title">Create Bucket</h1>
		<div id="roundbox">
			<input type="text" name="bucket_name" placeholder="Bucket Name">
			<div class="link_block">
				<h3 style="color: rgb(122, 128, 238);">Link 1</h3>
				<input type="text" name="link1_name" placeholder="Name">
				<br>
				<br>
				<input type="text" name="link1" placeholder="url">
				<br>
				<br>
				<textarea name="link1_desc" placeholder="description..." rows="3"
					cols="50"></textarea>
			</div>
			<div class="link_block">
				<h3 style="color: rgb(122, 128, 238);">Link 2</h3>
				<input type="text" name="link2_name" placeholder="Name">
				<br>
				<br>
				<input type="text" name="link2" placeholder="url">
				<br>
				<br>
				<textarea name="link2_desc" placeholder="description..." rows="3"
					cols="50"></textarea>
			</div>
			<br id="last_break">
			<input type="button" id="addLink" value="Add Link">
			<input type="submit">
		</div>
	</form>
	<script>
		let linkIndex = 3;
		document.getElementById("addLink").onclick = function() {
			var block = document.createElement("div");
			block.classList.add("link_block");

			// header
			var header = document.createElement("h3");
			header.innerHTML = "Link " + linkIndex;
			header.style = "color: rgb(122, 128, 238);";
			block.appendChild(header);

			// name input 
			var inputName = document.createElement("input");
			inputName.type = "text";
			inputName.name = "link" + linkIndex + "_name";
			inputName.placeholder = "name";
			block.appendChild(inputName);
			block.appendChild(document.createElement("br"));
			block.appendChild(document.createElement("br"));

			// url input
			var inputLink = document.createElement("input");
			inputLink.type = "text";
			inputLink.name = "link" + linkIndex;
			inputLink.placeholder = "url";
			block.appendChild(inputLink);

			// breaks
			var inputBr = document.createElement("br");
			block.appendChild(inputBr);
			block.appendChild(document.createElement("br"));

			// description input
			var inputDesc = document.createElement("textarea");
			inputDesc.name = "link" + linkIndex + "_desc";
			inputDesc.placeholder = "description";
			inputDesc.rows = "3";
			inputDesc.cols = "50";
			block.appendChild(inputDesc);

			var form = document.getElementById("roundbox");
			var lastBreak = document.getElementById("last_break");

			form.insertBefore(block, lastBreak);

			linkIndex++;

		}
	</script>
</body>
</html>
