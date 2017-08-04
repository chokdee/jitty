
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (1, 'Peter Meier', 'pmeier@bla.de', 0, 'user', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (2, 'Administrator', 'admin@bla.de', 0, 'admin', '43');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (3, 'Super User', 'superuser@bla.de', 0, 'superuser', '44');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (4, 'Fritz Müller', 'fritz@mueller.de', 0, 'mmueller', '45');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (5, 'Karla Schweinchen', 'karla@gmx.net', 0, 'karla', '46');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (6, 'Mauer Blümchen', 'mauer@blum.de', 0, 'mauer', '47');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (7, 'Valentino Rossi', 'rossi@rossi.it', 0, 'rossi', '48');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (8, 'Karl Foggarty', 'k@k.com', 0, 'foggy', '49');


INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE, TABLE_COUNT, RUNNING) VALUES (1, '2015-06-03', 'Jitty Open 2015', '2015-06-01', 8, 0);
INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE, TABLE_COUNT, RUNNING) VALUES (2, '2016-12-29', 'Jitty Open 2016', '2016-12-01', 4, 1);

INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, START_TIME, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP, STATUS)
VALUES (1, 2, 'A-Klasse', 1800, 3000, NOW() + INTERVAL '1' HOUR, TRUE, FALSE, FALSE, -1, 1, 'Damen/Herren', 'NOTSTARTED');
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP, STATUS)
VALUES (2, 2, 'B-Klasse', 0, 1850, TRUE, FALSE, FALSE, 0, 1, 'Damen/Herren', 'NOTSTARTED');
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP, STATUS)
VALUES (3, 2, 'C-Klasse', 0, 1600, TRUE, FALSE, FALSE, -1, 1, 'Damen/Herren', 'NOTSTARTED');
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP, STATUS)
VALUES (4, 2, 'D-Klasse', 0, 1400, TRUE, FALSE, FALSE, -1, 1, 'Damen/Herren', 'NOTSTARTED');
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR, START_TIME, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, SYSTEM_TYPE, AGE_GROUP, STATUS)
VALUES (5, 2, 'E-Klasse', 0, 1200, NOW(), TRUE, FALSE, FALSE, -1, 1, 'Damen/Herren', 'NOTSTARTED');



-- one tc has a group
-- INSERT INTO TOURNAMENT_GROUP(ID, NAME, TC_ID, ACTIVE_PHASE) VALUES (1,'1', 5, -1);
-- swiss
UPDATE USER SET TOURNAMENT_ID = 2 WHERE ID = 1;


INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (1, 'Badischer TTV', 'BaTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (2, 'Bayerischer TTV', 'BTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (3, 'Berliner TTV', 'BETTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (4, 'TTV Brandenburg', 'TTVB');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (5, 'FTT Bremen', 'FTTB');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (6, 'Hamburger TTV', 'Hamburger TTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (7, 'Hessischer TTV', 'HTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (8, 'TTV Mecklenburg-Vorpommern', 'TTVMV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (9, 'TTV Niedersachsen', 'TTVN');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (10, 'Pfälzischer TTV', 'PTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (11, 'Rheinhessischer TTV', 'RTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (12, 'TTV Rheinland', 'TTVR');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (13, 'Saarländischer TTB', 'STTB');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (14, 'Sächsischer TTV', 'STTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (15, 'TTV Sachsen-Anhalt', 'TTVSA');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (16, 'TTV Schleswig-Holstein', 'TTVSH');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (17, 'Südbadischer Tischtennis-Verband', 'SBTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (18, 'Thüringer TTV', 'TTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (19, 'TTV Württemberg-Hohenzollern', 'TTVWH');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (20, 'Westdeutscher TTV', 'WTTV');
INSERT INTO ASSOCIATION (ID, LONGNAME, SHORTNAME) VALUES (21, 'Ausland', 'Ausland');


INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TV Scheven',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TSV Eisenberg',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'TTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('SV Grambke Oslebshausen',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'FTTB'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('Ahlener SG',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('FC Ruthe',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'TTVN'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('Herpfer SV 07',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'TTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('Switzerland',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'Ausland'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TTZ Altstadt-Kirkel',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'STTB'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('1. FC Bruchsal',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('1. TC Ittersbach',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('1. TTC Ketsch',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('ASV Eppelheim',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('ASV Grünwettersbach',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('BJC Buchen',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK 1927 Dossenheim',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Balzfeld',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Daxlanden',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Käfertal',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Mannheim Lindenhof',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Neckarhausen',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Ost Karlsruhe',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Rüppurr',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Schönau',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK St. Hildegard Käfertal Süd',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK St. Pius Neuostheim/Neuhermsheim',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Unterbalbach e.V. 1930',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Mannheim',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'BaTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TTG St. Augustin',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TTV BW Neudorf',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TTC Troisdorf',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('1. FC Köln',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('ESV Troisdorf',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TSV Seelscheid',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('DJK Eintracht Eitorf',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('TTV Viktoria Bonn',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));
INSERT INTO club(NAME,ASSOCIATION_ID) VALUES ('SSV Happerschoß',(SELECT  id FROM ASSOCIATION WHERE SHORTNAME = 'WTTV'));


INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (1, 'Timo', 'Boll', 'm', 1, 20, 2499, 2500);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (2, 'Palme', 'Saatkrähe', 'w', 2, 1, 1488, 1500);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (3, 'Kartoffel', 'Ohrentaucher', 'm', 3, 2, 1799, 1805);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (4, 'Noch', 'Keine Anmeldung', 'm', 4, 2, 1155, 1155);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (5, 'Ma', 'Long', 'm', 5, 2, 2400, 2400);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (6, 'Fan', 'Zhedong', 'm', 6, 2, 2299, 2299);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (7, 'Zhang', 'Jike', 'm', 7, 2, 2388, 2388);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (8, 'Xu', 'Xin', 'm', 8, 2, 2399, 2399);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (9, 'Nat', 'Melzer', 'w', 8, 2, 2555, 2555);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (10, 'Jan', 'Mizutani', 'm', 9, 2, 2344, 2344);

INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (1, 1);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (3, 2);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (5, 1);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (6, 1);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (7, 1);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (8, 1);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (9, 1);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (10, 1);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (2, 2);
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) VALUES (4, 3);

-- INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) select id, 6 from TOURNAMENT_PLAYER where id < 11;

-- Klasse B fast  komplett 14 Spieler
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (11, 'Karl', 'Müller', 'm', 1, 2, 1755, 1755);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (12, 'Peter', 'Meier', 'm', 2, 1, 1745, 1745);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (13, 'Michael', 'Mittermeier', 'm', 3, 3, 1699, 1699);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (14, 'Hans', 'Rose', 'm', 4, 3, 1700, 1700);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (15, 'Andreas', 'Michel', 'm', 5, 3, 1720, 1720);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (16, 'Jürgen', 'Keil', 'm', 6, 3, 1721, 1721);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (17, 'Manfred', 'Hilde', 'm', 7, 3, 1580, 1580);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (18, 'Matthias', 'Krut', 'm', 8, 3, 1540, 1540);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (19, 'Thorsten', 'Knuck', 'm', 9, 3, 1690, 1690);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (20, 'Erich', 'Kästner', 'm', 10, 3, 1702, 1702);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (21, 'Frank', 'Pusteblume', 'm', 11, 3, 1703, 1703);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (22, 'Timo', 'Baum', 'm', 12, 3, 1704, 1704);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (23, 'Nico', 'Poco', 'm', 13, 3, 1705, 1705);
INSERT INTO TOURNAMENT_PLAYER (ID, FIRSTNAME, LASTNAME, GENDER, CLUB_ID, ASSOCIATION_ID, QTTR, TTR) VALUES (24, 'Stefan', 'Ente', 'm', 14, 3, 1706, 1706);
INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENTS_ID) SELECT
                                                    id,
                                                    2
                                                  FROM TOURNAMENT_PLAYER
                                                  WHERE id > 11;
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) SELECT
                                                 id,
                                                 2
                                               FROM TOURNAMENT_PLAYER
                                               WHERE id > 11;

-- B-Klasse is running
INSERT INTO T_SYSTEM (ID,TC_ID) VALUES (1, 2);
INSERT INTO PHASE (ID, INDEXP, S_ID) VALUES (1, 0, 1);
INSERT INTO GROUP_PHASE(ID, GROUP_COUNT, PLAYER_PER_GROUP, QUALI_GROUP_COUNT) VALUES (1, 4, 4, 2);
INSERT INTO PHASE (ID, INDEXP, S_ID) VALUES (2, 1, 1);
INSERT INTO KO_PHASE(ID, KOFIELD_ID) VALUES (2, null);

INSERT INTO TOURNAMENT_GROUP (ID,NAME, GP_ID) VALUES (2, 'A', 1);
INSERT INTO TOURNAMENT_GROUP (ID,NAME, GP_ID) VALUES (3, 'B', 1);
INSERT INTO TOURNAMENT_GROUP (ID,NAME, GP_ID) VALUES (4, 'C', 1);
INSERT INTO TOURNAMENT_GROUP (ID,NAME, GP_ID) VALUES (5, 'D', 1);

INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (2, 11);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (2, 12);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (2, 13);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (2, 14);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (3, 15);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (3, 16);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (3, 17);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (3, 18);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (4, 19);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (4, 20);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (4, 21);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (5, 22);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (5, 23);
INSERT INTO TG_PLAYER (TOURNAMENT_GROUP_ID, PLAYER_ID) VALUES (5, 24);


UPDATE TOURNAMENT_CLASS SET START_TIME=NOW(), RUNNING=1 WHERE ID = 2;

INSERT INTO GAME_QUEUE (ID) VALUES(1);

INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(1,1, 1, NOW(), 'B-Klasse' , 2, 2, 11, 14, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (1,3,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (1,1);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (2,9,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (1,2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (3,11,1);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (1,3);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (4,11,5);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (1,4);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (5,6,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (1,5);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(3,1, 1, NOW(), 'B-Klasse' , 2, 2, 12, 13, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (9,8,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (3,9);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (10,8,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (3,10);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (11,2,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (3,11);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(4,1, 1, NOW(), 'B-Klasse' , 2, 2, 13, 11, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (12,7,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (4,12);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (13,7,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (4,13);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (14,11,2);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (4,14);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (15,11,0);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (4,15);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (16,5,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (4,16);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(5,1, 1, NOW(), 'B-Klasse' , 2, 2, 12, 14, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (17,4,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (5,17);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (18,4,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (5,18);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (19,11,0);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (5,19);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (20,2,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (5,20);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(6,1, 1, NOW(), 'B-Klasse' , 1, 2, 11, 12, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (21,11,8);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (6,21);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (22,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (6,22);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (23,11,5);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (6,23);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(7,1, 1, NOW(), 'B-Klasse' , 1, 2, 13, 14, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (24,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (7,24);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (25,11,0);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (7,25);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (26,11,4);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (7,26);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(8,1, 1, NOW(), 'B-Klasse' , 1, 3, 15, 18, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (27,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (8,27);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (28,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (8,28);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (29,0,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (8,29);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (30,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (8,30);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(9,1, 1, NOW(), 'B-Klasse' , 1, 3, 16, 17, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (31,11,9);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (9,31);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (32,11,4);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (9,32);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (33,4,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (9,33);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (34,11,1);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (9,34);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(10,1, 1, NOW(), 'B-Klasse' , 1, 3, 17, 15, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (35,11,7);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (10,35);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (36,11,0);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (10,36);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (37,4,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (10,37);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (38,1,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (10,38);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (39,11,6);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (10,39);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(11,1, 1, NOW(), 'B-Klasse' , 1, 3, 16, 18, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (40,11,8);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (11,40);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (41,11,9);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (11,41);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (42,2,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (11,42);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (43,11,4);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (11,43);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(12,1, 1, NOW(), 'B-Klasse' , 1, 3, 15, 16, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (44,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (12,44);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (45,11,2);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (12,45);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (46,11,7);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (12,46);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(13,1, 1, NOW(), 'B-Klasse' , 1, 3, 17, 18, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (47,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (13,47);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (48,11,6);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (13,48);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (49,1,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (13,49);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (50,9,11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (13,50);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (51,11,5);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (13,51);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(14,1, 1, NOW(), 'B-Klasse' , 1, 4, 20, 21, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (52,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (14,52);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (53,11,6);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (14,53);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (54,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (14,54);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(15,1, 2, NOW() , 'B-Klasse',1, 4, 21, 19, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (55,2, 11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (15,55);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (56,6, 11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (15,56);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (57, 3, 11);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (15,57);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(16,1, 1, NOW(), 'B-Klasse' , 1, 4, 19, 20, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (58,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (16,58);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (59,11,6);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (16,59);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (60,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (16,60);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(17,1, 1, NOW(), 'B-Klasse' , 1, 5, 23, 24, 2);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (61,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (17,61);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (62,11,6);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (17,62);
-- INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (63,11,3);
-- INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (17,63);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(18,0, 1, NULL, 'B-Klasse', -1, 5, 24, 22, 2);
INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, TOURNAMENT_CLASS_NAME,  WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID, T_ID) VALUES(19,0, 0, NULL, 'B-Klasse', -1, 5, 22, 23, 2);
update TOURNAMENT_SINGLE_GAME set WINNER = -1, CALLED = 0, PLAYED = 0 , START_TIME = NULL where PLAYED = 1;

UPDATE TOURNAMENT_SINGLE_GAME set GAME_QUEUE_ID =1;