<!--
Author: BookStudy Team
Version: 1.0
Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
  <meta charset="utf-8">
  <title>View Organizer Information</title>
  <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
  <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
  <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
  <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
  <script src="${pageContext.request.contextPath}/js/utils.js"></script>
</head>


<body>
  <header></header>
  <h1>Organizer Information</h1>

  <div class="container">
    <form method="GET" action="<c:url value="/organizer/information"/>">

      <label><b>Phone number of the organizer:</b>
          <input name="phoneNumber" placeholder="Enter phone number" type="text"/><br/>
      </label>
      <button type="submit">Search</button><br/>
      <button type="reset">Cancel</button><br/>
      <button type="submit" href="${pageContext.request.contextPath}/jsp/homepage.jsp">Go back to Homepage</button>

    </form>
    <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
      <button type="submit">Go back to Homepage</button><br/>
    </form>
  </div>
  <footer></footer>
</body>
</html>