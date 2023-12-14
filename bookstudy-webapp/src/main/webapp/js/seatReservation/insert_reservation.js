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
    document.getElementById("seatReservDate").addEventListener("change", changeDate);
    document.getElementById("seatReservSlot").addEventListener("change", changeSlot);
    document.getElementById("submitSeatReservation").addEventListener("click", insertSeatReservation);
    let today = new Date().toISOString().substr(0, 10);
    document.getElementById("seatReservDate").value = today;
    var event = new Event('change');



    document.getElementById("seatReservDate").dispatchEvent(event);
});

/**
 * Get the slots for a given date
 */
function changeDate(){
    var date = document.getElementById("seatReservDate").value;
    document.getElementById("errorDate").innerText = "";
    var yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    if ((new Date(date)).getTime() < yesterday.getTime()) {
        //The date insert is behind today. Error
        document.getElementById("errorDate").innerText = "You cannot insert a date in the past!";
        let today = new Date().toISOString().substr(0, 10);
        document.getElementById("seatReservDate").value = today;
    }
    var httpRequest = new XMLHttpRequest();
    var url = contextPath + "seatReserv/free/" + date;
    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    //console.log(url);
    httpRequest.onreadystatechange = function () {
        writeSlots(httpRequest)
    };
    httpRequest.open('GET', url);
    httpRequest.send();
}

/**
 * Get the freeSeat for a given slot when it changes
 */
function changeSlot(){
    var slot = document.getElementById("seatReservSlot").value;
    var date = document.getElementById("seatReservDate").value;
    document.getElementById("errorDate").innerText = "";
    var yesterday = new Date();
    yesterday.setDate(yesterday.getDate()-1);
    if((new Date(date)).getTime()<yesterday.getTime()){
        //The date insert is behind today. Error
        document.getElementById("errorDate").innerText = "You cannot insert a date in the past!";
        let today = new Date().toISOString().substr(0, 10);
        document.getElementById("seatReservDate").value = today;
    }
    var httpRequest = new XMLHttpRequest();
    var url = contextPath+"seatReserv/free/"+date+"/"+slot;
    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    //console.log(url);
    httpRequest.onreadystatechange = function (){ writeFreeSeat(httpRequest)};
    httpRequest.open('GET', url);
    httpRequest.send();
}

/**
 * Fill the select of the slots.
 */
