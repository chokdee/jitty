INSERT INTO user (id, name, email, locked, loginname, password) VALUES (1, 'Peter Meier', 'pmeier@bla.de', 0, 'user', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (2, 'Administrator', 'admin@bla.de', 0, 'admin', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (3, 'Super User', 'superuser@bla.de', 0, 'superuser', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (4, 'Fritz Müller', 'fritz@mueller.de', 0, 'user', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (5, 'Karla Schweinchen', 'karla@gmx.net', 0, 'user', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (6, 'Mauer Blümchen', 'mauer@blum.de', 0, 'user', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (7, 'Valentino Rossi', 'rossi@rossi.it', 0, 'user', '42');
INSERT INTO user (id, name, email, locked, loginname, password) VALUES (8, 'Karl Foggarty', 'k@k.com', 0, 'user', '42');


INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE) VALUES (1, '2015-06-03', 'Jitty Open 2015', '2015-06-01');
INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE) VALUES (2, '2016-06-29', 'Jitty Open 2016', '2016-06-27');

INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR) VALUES (1, 2, 'A-Klasse', 0, 3000);
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR) VALUES (2, 2, 'B-Klasse', 0, 1850);
INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, START_TTR, END_TTR) VALUES (3, 2, 'C-Klasse', 0, 1600);

UPDATE USER SET TOURNAMENT_ID = 2 WHERE ID = 1;

INSERT INTO TOURNAMENT_PLAYER (FIRSTNAME, LASTNAME, GENDER, CLUBNAME, ASSOCIATION,QTTR, TTR) VALUES ('Timo', 'Boll', 'm', 'Borussia Düsseldorf', 'WTTV',  2499, 2500);