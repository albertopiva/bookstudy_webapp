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
    <title>login</title>
    <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/utils.js"></script>
</head>
<body>

    <header></header>

    <div class="container">
        You are not authorized to see the requested page.<br/>
        <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
            <button type="submit">Go back to Homepage</button><br/>
        </form>
    </div>

    <footer></footer>
</body>
</html>