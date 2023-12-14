-- Create new role
-- Using postgres user

CREATE ROLE studyteam
    LOGIN
    CREATEROLE
    CREATEDB
    REPLICATION
    ENCRYPTED PASSWORD 'admin';

