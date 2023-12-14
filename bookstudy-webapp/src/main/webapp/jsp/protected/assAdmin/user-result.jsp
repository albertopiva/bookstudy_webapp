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
    <title>View Users</title>
    <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/utils.js"></script>
</head>

<body>
<header></header>

<h1>View all users</h1>
<!-- display the list of users without the password-->
    <c:choose>
        <c:when test='${not empty UserList}'>
            <table>
                 <thread>
                    <tr>
                       <th>Phone Number</th><th>Name</th><th>Surname</th><th>Role</th>
                    </tr>
                 </thread>

                 <tbody>
                 <c:forEach var="user" items="${UserList}">
                      <tr>
                            <td><c:out value="${user.phoneNumber}"/></td>
                            <td><c:out value="${user.name}"/></td>
                            <td><c:out value="${user.surname}"/></td>
                            <td><c:out value="${user.role}"/></td>
                      </tr>
                 </c:forEach>
                 </tbody>
            </table>
        </c:when>
        <c:otherwise>
        <span><h3>Empty Users list</h3></span>
        </c:otherwise>
    </c:choose>
    <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
        <button type="submit">Go back to Homepage</button><br/>
    </form>
    <footer></footer>

</body>
</html>