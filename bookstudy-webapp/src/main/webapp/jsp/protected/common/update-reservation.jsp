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
		<title>Update Reservation Form</title>
        <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
        <script src="${pageContext.request.contextPath}/js/utils.js"></script>
	</head>

  <body>
    <header></header>
	<h1>Update Reservation Form</h1>
    <div class="container">
	<form method="POST" action="<c:url value="/seatReserv/update/entry"/>">
            <span id="infoRegistration"></span>
		    <label>Update reservation (entry time)</label><br/>
            <label><b>AlphaCode:</b>
		        <input id = "entryCode" name="alphanumericCode"  type="text"/><br/><br/>
            </label>
            <button type="submit">Update entry time</button><br/>
            <button type="reset">Cancel</button>
    </form>
    <br/>

        <form method="POST" action="<c:url value="/seatReserv/update/exit"/>">

                <label><b>Update reservation (exit time)</b></label><br/>
                <label><b>AlphaCode:</b>
                    <input id = "exitCode" name="alphanumericCode" type="text"/><br/><br/>
                </label>
                <button type="submit">Update exit time</button><br/>
                <button type="reset">Cancel</button><br/>

        </form>
        <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
            <button type="submit">Go back to Homepage</button><br/>
        </form>
    </div>
    <footer></footer>
	</body>
</html>
