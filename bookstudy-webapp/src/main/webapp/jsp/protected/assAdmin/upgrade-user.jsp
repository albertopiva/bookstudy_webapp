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
		<title>Enroll member module</title>
        <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/body.css" type="text/css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
        <script src="${pageContext.request.contextPath}/js/utils.js"></script>
	</head>

  <body>

    <header></header>
    
	<h1>Enroll user to association</h1>


        <form method="POST" action="<c:url value="/user/update/UsrToMember"/>">
            <div class="container">
                <label><b>Phone number of the selected user:</b>
                    <input name="phoneNumber" placeholder="Enter phone number" type="text" required/><br/>
                </label>
                <label >Choose the role update:</label><br/>
                <input type="radio" id="association_member" name="role" value="association_member" checked>
                <label for="association_member">Association member</label><br>
                <input type="radio" id="association_admin" name="role" value="association_admin">
                <label for="association_admin">Association administrator</label><br>
                <label><b>City of birth:</b>
                    <input name="hometown" placeholder="Enter the city name" type="text" required/><br/>
                </label>
                <label><b>Region of birth:</b>
                    <input name="bornRegion" placeholder="Enter the region name" type="text"required/><br/>
                </label>
                <label><b>Date of birth:</b>
                    <input name="birthday" type="date" required/><br/>
                </label>
                <label><b>City of residence:</b>
                    <input name="city" placeholder="Enter the city name" type="text"required/><br/>
                </label>
                <label><b>Region of residence:</b>
                    <input name="region" placeholder="Enter the region name" type="text"required/><br/>
                </label>

                <div id="operationResult"></div>

                <button id="upgradeSubmit" type="submit">Make the role update</button><br/>
                <button type="reset">Cancel</button>
            </div>
        </form>
        <form action="${pageContext.request.contextPath}/jsp/homepage.jsp">
            <button type="submit">Go back to Homepage</button><br/>
        </form>

    <footer></footer>
    <script src="${pageContext.request.contextPath}/js/upgrade_user.js"></script>
	</body>
</html>
