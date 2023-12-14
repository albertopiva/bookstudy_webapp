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
        <meta charset="UTF-8">
        <title>Homepage</title>

        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script src="${pageContext.request.contextPath}/js/utils.js"></script>
        <!--<script src="${pageContext.request.contextPath}/js/jquery-3.6.0.js"></script>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/all.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/FontAwesome/css/fontawesome.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/css/template.css" type="text/css" rel="stylesheet"/>

        <script src="/bookstudy-webapp-1.0/js/homepage_conference.js"></script>
        <script src="/bookstudy-webapp-1.0/js/jquery.simple-calendar.js"></script>
        <link href="${pageContext.request.contextPath}/css/simple-calendar.css" rel="stylesheet"/>

        <c:choose>
            <c:when test="${!empty sessionScope.phoneNumber}">
                <script>
                    sessionStorage.setItem("loggedIn", true);
                    sessionStorage.setItem("username","${sessionScope.name}");
                    sessionStorage.setItem("phoneNumber", "${sessionScope.phoneNumber}");
                    sessionStorage.setItem("role","${sessionScope.role}");
                    sessionStorage.setItem("surname","${sessionScope.surname}");
                </script>
            </c:when>
            <c:otherwise>
                <script>
                    sessionStorage.removeItem("loggedIn");
                    sessionStorage.removeItem("username");
                    sessionStorage.removeItem("phoneNumber");
                    sessionStorage.removeItem("role");
                </script>
            </c:otherwise>
        </c:choose>
    </head>

    <body>

        <header></header>

        <div id="content">

            <div id="myCarousel" class="carousel slide" data-ride="carousel">
                <!-- Indicators -->
                <ol class="carousel-indicators">
                    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#myCarousel" data-slide-to="1"></li>
                    <li data-target="#myCarousel" data-slide-to="2"></li>
                    <li data-target="#myCarousel" data-slide-to="3"></li>
                </ol>

                <!-- Wrapper for slides -->
                <div class="carousel-inner">
                    <div class="item active">
                        <img class="sliderShow" src="/bookstudy-webapp-1.0/media/slide1.jpg" alt="newLibrary">
                    </div>

                    <div class="item">
                        <img class="sliderShow" src="/bookstudy-webapp-1.0/media/slide2.jpg" alt="books">
                    </div>

                    <div class="item">
                        <img class="sliderShow" src="/bookstudy-webapp-1.0/media/slide3.jpg" alt="libraryHall" >
                    </div>

                    <div class="item">
                        <img class="sliderShow" src="/bookstudy-webapp-1.0/media/slide4.jpg" alt="building" >
                    </div>
                </div>
            </div>
            <div id="homepage_description">
                <p>
                    The library has the following opening hours: monday - friday 9:30-12:30 // 15:00-19:30 and saturday only 9:30-12:30
                </p>
                <p>
                    The library provides the following services:
                    <ul>
                        <li>Online services given by this web applications</li>
                        <li>Books lending</li>
                        <li>"Library at home"</li>
                        <li>Study Room</li>
                        <li>Conference Room</li>
                    </ul>
                </p>
            </div>
            <div class="homepage_elements">
                <div class="column" id = "view-top-conference"></div>

                <!--Google Maps-->
                <div class="column">
                    <div class="column-content">
                        <iframe id="libraryGmap" src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d4790.318490842921!2d12.073647620780333!3d45.549867112742824!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xa3c33309acc5aa78!2sBiblioteca%20Comunale%20Eliseo%20Carraro!5e0!3m2!1sen!2sit!4v1622036515166!5m2!1sen!2sit" width="400" height="300" style="border:0;" allowfullscreen="" loading="lazy"></iframe>
                    </div>
                </div>
            </div>

        </div>


        <footer></footer>

    </body>
</html>