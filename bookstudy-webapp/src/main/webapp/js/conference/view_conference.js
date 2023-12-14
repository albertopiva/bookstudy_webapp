/**
 * Manage the request about the visualization of conferences.
 *
 * @author BookStudy Team
 * @version 1.0
 * @since 1.0
 */

document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    readConference();
});

/**
 * Read the list of the conference
 */
function readConference(){
    var url = contextPath+"conference/list";
    genericGETRequest(url, fillConference);
}

/**
 * Callback of the readConference HttpRequest
 * @param req
 */
function fillConference(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        var div = document.getElementById("allConference");
        div.innerHTML="";
        if (req.status == 200) {
            //{"resource-list":[{"Conference":{"AlphanumericCode":"7jWMBB5XE5","Date":"2021-04-26","Title":"webapp","Description":"cciao","OrganizerID":"987654321","ConferenceRoomID":"CF1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);

            div.append(document.createElement("hr"));

            var table = document.createElement("table");
            var thead = document.createElement("thead");

            var th = document.createElement("th");
            th.innerHTML="AlphanumericCode";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Title";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Description";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Date";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Conference Room";
            thead.append(th);

            th = document.createElement("th");
            thead.append(th);

            table.append(thead);

            var tbody = document.createElement("tbody");
            for(var i=0; i < list.length;i++){
                var conf = list[i];
                var tr = document.createElement("tr");
                var td = document.createElement("td");
                td.innerHTML=conf['conference'].alphanumericCode;
                tr.append(td);


                var td = document.createElement("td");
                td.innerHTML=conf['conference'].title;
                tr.append(td);

                var td = document.createElement("td");
                td.innerHTML=conf['conference'].description;
                tr.append(td);

                var td = document.createElement("td");
                td.innerHTML=conf['conference'].date;
                tr.append(td);

                var td = document.createElement("td");
                td.innerHTML=conf['conference'].conferenceRoomID;
                tr.append(td);

                var td = document.createElement("td");
                var button = document.createElement("button");
                button.setAttribute("onclick","viewAttendance(\""+conf['conference'].alphanumericCode+"\")");
                button.innerHTML="View Attendance";
                td.append(button);
                tr.append(td);
                tbody.append(tr);
            }
            table.append(tbody);
            div.append(table);

            var divAttendance = document.createElement("div");
            divAttendance.setAttribute("id","attendance");
            div.append(divAttendance);

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
 * Get the list of users booked for the conference.
 *
 * @param conferenceID the conference to get the list of users.
 */
function viewAttendance(conferenceID){
    var url = contextPath+"conference/view/"+conferenceID;
    genericGETRequest(url, fillAttendanceConference);
}

/**
 * Callback of the viewAttendance HttpRequest.
 *
 * @param req
 */
function fillAttendanceConference(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        var div = document.getElementById("attendance");
        div.innerHTML="";
        if (req.status == 200) {
            //{"resource-list":[{"user":{"phoneNumber":"1234567890","name":"prova","surname":"prova2","role":"user"}}]}var list = JSON.parse(req.responseText)['resource-list'];

            var list = JSON.parse(req.responseText)['resource-list'];
            if(list===null || typeof list === 'undefined'){
                var span = document.createElement("span");
                var h2 = document.createElement("h2");
                h2.innerHTML = "No reservation for this conference.";
                span.append(h2);
                div.append(span);

            }else {
                console.log(list);

                var span = document.createElement("span");
                var h2 = document.createElement("h2");
                h2.innerHTML = "Attendance for the selected conference";
                span.append(h2);
                div.append(span);

                div.append(document.createElement("hr"));

                var table = document.createElement("table");
                var thead = document.createElement("thead");

                var th = document.createElement("th");
                th.innerHTML = "Name";
                thead.append(th);

                th = document.createElement("th");
                th.innerHTML = "Surname";
                thead.append(th);


                table.append(thead);

                var tbody = document.createElement("tbody");
                for (var i = 0; i < list.length; i++) {
                    var conf = list[i];
                    var tr = document.createElement("tr");
                    var td = document.createElement("td");
                    td.innerHTML = conf['user'].name;
                    tr.append(td);

                    var td = document.createElement("td");
                    td.innerHTML = conf['user'].surname;
                    tr.append(td);

                    tbody.append(tr);
                }
                table.append(tbody);
                div.append(table);

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