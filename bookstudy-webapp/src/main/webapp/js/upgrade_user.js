var phoneNumber = document.getElementsByName("phoneNumber")[0];
/*
document.addEventListener('DOMContentLoaded', function (event){
    document.getElementById("upgradeSubmit").addEventListener("click",)
})*/


phoneNumber.addEventListener("change", function (){
    if(typeof phoneNumber.value == "undefined" || phoneNumber.value.trim() == ""){
        resetDataForm();
        return false;
    }

    var httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState == XMLHttpRequest.DONE) {
            if (httpRequest.status == 200) {
                var member = JSON.parse(httpRequest.responseText)['Member'];
                if (member != null || typeof member != "undefined") {
                    loadDatainForm(member);
                }
                else {
                    checkUser();
                }
            } else {
                console.log(httpRequest.responseText);
                alert("problem processing the request");
            }
        }
    }

    var url = contextPath + "member/"+ phoneNumber.value;
    httpRequest.open("GET", url);
    httpRequest.send();
})

function loadDatainForm(member){
    var homeTown = document.getElementsByName("hometown")[0];
    homeTown.setAttribute("value",member["hometown"]);
    var bornRegion = document.getElementsByName("bornRegion")[0];
    bornRegion.setAttribute("value",member["bornRegion"]);
    var birthday = document.getElementsByName("birthday")[0];
    birthday.setAttribute("value",member["birthday"]);
    var city = document.getElementsByName("city")[0];
    city.setAttribute("value",member["city"]);
    var region = document.getElementsByName("region")[0];
    region.setAttribute("value",member["region"]);
}

function checkUser(){
    var httpRequest = new XMLHttpRequest()

    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState == XMLHttpRequest.DONE) {
            if (httpRequest.status == 200) {
                var user = JSON.parse(httpRequest.responseText)['user'];
                if (user == null || typeof user == "undefined") {
                    alert("Please insert a registered user!");
                    console.log(httpRequest.responseText);
                    return false;
                }else resetDataForm();
            } else {
                console.log(httpRequest.responseText);
                alert("problem processing the request");
            }
        }
    }

    var url = contextPath + "userCheck/"+phoneNumber.value;
    httpRequest.open("GET", url);
    httpRequest.send();
}

function resetDataForm(){
    var homeTown = document.getElementsByName("hometown")[0];
    if(homeTown.hasAttribute("value"))
        homeTown.removeAttribute("value");
    var bornRegion = document.getElementsByName("bornRegion")[0];
    if(bornRegion.hasAttribute("value"))
        bornRegion.removeAttribute("value");
    var birthday = document.getElementsByName("birthday")[0];
    if(birthday.hasAttribute("value"))
        birthday.removeAttribute("value");
    var city = document.getElementsByName("city")[0];
    if(city.hasAttribute("value"))
        city.removeAttribute("value");
    var region = document.getElementsByName("region")[0];
    if(region.hasAttribute("value"))
        region.removeAttribute("value");
}