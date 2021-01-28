CREATE DATABASE `inmane` character set utf8mb4;
CREATE USER 'inmnuser' identified by 'inmnpass';
GRANT ALL PRIVILEGES ON `inmane`.* TO 'inmnuser'@'%' IDENTIFIED BY 'inmnpass';
FLUSH PRIVILEGES;

SHOW GRANTS FOR 'inmnuser';

