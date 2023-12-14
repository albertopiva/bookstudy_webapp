

document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    loadTop3Conference();

});

function loadTop3Conference(){
    var url = contextPath+"allConference";
    genericGETRequest(url, fill3TopConferenceInfo);
}

function fill3TopConferenceInfo(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        if (req.status === 200) {
            //{"resource-list":[{"Conference":{"AlphanumericCode":"7jWMBB5XE5","Date":"2021-04-26","Title":"webapp","Description":"cciao","OrganizerID":"987654321","ConferenceRoomID":"CF1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);
            //fill conference

            var divViewConference = document.getElementById("view-top-conference");
            divViewConference.innerHTML="";
            var table = document.createElement("table");
            table.classList.add("table-homepage");
            var thead = document.createElement("thead");

            var th = document.createElement("th");
            th.innerHTML="Date";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Title";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Description";
            thead.append(th);


            table.append(thead);

            var tbody = document.createElement("tbody");
            //for(var i=0; i <  list.length;i++){
            if (list.length < 5) {
                for (var i = 0; i < list.length; i++) {
                    var conference = list[i]['conference'];
                    var tr = document.createElement("tr");
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
            }else {
                for (var i = 0; i < 5; i++) {
                    var conference = list[i]['conference'];
                    var tr = document.createElement("tr");
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