-- Using the new role
-- Database creation
CREATE DATABASE appDB
    OWNER studyteam
    ENCODING = 'UTF8';

-- Connect to the database
\c appdb

-- Create new Schema
DROP SCHEMA IF EXISTS libraryapp CASCADE;
CREATE SCHEMA libraryapp;
ALTER SCHEMA libraryapp OWNER TO studyteam;


-- Create domain
CREATE DOMAIN libraryapp.passwd AS VARCHAR(254)
    CONSTRAINT passwordconstr CHECK (((VALUE)::text ~* '[A-Za-z0-9._%!]{8,}'::text));

CREATE DOMAIN libraryapp.email AS VARCHAR(254)
    CONSTRAINT emailconstr CHECK (((VALUE)::text ~* '^[A-Za-z0-9._%]+@[A-Za-z0-9.]+[.][A-Za-z]+$'::text));

CREATE DOMAIN libraryapp.phone AS VARCHAR(12)
    CONSTRAINT phoneconstr CHECK (((VALUE)::text ~* '[0-9 ]{9,}'::text));

CREATE TYPE roleType as ENUM(
    'librarian',
    'cultural_office',
    'association_admin',
    'association_member',
    'user'
);

CREATE TYPE daySlot as ENUM(
    'morning',
    'afternoon',
    'evening'
);

-- Table creation
CREATE TABLE libraryapp.user(
    phoneNumber libraryapp.phone PRIMARY KEY,
    password libraryapp.passwd NOT NULL,
    salt VARCHAR(15) NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    role roleType DEFAULT 'user'
);

CREATE TABLE libraryapp.userData(
    userID libraryapp.phone PRIMARY KEY,
    homeTown VARCHAR(50) NOT NULL,
    bornRegion VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    city VARCHAR(50) NOT NULL,
    region VARCHAR(50) NOT NULL,
    FOREIGN KEY(userID) REFERENCES libraryapp.user(phoneNumber)
);

-- Table only for cultural association
CREATE TABLE libraryapp.enroll(
    userID libraryapp.phone NOT NULL,
    newPerson libraryapp.phone PRIMARY KEY,
    FOREIGN KEY (userID) REFERENCES libraryapp.user(phoneNumber),
    FOREIGN KEY (newPerson) REFERENCES libraryapp.user(phoneNumber)
);

CREATE TABLE libraryapp.librarySeat(
    id  INTEGER PRIMARY KEY,
    room VARCHAR(10)
);

CREATE TABLE libraryapp.timeSlot(
    id  BIGSERIAL PRIMARY KEY,
    hour_range daySlot NOT NULL,
    date DATE NOT NULL
);

CREATE TABLE libraryapp.organizer(
    phoneNumber libraryapp.phone PRIMARY KEY,
    password libraryapp.passwd NOT NULL,
    salt VARCHAR(15) NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    association VARCHAR(70) NOT NULL
);

CREATE TABLE libraryapp.conferenceRoom(
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(30)
);

CREATE TABLE libraryapp.conference(
    alphanumericCode VARCHAR(50) PRIMARY KEY,
    date DATE NOT NULL,
    title VARCHAR(50),
    description VARCHAR(1024),
    organizerID libraryapp.phone NOT NULL,
    conferenceRoomID VARCHAR(10) NOT NULL,
    FOREIGN KEY (organizerID) REFERENCES libraryapp.organizer(phoneNumber),
    FOREIGN KEY (conferenceRoomID) REFERENCES libraryapp.conferenceRoom(id)
);

CREATE TABLE libraryapp.conferenceBook(
    userID libraryapp.phone,
    conferenceID VARCHAR(50),
    PRIMARY KEY (userID,conferenceID),
    FOREIGN KEY (userID) REFERENCES libraryapp.user(phoneNumber),
    FOREIGN KEY (conferenceID) REFERENCES libraryapp.conference(alphanumericCode) ON DELETE CASCADE
);

CREATE TABLE libraryapp.seatReservation(
    alphanumericCode VARCHAR(50) PRIMARY KEY,
    date DATE NOT NULL,
    entryTime TIME WITH TIME ZONE DEFAULT NULL,
    exitTime TIME WITH TIME ZONE DEFAULT NULL,
    slotID BIGINT NOT NULL,
    seatID INTEGER NOT NULL,
    userID libraryapp.phone NOT NULL,
    FOREIGN KEY (slotID) REFERENCES  libraryapp.timeSlot(id),
    FOREIGN KEY (seatID) REFERENCES  libraryapp.librarySeat(id),
    FOREIGN KEY (userID) REFERENCES libraryapp.user(phoneNumber)
);


-- give the permission of connection
GRANT CONNECT ON DATABASE appdb TO studyteam;

--give the permission in the schema
GRANT USAGE ON SCHEMA libraryapp TO studyteam;

--give all the permission in all tables of the schema
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA libraryapp TO studyteam;

--give all the permission in all sequences of the schema
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA libraryapp TO studyteam;