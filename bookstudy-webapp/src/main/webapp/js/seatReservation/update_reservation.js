/**
 * Manage the request about the update of reservation in the library.
 *
 * @author BookStudy Team
 * @version 1.0
 * @since 1.0
 */

document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    loadInfoReservation();
});

/**
 * Read the list of reservation for this user
 */

function loadInfoReservation(){
    var url = contextPath + "seatReserv/view/user";
    genericGETRequest(url,displayInfo)
}

/**
 * Display the information about today's reservations.
 * Give the possibility to set up the entry or exit time in the library.
 * @param req
 */
function displayInfo(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        //document.getElementById("errorBox").innerHTML=req.responseText;
        console.log(req.responseText)
        if (req.status == 200) {
            //this is the json returned
            //{"resource-list":[{"LibrarySeat":{"seatID":"2","Room":"R3"}},{"LibrarySeat":{"seatID":"12","Room":"R2"}},{"LibrarySeat":{"seatID":"8","Room":"R2"}},{"LibrarySeat":{"seatID":"15","Room":"R1"}},{"LibrarySeat":{"seatID":"20","Room":"R3"}},{"LibrarySeat":{"seatID":"1","Room":"R1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            //console.log(list);

            $("#infoReservation").empty()
            var divInfo = document.getElementById("infoReservation");
            var existReservationForToday = false;
            let todayString = new Date().toISOString().substr(0, 10);
            for(var i = 0; i < list.length;i++){
                if(list[i].SeatReservation.date == todayString){
                    var today = new Date();
                    if ( (list[i].SeatReservation.TimeSlot.hour_range === "morning" && (today.getHours() * 60 + today.getMinutes()) < (13 * 60) && (today.getHours() * 60 + today.getMinutes()) >= (8 * 60))
                        || (list[i].SeatReservation.TimeSlot.hour_range === "afternoon" && (today.getHours() * 60 + today.getMinutes()) < (18 * 60) && (today.getHours() * 60 + today.getMinutes()) >= (13 * 60))
                        || (list[i].SeatReservation.TimeSlot.hour_range === "evening" && (today.getHours() * 60 + today.getMinutes()) < (24 * 60) && (today.getHours() * 60 + today.getMinutes()) >= (18 * 60))) {

                        //add reservation in the page
                        if (list[i].SeatReservation.entryTime === null || list[i].SeatReservation.entryTime === "null"
                            || list[i].SeatReservation.exitTime === null || list[i].SeatReservation.exitTime === "null") {
                            existReservationForToday = true;
                            var div = document.createElement("div");
                            var h2 = document.createElement("h2");
                            var slot = list[i].SeatReservation.TimeSlot.hour_range;
                            var seat = list[i].SeatReservation.LibrarySeat.seatID;
                            var room = list[i].SeatReservation.LibrarySeat.room;
                            h2.innerText="Today's reservation info";
                            var divLabel = document.createElement("div");
                            divLabel.style.margin = "10px";
                            divLabel.appendChild(h2);
                            div.appendChild(divLabel);
                            var divLabel = document.createElement("div");
                            divLabel.style.margin = "10px"
                            var label = document.createElement("label");
                            label.innerText = "Room "+room;
                            divLabel.appendChild(label);
                            div.appendChild(divLabel);
                            var divLabel = document.createElement("div");
                            divLabel.style.margin = "10px";
                            var label = document.createElement("label");
                            label.innerText = "Seat "+seat;
                            divLabel.appendChild(label);
                            div.appendChild(divLabel);
                            var divLabel = document.createElement("div");
                            divLabel.style.margin = "10px";
                            var label = document.createElement("label");
                            label.innerText = "Slot "+slot;
                            divLabel.appendChild(label);
                            div.appendChild(divLabel);

                            if (list[i].SeatReservation.entryTime === null || list[i].SeatReservation.entryTime === "null") {
                                var button = document.createElement("button");
                                button.name = "entryButton";
                                button.innerText = "Set Entry Time";
                                button.setAttribute("onclick", "updateReservation(\"" + list[i].SeatReservation.alphanumericCode + "\",true)");
                                button.style.marginTop="10px";
                                //div.appendChild(label);
                                div.appendChild(button);
                            } else if (list[i].SeatReservation.exitTime === null || list[i].SeatReservation.exitTime === "null") {
                                var button = document.createElement("button");
                                button.innerText = "Set Exit Time";
                                button.name = "exitButton";
                                var entryLabel = document.createElement("label");
                                entryLabel.innerText = " You are in the library from the " + list[i].SeatReservation.entryTime;
                                button.setAttribute("onclick", "updateReservation(\"" + list[i].SeatReservation.alphanumericCode + "\",false)");
                                button.style.marginTop="10px";
                                var divLabel = document.createElement("div");
                                divLabel.style.margin = "10px";
                                divLabel.appendChild(entryLabel);
                                div.appendChild(divLabel);
                                div.appendChild(button);
                            }
                            divInfo.appendChild(div);
                        }
                    }
                }
            }
            if(!existReservationForToday){
                var label = document.createElement("label");
                label.innerText = "You have no reservation for today.";
                divInfo.appendChild(label);
            }

        }else if(req.status==400){
            alert("Wrong Request!");

        }else if(req.status==404){
            alert("Resource not found!");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}

/**
 * Update the reservation setting up the entry or exit time for the {@param alphaCode} reservation.
 * In particular if isEntry is true we are setting the entry time, otherwise the exit time.
 *
 * @param alphaCode
 * @param isEntry
 * @returns {boolean}
 */
function updateReservation(alphaCode, isEntry){
    var httpRequest = new XMLHttpRequest();
    var url = contextPath + "seatReserv/update";
    if(isEntry)
        url+="/entry";
    else
        url+="/exit";
    url+="/"+alphaCode;
    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    //console.log(url);
    httpRequest.onreadystatechange = function () {
        confirmUpdate(httpRequest);
    };
    httpRequest.open('UPDATE', url);
    httpRequest.send();
}

/**
 * Callback for the updateReservation xmlhttprequest
 * @param req
 */
function confirmUpdate(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        //document.getElementById("errorBox").innerHTML=req.responseText;
        console.log(req.responseText)
        if (req.status == 200) {
            //this is the json returned

            loadInfoReservation()
        }else if(req.status==400){
            alert("Wrong Request!");

        }else if(req.status==404){
            alert("Resource not found!");
        }else if(req.status==409){
            var error = JSON.parse(req.responseText);
            alert(error['error'].message);
        }
        else {
            alert("problem processing the request");
        }
    }
}