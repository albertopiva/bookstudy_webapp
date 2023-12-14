/**
 * Manage the request about the visualization of reservation in the library.
 *
 * @author BookStudy Team
 * @version 1.0
 * @since 1.0
 */

document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    urlGetReservation = contextPath + "seatReserv/view/user";
    console.log(urlGetReservation);
    genericGETRequest(urlGetReservation, fillReservation);
});

/**
 * Fill the reservation table for the user.
 * @param req
 */
function fillReservation(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        //document.getElementById("errorBox").innerHTML=req.responseText;
        console.log(req.responseText)
        if (req.status == 200) {
            //this is the json returned
            //
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);
            $("#reservationResult").empty();
            //fill the select
            var divReservationResult = document.getElementById("reservationResult");
            var table = document.createElement("table");
            var thead = document.createElement("thead");

            var th = document.createElement("th");
            th.innerHTML="Date";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Slot";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Seat Number";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Room";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Entry Time";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="Exit Time";
            thead.append(th);

            table.append(thead);

            var tbody = document.createElement("tbody");
            for(var i=0; i < list.length;i++){
                var reservation = list[i]['SeatReservation'];
                var tr = document.createElement("tr");

                var td = document.createElement("td");
                td.innerHTML=reservation.date;
                tr.append(td);

                var td = document.createElement("td");
                td.innerHTML=reservation.TimeSlot.hour_range;
                tr.append(td);

                var td = document.createElement("td");
                td.innerHTML=reservation.LibrarySeat.seatID;
                tr.append(td);

                var td = document.createElement("td");
                td.innerHTML=reservation.LibrarySeat.room;
                tr.append(td);

                var td = document.createElement("td");
                if(reservation.entryTime === null || reservation.entryTime === "null" )
                    td.innerHTML="Not Provided";
                else
                    td.innerHTML=reservation.entryTime;
                tr.append(td);

                var td = document.createElement("td");
                if(reservation.exitTime === null || reservation.exitTime === "null")
                    td.innerHTML="Not Provided";
                else
                    td.innerHTML=reservation.exitTime;
                tr.append(td);

                tbody.append(tr);
            }
            table.append(tbody);
            divReservationResult.append(table);
        }else if(req.status==400){
            alert("Wrong Request!");

        }else if(req.status==404){
            alert("No seat reservation not found!");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}