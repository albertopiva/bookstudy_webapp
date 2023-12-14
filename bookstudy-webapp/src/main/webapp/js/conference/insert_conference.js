/**
 * Manage the request about the creation of conferences.
 *
 * @author BookStudy Team
 * @version 1.0
 * @since 1.0
 */

document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    document.getElementById("insertConference").addEventListener("click",insertConference);
    loadRooms();
});
/**
 * Get the list of rooms
 */
function loadRooms(){
    var url = contextPath+"conference/room";
    genericGETRequest(url, fillRooms);
}

/**
 * Callback of the loadRooms HttpRequest. It loads the list of conference rooms
 * in the select in the insert reservation form.
 * @param req
 */
function fillRooms(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        var select = document.getElementById("selectRoom");

        if (req.status == 200) {
            //{"resource-list":[{"Conference":{"AlphanumericCode":"7jWMBB5XE5","Date":"2021-04-26","Title":"webapp","Description":"cciao","OrganizerID":"987654321","ConferenceRoomID":"CF1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);
            if(list==null){alert("No rooms!")}
            else{
                for(var i=0;i < list.length;i++){
                    var option = document.createElement("option");
                    option.setAttribute("value",list[i]['conferenceRoom'].ID);
                    option.innerHTML = list[i]['conferenceRoom'].name;
                    select.append(option);
                }
            }
        }else if(req.status==400){
            alert("Bad Request!");
            window.location.href = (contextPath+"/jsp/homepage.jsp");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}


/**
 * Create a new conference
 * @returns {boolean}
 */
function insertConference(){
    var conference = new Object();
    conference.alphanumericCode = "";
    conference.date = document.getElementsByName("date")[0].value;
    conference.title = document.getElementsByName("title")[0].value;
    conference.description = document.getElementsByName("description")[0].value;
    conference.organizerID = "";
    conference.conferenceRoomID = document.getElementsByName("confroomID")[0].value;
    var httpRequest = new XMLHttpRequest();
    var url = contextPath+"conference/manage";
    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    var json_obj = new Object();
    json_obj.conference = conference;
    console.log(url);
    console.log(JSON.stringify(json_obj));
    httpRequest.onreadystatechange = function (){ insertResult(httpRequest)};

    httpRequest.open('POST', url);
    httpRequest.send(JSON.stringify(json_obj));
}

/**
 * Callback of the insertConference HttpRequest
 * @param req
 */
function insertResult(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        document.getElementById("responseInsert").innerHTML=req.responseText;
        if (req.status == 200) {
            alert("Conference Inserted!");
            window.location.href = (contextPath+"/jsp/homepage.jsp");
        }else if(req.status==400){
            alert("Bad Request!");
            window.location.href = (contextPath+"/jsp/homepage.jsp");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}
