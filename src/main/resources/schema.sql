CREATE PROFILE INMN_PROFILE LIMIT
 SESSIONS_PER_USER DEFAULT
 CPU_PER_SESSION DEFAULT
 CPU_PER_CALL DEFAULT
 CONNECT_TIME DEFAULT
 IDLE_TIME DEFAULT
 LOGICAL_READS_PER_SESSION DEFAULT
 LOGICAL_READS_PER_CALL DEFAULT
 COMPOSITE_LIMIT DEFAULT
 PRIVATE_SGA DEFAULT
 FAILED_LOGIN_ATTEMPTS UNLIMITED
 PASSWORD_LIFE_TIME UNLIMITED
 PASSWORD_LOCK_TIME UNLIMITED
 PASSWORD_GRACE_TIME UNLIMITED;

Create User INMNUSR Identified By "inmnpass" PROFILE INMN_PROFILE;
Grant CONNECT              To INMNUSR With Admin Option;
Grant UNLIMITED TABLESPACE To INMNUSR With Admin Option;
Alter User INMNUSR Default Role All;

Grant CREATE TABLE TO INMNUSR With Admin Option;
Grant CREATE SEQUENCE TO INMNUSR With Admin Option;
Grant CREATE VIEW TO INMNUSR With Admin Option;