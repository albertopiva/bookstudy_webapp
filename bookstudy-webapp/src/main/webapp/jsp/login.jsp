<!--
Author: StudyTeam
Version: 1.0
Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Login Form</title>
	<link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
	<link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
	<link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
	<link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath}/js/utils.js"></script>
	<script src="${pageContext.request.contextPath}/js/login.js"></script>

</head>

<body>
	<header></header>

	<c:if test='${error == "true"}'>
		<br/>
		<br/>
		<div class="result">Error: wrong fields</div>
	</c:if>
	<c:if test='${error == "false"}'>
		<br/>
		<br/>
		<div class="result">New user created</div>
	</c:if>

	<div class="container">
		<div class = "simil-form">
			<label id="uname"><b>Phone Number</b>
				<input type="text" placeholder="Enter phone number" name="phoneNumber" required>
			</label><br/>

			<label id="Password"><b>Password</b>
				<input type="password" placeholder="Enter Password" name="password" required>
			</label><br/>


			<label>Select user type:<br/>
				<input name="kindOfUser" type="radio" value = "user" checked/>User
				<input name="kindOfUser" type="radio" value = "organizer"/>Organizer<br/>
			</label>

			<div id = "login_error"></div>

			<button id = "login_submit">Login</button><br/>
			<button type = "reset" id = "reset">Cancel</button><br/>
		</div>
	</div>

<footer></footer>
</body>
</html>
