<%@ page import="src.linkit.LinkItSql"%>
<%@ page import="src.LinkItServer.LinkItClient"%>
<%@ page import = "src.linkit.User" %>
<%@page import="java.util.Queue" %>
<%
	// Construct and store sql wrapper class to make it easier to do sql query and update.
	LinkItSql linkitSql = new LinkItSql();
	linkitSql.connect();
	session.setAttribute("LinkItSql", linkitSql);
	String profileURL = "../jsp/Profile.jsp";
	String packageURL = "../jsp/CreatePackage.jsp";
	boolean isGuest = false;
	User currUser = (User)session.getAttribute("currUser");
	if(currUser == null) {
		isGuest = true;
	}
	else{
		profileURL = "../jsp/Profile.jsp?u=" + currUser.getUsername();
	}
	
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LinkIt!</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="../css/Entry.css">
<link rel="stylesheet" href="../css/Navbar.css">
<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="https://fonts.googleapis.com/css?family=Lato"
	rel="stylesheet">
	
<script src="../js/Search.js" type="text/javascript"></script>
<script src="../js/RefreshFeed.js" type="text/javascript"></script>
</head>
<body onload = "init()">
	<div id="view" class="home">
		<div id="teaser">
			<div id="teaser-content">
				<div class="container">
					<div id="landing-top-bar" class="row">
						<div class="pull-left">
							<img id="brand" src="../img/linkit.png">
						</div>
						<div id="temp" class="pull-right color-97"
							style="font-size: 14px;">
							<div class="panel" id="right-header">
								<span class="space-top-4 space-right-10 float">
								<% if(isGuest){ %>
									Already using LinkIt?
								<%}
								else { %>
									Welcome <%=currUser.getUsername()%>!
								<%} %>
								</span>
								<%if(isGuest){ %>
									<span><a href="../jsp/Login.jsp"class="btn btn-primary"> Log in</a></span>
								<% } %>
								<%if(!isGuest){ %>
								<span><a id="login_button" href="<%=profileURL%>"
									class="btn btn-primary"> <img id="profile_img"
										src="../img/black_profile.png"></a></span>
								<span><a id="login_button" href="<%=packageURL%>"
								class="btn btn-primary"> <img id="profile_img"
								src="../img/create_packages.png"></a></span> 
									<a id="feed_button" class="btn btn-primary pull-right"
									data-parent="#temp" data-toggle="collapse" data-target="#feed">
									<img id="feed_img" src="../img/plus_filled.png">
								</a>
								<div id="feed" class="collapse">
									<img id="connect_icon" src="../img/arrow_icon.png">
									<ul id="feed2" class="nav navbar-nav pull-right">
										<li><p>
										<%
											LinkItClient client = (LinkItClient)session.getAttribute("LinkItClient");
											Queue<String> q = client.getFeedElements();
											for(String m : q) { %>
												<p><%=m%></p>
											<%}%>
										</li>
									</ul>
								</div>
								<%}%>
							</div>
						</div>
					</div>
					<div class="col-span-12">
						<div class="row no-space-top-iPhone" id="teaser-stage">
							<div class="col-span-6">
								<h1 class="color-30 space-bottom-10" id="teaser-headline">
									<span class="float">Find Content.</span><span class="float">Really
										Simple.</span><span class="float">From Students.</span>
								</h1>
								<div
									class="space-top space-bottom-10 space-left-10 no-space-left-mobile pull-center-mobile fill-iPhone row"
									style="clear: both">
										<div id="teaser-form-notify-message"
											class="pull-center-mobile no-space-left"></div>
										<div>
										<form name="search_form" action="ResultsPage.jsp" method="GET">
											<span class="col-span-8 pull-left pull-center-iPhone notify-input">
												<input type="text" class="input-large" placeholder="Search for a class or topic" name="search">
											</span>
											<span class="space-left-10 col-span-4 pull-left">
												<button class="btn btn-danger btn-large" type="submit">Search</button>
											</span>
										</form>
										</div>
								</div>
							</div>
							<div class="col-span-6 pull-center-mobile">
								<img src="../img/logo.png">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>