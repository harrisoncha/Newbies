<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import = "src.linkit.User" %>
<%@ page import="src.LinkItServer.LinkItClient"%>
<%@page import="java.util.Queue" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	boolean isGuest = false;
	User currUser = (User)session.getAttribute("currUser");
	if(currUser == null) {
		isGuest = true;
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="../css/Navbar.css">
<link rel="stylesheet" href="../css/Login.css">
<script src="../js/Login.js" type="text/javascript"></script>
<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top no-space-top"
		id="navbar-landing">
		<div class="container space-top-10">
			<div class="pull-left">
				<a href="../jsp/entry_linkit.jsp"><img id="brand"
					src="../img/linkit.png"></a>
			</div>
			<div id="temp" class="pull-right color-97" style="font-size: 14px;">
				<div class="panel" id="right-header">
					<span class="space-top-6 space-right-10 float">
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
								<span><a id="login_button" href="../jsp/Profile.jsp"
									class="btn btn-primary"> <img id="profile_img"
										src="../img/black_profile.png"></a></span> <a
									id="feed_button" class="btn btn-primary pull-right"
									data-parent="#temp" data-toggle="collapse" data-target="#feed">
									<img id="feed_img" src="../img/plus_filled.png">
								</a>
								<div id="feed" class="collapse">
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
	</div>
	<div id="view" class="login">
		<div class="container">
			<div class="row" id="content">
				<div class="container pull-center fill" id="alerts"></div>
				<div class="col-span-12" id="main">
					<div id="liberio-view">
						<div id="center" class="col-span-12 space-bottom-55">
							<div id="primary-login-center-view">
								<div class="modal-wrapper pull-center">
									<h1 class="color-30 text-center space-top space-bottom-5">Login</h1>
									<form id="primary-login-form" name = "login-form" method="get">
										<div
											class="col-span-12 space-bottom-15 no-space-left no-space-right">
											<input type="text" class="input-large"
												placeholder="Email address" name="email" required>
										</div>
										<div
											class="col-span-12 space-bottom-15 no-space-left no-space-right">
											<input type="password" class="input-large"
												placeholder="Password" name="password" required>
										</div>
										<p id="error-message" style="font-size: 17px; text-align: center; color: red;"></p>
										<div class="col-span-12 no-space-left no-space-right">
											<button type = "submit"
												class="btn btn-danger btn-large pull-center modal-wrapper">
												Log in
											</button>
										</div>
									</form>
									<div
										class="col-span-12 space-top-15 space-bottom-15 text-center">
										<span class="ruler">or</span>
									</div>
									<form>
										<div
											class="col-span-12 space-bottom-15 no-space-left no-space-right">
											<a
												class="btn btn-primary btn-large modal-wrapper trigger void"
												href="Signup.jsp"
												data-serialize="#redeem-code-form">Sign up</a>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>