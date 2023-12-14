<!--
Author: BookStudy
Version: 1.0
Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Insert Reservation Form</title>
    <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/utils.js"></script>
    <script src="${pageContext.request.contextPath}/js/conferenceReservation/insert_conference_reservation.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery-3.6.0.js"></script>
</head>

<body>

    <header></header>

    <c:if test='${error == "true"}'>
        <br/>
        <br/>
        <div class="result">Error: conference already booked or not existing</div>
        <br/>
    </c:if>
    <c:if test='${error == "false"}'>
        <br/>
        <br/>
        <div class="result">Conference booked</div>
        <br/>
    </c:if>
    <div class="c
    ontainer">
        <div id = "view-conference"></div>
        <form id ="book_conference" method="POST" action="<c:url value="/confReserv/insert"/>">

                <h1>Book your seat in a conference</h1>
                <label><b>Choose the conference:</b>
                    <input id = "conferenceID" name = "conferenceID" placeholder="Enter ID" type = "text"/><br/><br/>
                </label>
                <button type="submit">Submit</button><br/><br/>
                <button type="reset">Cancel</button><br/>

        </form>
        <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
            <button type="submit">Go back to Homepage</button><br/>
        </form>
    </div>
    <footer></footer>
</body>
</html>
