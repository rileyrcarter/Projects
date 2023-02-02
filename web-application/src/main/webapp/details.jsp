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
	color: rgb(122, 128, 238);
}

#roundbox p {
	margin-bottom: 0;
	color: rgb(122, 128, 238);
}

.link_block h3 {
	color: rgb(122, 128, 238);
}

.details {
	margin: 20px 100px;
}

.desc {
	margin: 0px 100px;
	color: slateblue;
}

#roundbox input[type="button"], input[type="submit"], button[type="submit"]
	{
	border-radius: 10px;
	margin: auto;
	background-color: #CCCCFF;
	border: 2px solid white;
	color: white;
	width: 100px;
	padding: 1px 6px;
	height: 30px;
}

#edit_bucket_title {
	color: slateblue;
	text-align: center;
}
</style>

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
</head>
<body>
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

	<form action='EditBucketDispatcher' method='POST' id="edit_bucket_id"
		autocomplete="off">
		<h1 id='edit_bucket_title'>Edit Bucket</h1>
		<div id='roundbox'>
			<%
			out.print("<p>Bucket Name:</p>");
			out.print("<input type='text' name='bucket_name' value=\"" + bucket.getBucketName() + "\">");

			out.print("<input type = 'hidden' id='bucket_id' name='bucket_id' value ='" + bucketId + "'>");
			out.print("<input type='hidden' id='link_num' value='" + links.size() + "'>");
			int i = 1;
			for (Link l : links) {
				out.print("<div class='link_block'>");
				out.print("<h3>Link " + i + "</h3>");
				out.print("<p>Link Name:</p>");
				out.print(String.format("<input type='text' name='link%d_name' value=\"%s\">", i, l.getLinkName().replaceAll("\"", "&quot")));
				out.print("<p>Link url:</p>");
				out.print(String.format("<input type='text' name='link%d' value =\"%s\">", i, l.getLink().replaceAll("\"", "&quot")));
				out.print("<p>Link Description:</p>");
				out.print(
				"<textarea type='text' name='link" + i + "_desc' rows='3' cols='50'>" + l.getDescription() + "</textarea>");
				out.print("<input type = 'hidden' name = 'link" + i + "_id' value='" + l.getLinkId() + "'>");
				out.print("<br>");
				out.print("</div>");
				i++;
			}
			%>


			<br id="last_break"> <input type="button" id="addLink"
				value="Add Link">
			<button type='submit'>Save</button>

		</div>

	</form>

	<script>
		let linkIndex = parseInt(document.getElementById("link_num").value) + 1;
		document.getElementById("addLink").onclick = function() {
			var block = document.createElement("div");
			block.classList.add("link_block");

			// header
			var header = document.createElement("h3");
			header.innerHTML = "Link " + linkIndex;
			block.appendChild(header);
			
			// name prompt
			var promptName = document.createElement("p");
			promptName.innerHTML = "Link Name:";
			block.appendChild(promptName);

			// name input 
			var inputName = document.createElement("input");
			inputName.type = "text";
			inputName.name = "link" + linkIndex + "_name";
			inputName.placeholder = "name";
			block.appendChild(inputName);

			// url prompt
			var promptUrl = document.createElement("p");
			promptUrl.innerHTML = "Link Name:";
			block.appendChild(promptUrl);
			
			// url input
			var inputLink = document.createElement("input");
			inputLink.type = "text";
			inputLink.name = "link" + linkIndex;
			inputLink.placeholder = "url";
			block.appendChild(inputLink);
			
			// description prompt
			var promptDesc = document.createElement("p");
			promptDesc.innerHTML = "Description:";
			block.appendChild(promptDesc);

			// description input
			var inputDesc = document.createElement("textarea");
			inputDesc.name = "link" + linkIndex + "_desc";
			inputDesc.placeholder = "description";
			inputDesc.rows = "3";
			inputDesc.cols = "50";
			block.appendChild(inputDesc);
			

			var hiddenId = document.createElement("input");
			hiddenId.type = "hidden";
			hiddenId.name = "link" + linkIndex + "_id";
			hiddenId.value = "-1";
			block.appendChild(hiddenId);

			var form = document.getElementById("roundbox");
			var lastBreak = document.getElementById("last_break");

			form.insertBefore(block, lastBreak);

			linkIndex++;

		}
	</script>

</body>
</html>