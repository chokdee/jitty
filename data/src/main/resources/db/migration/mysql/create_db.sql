CREATE DATABASE jitty;
CREATE USER 'jitty'@'localhost'
  IDENTIFIED BY 'jitty';
GRANT ALL ON jitty.* TO 'jitty'@'localhost';