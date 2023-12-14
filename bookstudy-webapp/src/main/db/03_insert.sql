--Insert operation

--User

INSERT INTO libraryapp.user(phonenumber, password, salt, name, surname, role) VALUES
('03859251930', '4227FF9525730839C8D6BD935504DB58', 'salt10','Adriana', 'Lucchesi', 'librarian'), --pass: TAA_3134256
('03171085676', 'CDDEFEA9B20A6FD4A62CEB2B7E5FC6CE', 'salt11','Sofia', 'Marchesi', 'association_admin'), --pass: S.M1983565
('03782853213', 'C57B488815C6BDB5366E5CD8995CD879', 'salt12','Clemente', 'Fonti', 'cultural_office'), --pass: Fonti1958!
('03282107865', '6123DCE98524AB0950A502CE14C9E57F', 'salt13','Agnese', 'Fiorentini', 'user'), --pass: PD_28602565
('03336239657', '4DEAC36F29BEAE81DD56C6EF671E8B92', 'salt14','Valerio', 'Bianchi', 'association_member'); -- pass: PEFE18!!566

--User's data

INSERT INTO libraryapp.userdata(userid, hometown, bornregion, birthday, city, region) VALUES
('03859251930', 'Nuoro', 'Sardegna', '1962-03-31', 'Trento', 'Trentino-Alto Adige'),
('03171085676', 'Trapani', 'Sicilia', '1983-06-04', 'Vicenza', 'Veneto'),
('03782853213', 'Padova', 'Veneto', '1958-05-29', 'Padova', 'Veneto'),
('03336239657', 'Pesaro', 'Marche', '1089-10-18', 'Ferrara', 'Emilia-Romagna');

--Enroll

INSERT INTO libraryapp.enroll(userid, newperson) VALUES
('03171085676', '03336239657');

--Seats


INSERT INTO libraryapp.librarySeat(id, room) VALUES
(01, 'R1'),
(15, 'R1'),
(08, 'R2'),
(12, 'R2'),
(02, 'R3'),
(20, 'R3');

--timeSlot

INSERT INTO libraryapp.timeslot(id, hour_range, date) VALUES
(DEFAULT,'morning','2020-02-02'),
(DEFAULT,'afternoon','2020-11-16'),
(DEFAULT,'evening','2021-05-07');


--Organizer

INSERT INTO libraryapp.organizer(phonenumber, password, salt, name, surname, association) VALUES
('03713536651', 'C14A0F457167C3EA748DB2A93B9A97F5', 'salt15', 'Serena', 'Russo','Hollywood Video'), --pass: Se_Ru_98dfgd
('03318280685', 'CC01FB0BAC36D3229DDADABE5D92B3A9', 'salt16', 'Ettore', 'Napolitano', 'Museum Company'), --pass: Ahph4ooldfgdf
('03115440293', 'EAE483A17C595D95322A4DC6091C6350', 'salt17', 'Michelangelo', 'Boni', 'Handy Andy Company'); --pass: dooTh7eidsefw

--Conference Room

INSERT INTO libraryapp.conferenceroom VALUES
('CF1', 'red_room'),
('CF2', 'green_room');

--Conference

INSERT INTO libraryapp.conference(alphanumericcode, date, title, description,organizerid,conferenceroomid) VALUES
('s7Z1A9iY','2020-10-20','Spanish Lesson','Grammar lesson','03713536651','CF1'),
('ky5teftX','2021-05-25','Java Programmi','Study the basic of JAVA','03318280685','CF2');

--ConferenceBook

INSERT INTO libraryapp.conferenceBook(userid, conferenceid) VALUES
('03282107865','s7Z1A9iY'),
('03282107865','ky5teftX');

--SeatReservation

INSERT INTO libraryapp.seatReservation(alphanumericcode, date, entrytime, exittime, slotid, seatid, userid) VALUES
('tVN6jz0923', '2020-02-02', '9:03:00+01', '11:58:00+01', 01, 15, '03282107865'),
('CgfZgDPHfK', '2020-02-02', '21:10:00+01', '23:45:00+01', 01, 08, '03859251930'),
('S73SAKxxNp', '2020-11-16', '10:00:00+01', '11:35:00+01', 02, 12, '03282107865'),
('BCf4NdQZTB', '2021-7-22', NULL, NULL, 03, 08, '03859251930')