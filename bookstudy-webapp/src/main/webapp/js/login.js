document.addEventListener('DOMContentLoaded', function(event) {
    document.getElementById("login_submit").addEventListener("click",login);
    document.getElementById("reset").addEventListener("click",resetForm);
});
function resetForm() {
    document.getElementsByName("phoneNumber")[0].value = "";
    document.getElementsByName("password")[0].value = "";
}


function login(){
    var httpRequest = new XMLHttpRequest();
    var url = contextPath+"auth/";
    var phoneNumber = document.getElementsByName("phoneNumber")[0].value;
    var password = document.getElementsByName("password")[0].value;
    var radioButtons = document.getElementsByName("kindOfUser");
    var jsonObj = new Object();
    if(radioButtons[0].checked && radioButtons[0].value == "user") {
        var user = new Object();
        user.phoneNumber = phoneNumber;
        user.password = password;
        jsonObj.user = user;
        url+="user";
    }else if(radioButtons[1].checked && radioButtons[1].value == "organizer") {
        var organizer = new Object();
        organizer.phoneNumber = phoneNumber;
        organizer.password = password;
        jsonObj.organizer = organizer;
        url+="organizer";
    }
    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    httpRequest.onreadystatechange = function (){ check_login(httpRequest)};
    httpRequest.open('POST', url);
    console.log(JSON.stringify(jsonObj));
    httpRequest.send(JSON.stringify(jsonObj));
}

function check_login(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        //document.getElementById("errorBox").innerHTML=req.responseText;
        if (req.status == 200) {
            window.location.href=contextPath;
        }else if(req.status==400){
            alert("Wrong Request!");
        }else if(req.status==401){
            //Unauthorized
            document.getElementById("login_error").innerText="Wrong credentials.";
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}

