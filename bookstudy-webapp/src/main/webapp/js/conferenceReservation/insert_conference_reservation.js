/**
 * Manage the request about the creation of reservation in the library.
 *
 * @author BookStudy Team
 * @version 1.0
 * @since 1.0
 */

document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    loadAllConference();

});

function loadAllConference(){
    var url = contextPath+"allConference";
    genericGETRequest(url, fillAllConferenceInfo);
}

function fillAllConferenceInfo(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        if (req.status == 200) {
            //{"resource-list":[{"Conference":{"AlphanumericCode":"7jWMBB5XE5","Date":"2021-04-26","Title":"webapp","Description":"cciao","OrganizerID":"987654321","ConferenceRoomID":"CF1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);
            //fill conference

            var divViewConference = document.getElementById("view-conference");
            divViewConference.innerHTML="";

            //check how many conference there are in the next days
            var existsConference = false;
            for(var i=0; i < list.length;i++){
                var conference = list[i]['conference'];
                var dateOfConference = new Date(Date.parse(conference.date));
                var today = new Date();
                if(today<dateOfConference) {
                    existsConference=true;
                }
            }
            if(!existsConference){
                var h1 = document.createElement("h2");
                h1.innerText = "There are no conferences in the next days.";
                divViewConference.appendChild(h1);
                document.getElementById("book_conference").style.display="none";
            }else {
                var h1 = document.createElement("h2");
                h1.innerText = "Next conferences";
                divViewConference.appendChild(h1);
                var table = document.createElement("table");
                var thead = document.createElement("thead");

                var th = document.createElement("th");
                th.innerHTML = "Code";
                thead.append(th);

                th = document.createElement("th");
                th.innerHTML = "Date";
                thead.append(th);

                th = document.createElement("th");
                th.innerHTML = "Title";
                thead.append(th);

                th = document.createElement("th");
                th.innerHTML = "Description";
                thead.append(th);

                table.append(thead);

                var tbody = document.createElement("tbody");
                var today = new Date();
                for (var i = 0; i < list.length; i++) {
                    var conference = list[i]['conference'];
                    var dateOfConference = new Date(Date.parse(conference.date));
                    if(today<dateOfConference) {
                        var tr = document.createElement("tr");
                        var td = document.createElement("td");
                        td.innerHTML = conference.alphanumericCode;
                        tr.append(td);

                        var td = document.createElement("td");
                        td.innerHTML = conference.date;
                        tr.append(td);

                        var td = document.createElement("td");
                        td.innerHTML = conference.title;
                        tr.append(td);

                        var td = document.createElement("td");
                        td.innerHTML = conference.description;
                        tr.append(td);

                        tbody.append(tr);
                    }
                }
                table.append(tbody);
                divViewConference.append(table);
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