function writeSlots(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        //document.getElementById("errorBox").innerHTML=req.responseText;
        //console.log(req.responseText)
        if (req.status == 200) {
            //this is the json returned
            //{"resource-list":[{"TimeSlot":{"slotID":"37","hour_range":"morning","date":"2021-05-25"}},{"TimeSlot":{"slotID":"38","hour_range":"afternoon","date":"2021-05-25"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            $("#seatReservSlot").empty();
            //fill the select
            var selectSlot = document.getElementById("seatReservSlot");

            for(var i=0; i < list.length;i++){
                var slot = list[i]['TimeSlot'];
                var year = slot.date.split("-")[0];
                var month = slot.date.split("-")[1];
                var day = slot.date.split("-")[2];
                var today = new Date();
                if(today.getFullYear() === parseInt(year) && today.getMonth()+1===parseInt(month) && today.getDate()===parseInt(day)) {
                    if ((today.getHours() * 60 + today.getMinutes()) < (13 * 60) ){//&& (today.getHours() * 60 + today.getMinutes()) >= (8 * 60)) {
                        //I am in the morning
                        console.log("morning");
                        var option = document.createElement("option");
                        option.setAttribute("value", slot.slotID);
                        option.innerText = slot.hour_range;
                        selectSlot.appendChild(option);
                    }
                    else if((today.getHours()*60 + today.getMinutes())<(18*60) && (today.getHours()*60 + today.getMinutes())>=(13*60) ) {
                        //I am in the afternoon
                        console.log("afternoon");
                        if (slot.hour_range != "morning") {
                            var option = document.createElement("option");
                            option.setAttribute("value", slot.slotID);
                            option.innerText = slot.hour_range;
                            selectSlot.appendChild(option);
                        }
                    }else if((today.getHours()*60 + today.getMinutes())<(24*60) && (today.getHours()*60 + today.getMinutes())>=(18*60) ){
                        //I am in the evening
                        console.log("evening");
                        if(slot.hour_range === "evening") {
                            var option = document.createElement("option");
                            option.setAttribute("value", slot.slotID);
                            option.innerText = slot.hour_range;
                            selectSlot.appendChild(option);
                        }
                    }
                }else {
                    console.log("ALLDAY");
                    var option = document.createElement("option");
                    option.setAttribute("value", slot.slotID);
                    option.innerText = slot.hour_range;
                    selectSlot.appendChild(option);
                }
            }
            changeSlot();
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
 * Fill the select of the free seat for the slot.
 */
function writeFreeSeat(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        //document.getElementById("errorBox").innerHTML=req.responseText;
        //console.log(req.responseText)
        if (req.status == 200) {
            //this is the json returned
            //{"resource-list":[{"LibrarySeat":{"seatID":"2","Room":"R3"}},{"LibrarySeat":{"seatID":"12","Room":"R2"}},{"LibrarySeat":{"seatID":"8","Room":"R2"}},{"LibrarySeat":{"seatID":"15","Room":"R1"}},{"LibrarySeat":{"seatID":"20","Room":"R3"}},{"LibrarySeat":{"seatID":"1","Room":"R1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            //console.log(list);

            $("#seatReservSeat").empty();
            var selectSeat = document.getElementById("seatReservSeat");

            for(var i=0; i < list.length;i++){
                var seat = list[i]['LibrarySeat'];
                var option = document.createElement("option");
                option.setAttribute("value",seat.seatID);
                option.innerText = seat.seatID+" in room "+seat.Room;
                selectSeat.appendChild(option);
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
 * Insert a new reservation
 */
function insertSeatReservation(){
    var seatReservation = new Object();
    seatReservation.alphanumericCode = "";
    seatReservation.date = document.getElementsByName("date")[0].value;


    var timeSlot = new Object();
    timeSlot.slotID = document.getElementsByName("slotID")[0].value;
    seatReservation.TimeSlot = timeSlot;

    var librarySeat = new Object();
    librarySeat.seatID = document.getElementsByName("seatID")[0].value;
    seatReservation.LibrarySeat = librarySeat;

    var httpRequest = new XMLHttpRequest();
    var url = contextPath+"seatReserv/insert";
    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    var json_obj = new Object();
    json_obj.SeatReservation = seatReservation;
    console.log(url);
    console.log(JSON.stringify(json_obj));
    httpRequest.onload = function (){ insertResult(httpRequest)};

    httpRequest.open('POST', url);
    httpRequest.send(JSON.stringify(json_obj));
}

/**
 * Callback of the insertReservation HttpRequest
 * @param req
 */
function insertResult(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        //alert(req.responseText);
        //document.getElementById("responseInsert").innerHTML=req.responseText;
        if (req.status == 200) {
            document.getElementById("errorBox").style.color="green";
            document.getElementById("errorBox").innerText="Reservation Booked, thank you!";
            changeSlot();
            console.log("booked");
            //window.location.href = (contextPath+"/jsp/homepage.jsp");
        }else if(req.status==400){
            alert("Bad Request!");
            window.location.href = (contextPath+"/jsp/homepage.jsp");
        }else if(req.status==409){
            document.getElementById("errorBox").style.color="red";
            document.getElementById("errorBox").style.webkitAnimationName = "";
            setTimeout(function ()
            {
                document.getElementById("errorBox").style.webkitAnimationName = "shake";
            }, 0);
            document.getElementById("errorBox").innerText="You have already booked a seat for this slot, please choose another slot!";
            console.log("already booked");
            //window.location.href = (contextPath+"/jsp/homepage.jsp");
        } else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}