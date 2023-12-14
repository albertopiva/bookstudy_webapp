/**
 * Utils file, that provide some useful method.
 *
 * @author BookStudy Team
 * @version 1.0
 * @since 1.0
 */

/**
 * The context path of the webapp
 * @type {string}
 * The header path of the webapp
 * @type {string}
 * The footer path of the webapp
 * @type {string}
 * The navbar path of the webapp
 * @type {string}
 */

var contextPath = "http://localhost:8080/bookstudy-webapp-1.0/"
var header = "/html/commonPart/header.html"
var footer = "/html/commonPart/footer.html"



document.addEventListener('DOMContentLoaded', function(event) {
    loadTemplate()
})


function loadTemplate(){
    var headerUrl = new URL(contextPath+header);
    var footerUrl = new URL(contextPath+footer);

    templateGETRequest(headerUrl, loadHeader);
    templateGETRequest(footerUrl, loadFooter);
}


/**
 * A generic GET XMLHTTPRequest.
 *
 * @param url the url of the request.
 * @param callback the function to invoke when the servlet answer.
 * @returns {boolean} false if the request did not created.
 */
function genericGETRequest(url, callback){
    var httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    httpRequest.onreadystatechange = function (){ callback(httpRequest)};

    httpRequest.open('GET', url);
    httpRequest.send();
}


/**
 * A generic GET XMLHTTPRequest.
 *
 * @param url the url of the request.
 * @param callback the function to invoke when the servlet answer.
 * @returns {boolean} false if the request did not created.
 */
function templateGETRequest(url, callback){
    var httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
      alert('Cannot create an XMLHTTP instance');
      return false;
    }
    httpRequest.onreadystatechange = function (){
        if(httpRequest.readyState == XMLHttpRequest.DONE){
            if(httpRequest.status == 200){
                callback(httpRequest.responseText)
            }else {
                console.log(httpRequest.responseText);
                alert("problem processing the request");
            }
        }
    }

    httpRequest.open('GET', url);
    httpRequest.send();
}

/**
 *
 * @param data
 */
function loadHeader(data){

    var loggedIn = sessionStorage.getItem("loggedIn");
    var role = sessionStorage.getItem("role");
    var username = sessionStorage.getItem("username") + " " + sessionStorage.getItem("surname");

    document.getElementsByTagName("header")[0].innerHTML = data;

    document.getElementById("library_icon").addEventListener("click",function(){window.location.href=contextPath});

    var navbar = document.getElementById("navbar-button");

    navbar.addEventListener('click', function (event) { button(); })


    if(loggedIn) {

        document.getElementById("navbar").style.display = "block";
        document.getElementById("logged").style.display = "flex";
        document.getElementById("unlogged").style.display = "none";
        document.getElementById("userdata").innerHTML = "Welcome, "+username;

        if(role === "user") {
            document.getElementById("user-links").style.display = "block";
        }
        else if(role === "association_member") {
            document.getElementById("ass-member-links").style.display = "block";
        }
        else if(role === "association_admin") {
            document.getElementById("ass-admin-links").style.display = "block";
        }
        else if(role === "cultural_office") {
            document.getElementById("cult-office-links").style.display = "block";
        }
        else if(role === "librarian") {
            document.getElementById("librarian-links").style.display = "block";
        }
        else if(role === "organizer") {
            document.getElementById("organizer-links").style.display = "block";
        }
    }
    if(!loggedIn) {
        document.getElementById("logged").style.display = "none";
        document.getElementById("unlogged").style.display = "block";
    }
}


/**
 *
 * @param data
 */
function loadFooter(data){
    document.getElementsByTagName("footer")[0].innerHTML = data;
}


/**
 * Function to open and close the navigation bar using a button
 * @param type click on the button
 * @param function function that handle the navbar
 */


function button() {

    var x = document.getElementById("nav-area");

        if(x.style.display === "block") {
        x.style.display = "none";
    } else {
        x.style.display = "block";
    }
}

function sameDay(d1, d2) {
    return d1.getFullYear() === d2.getFullYear() &&
        d1.getMonth() === d2.getMonth() &&
        d1.getDate() === d2.getDate();
}