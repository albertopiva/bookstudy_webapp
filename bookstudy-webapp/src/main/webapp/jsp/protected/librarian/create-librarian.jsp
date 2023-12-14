<!--
 Author: BookStudy Team
 Version: 1.0
 Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Create New Librarian Account</title>
        <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
        <script src="${pageContext.request.contextPath}/js/utils.js"></script>
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
        <div class="result">New librarian created</div>
    </c:if>

	<h1>Create New Librarian Account</h1>
    <div class="container">
	<form method="POST" action="<c:url value="/user/insert"/>">

            <label><b>Phone number:</b>
                <input name="phoneNumber" placeholder="Enter phone number" type="text"/><br/>
            </label>
            <label><b>Password:</b>
                <input name="password" placeholder="Enter password" type="password"/><br/>
            </label>
            <label><b>Name:</b>
                <input name="name" placeholder="Enter name" type="text"/><br/>
            </label>
            <label><b>Surname:</b>
                <input name="surname" placeholder="Enter surname" type="text"/><br/>
            </label>
            <input name="role" value="librarian" type="hidden"/><br/><br/>

            <button type="submit">Sign in</button><br/>
            <button type="reset">Cancel</button><br/>

	</form>
    <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
        <button type="submit">Go back to Homepage</button><br/>
    </form>
    </div>
    <footer></footer>

  </body>
</html>
