INSERT INTO user (name, email, locked, loginname, password) VALUES ('username', 'user@bla.de', 0, 'user', '42');
INSERT INTO user (name, email, locked, loginname, password) VALUES ('adminname', 'user@bla.de', 0, 'admin', '42');
INSERT INTO user (name, email, locked, loginname, password) VALUES ('superusername', 'user@bla.de', 0, 'superuser', '42');



INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (1, 'Badischer TTV', 'BaTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (2, 'Bayerischer TTV', 'BTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (3, 'Berliner TTV', 'BETTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (4, 'TTV Brandenburg', 'TTVB');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (5, 'FTT Bremen', 'FTTB');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (6, 'Hamburger TTV', 'Hamburger TTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (7, 'Hessischer TTV', 'HTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (8,'TTV Mecklenburg-Vorpommern', 'TTVMV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (9,'TTV Niedersachsen', 'TTVN');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (10,'Pfälzischer TTV', 'PTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (11,'Rheinhessischer TTV', 'RTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (12,'TTV Rheinland', 'TTVR');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (13,'Saarländischer TTB', 'STTB');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (14,'Sächsischer TTV', 'STTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (15,'TTV Sachsen-Anhalt', 'TTVSA');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (16,'TTV Schleswig-Holstein', 'TTVSH');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (17,'Südbadischer Tischtennis-Verband', 'SBTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (18,'Thüringer TTV', 'TTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (19,'TTV Württemberg-Hohenzollern', 'TTVWH');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (20,'Westdeutscher TTV', 'WTTV');
INSERT INTO ASSOCIATION(ID, LONGNAME,SHORTNAME) VALUES (21,'Ausland', 'Ausland');


INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TV Scheven',20);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TSV Eisenberg',18);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('SV Grambke Oslebshausen',5);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('Ahlener SG',20);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('FC Ruthe',19);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('Switzerland',21);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('1. FC Bruchsal',1);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('1. TC Ittersbach',1);
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('1. TTC Ketsch',1);


INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE, TABLE_COUNT, RUNNING) VALUES (1, '2015-06-03', 'Jitty Open 2015', '2015-06-01', 8, 0);
INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE, TABLE_COUNT, RUNNING) VALUES (2, '2016-12-29', 'Jitty Open 2016', '2016-12-01', 8, 1);

INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, START_TIME, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP) VALUES (1, 2, 'A-Klasse', 1800, 3000, NOW() + INTERVAL '1' HOUR, TRUE, FALSE, FALSE, -1, 1, 'Damen/Herren');
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP) VALUES (2, 2, 'B-Klasse', 0, 1850, TRUE, FALSE, FALSE, -1, 1, 'Damen/Herren');
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP) VALUES (3, 2, 'C-Klasse', 0, 1600, TRUE, FALSE, FALSE, -1, 1, 'Damen/Herren');

INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (1, 'Timo', 'Boll', 'm', 1, 20, 2499, 2500);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (2, 'Palme', 'Saatkrähe', 'w', 1, 1, 1488, 1500);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (3, 'Kartoffel', 'Ohrentaucher', 'm', 3, 2, 1799, 1805);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (4, 'Peter', 'Meier', 'm', 3, 2, 1799, 1805);

INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (1, 1);
INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENTS_ID) VALUES (1, 2);
INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENTS_ID) VALUES (2, 2);
INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENTS_ID) VALUES (3, 2);
INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENTS_ID) VALUES (4, 2);