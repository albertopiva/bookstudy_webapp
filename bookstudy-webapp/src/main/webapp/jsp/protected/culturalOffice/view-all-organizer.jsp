<!--
Author: BookStudy Team
Version: 1.0
Since: 1.0
-->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>View All Organizers</title>
    <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/utils.js"></script>
</head>

  <body>
    <header></header>
    <h1>View All Organizers</h1>
    <div class="container">
      <form method="GET" action="<c:url value="/organizer/view"/>">
        <button type="View all">Submit</button><br/>
      </form>
      <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
        <button type="submit">Go back to Homepage</button><br/>
      </form>
    </div>
    <footer></footer>

  </body>
</html>