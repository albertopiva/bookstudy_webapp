/**
 * Manage the request about the deletion of conferences.
 *
 * @author BookStudy Team
 * @version 1.0
 * @since 1.0
 */

document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    document.getElementById("deleteConference").addEventListener("click",deleteConference);
    loadMyConference();
});

function loadMyConference(){
    var url = contextPath+"conference/list";
    genericGETRequest(url,fillMyConference);
}

function fillMyConference(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        console.log(req.responseText);
        if (req.status == 200) {
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);
            $("#conferenceID").empty();
            var selectConference = document.getElementById("conferenceID");

            for(var i=0; i < list.length;i++){
                var conf = list[i]['conference'];
                if(new Date(conf.date)>new Date()) {
                    var option = document.createElement("option");
                    option.setAttribute("value", conf.alphanumericCode);
                    option.innerText = conf.alphanumericCode;
                    selectConference.appendChild(option);
                }
            }
        }else if(req.status==400){
            alert("Wrong Request!");

        }else if(req.status==404){
            alert("This conference doesn't exist!");

        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}

/**
 * Delete a conference, giving the alphanumericCode
 *
 * @returns {boolean}
 */
function deleteConference(){
    var alphaCode = document.getElementById("conferenceID").value;
    var httpRequest = new XMLHttpRequest();
    var url = contextPath+"conference/manage/"+alphaCode;
    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    console.log(url);
    httpRequest.onreadystatechange = function (){ deleteResult(httpRequest)};
    httpRequest.open('DELETE', url);
    httpRequest.send();
}

/**
 * Callback of the deleteConference HttpRequest
 * @param req
 */
function deleteResult(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        document.getElementById("responseDelete").innerHTML=req.responseText;
        if (req.status == 200) {
            alert("Conference deleted");
            window.location.href = (contextPath+"/jsp/homepage.jsp");
        }else if(req.status==400){
            alert("Wrong Request!");
            window.location.href = (contextPath+"/jsp/homepage.jsp");
        }else if(req.status==404){
            alert("This conference doesn't exist!");
            window.location.href = (contextPath+"/jsp/homepage.jsp");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}
