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
		<title>View User Conference Reservation</title>
        <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
		<link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
		<link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
		<link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
        <script src="${pageContext.request.contextPath}/js/utils.js"></script>
	</head>

	<body>
		<header></header>
		<h1>View all my conference reservation</h1>
		<hr/>
		<c:choose>
			<c:when test='${empty user}'>
				<span><h3>Bad request, user not found</h3></span>
			</c:when>
			<c:otherwise>
				<!-- display the list of found reservation made by a user, if any -->
				<c:if test='${empty reservationList}'>
					<span><h3>You had never made a reservation</h3></span>
				</c:if>
				<c:if test='${not empty reservationList}'>
					<table class="small-table">
						<thead>
							<tr>
								<th>Conference ID</th>
							</tr>
						</thead>
						<span>Active conference reservation: <c:out value="${reservationSize}"/></span>
						<tbody>
							<c:forEach var="reserv" items="${reservationList}">
								<tr>
									<td><c:out value="${reserv.alphanumericCode}"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
			</c:otherwise>
		</c:choose>
		<form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
			<button type="submit">Go back to Homepage</button><br/>
		</form>
		<footer></footer>
	</body>
</html>
