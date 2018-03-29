INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE, RUNNING) VALUES (3, '2017-01-29', 'Jitty Swisssystem Cup', '2017-01-30', 1);
INSERT INTO TABLE_SETTINGS (ID, TABLE_COUNT) VALUES (3, 6);
UPDATE TOURNAMENT
SET TS_ID = 3
WHERE ID = 3;

INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, SYSTEM_TYPE, START_TTR, END_TTR, START_TIME, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, AGE_GROUP, STATUS)
VALUES (6, 3, 'Schweizer System', 2, 0, 3000, NOW(), TRUE, TRUE, FALSE, -1, 'Damen/Herren', 'NOTSTARTED');

--
-- INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENT_ID) SELECT
--                                                     id,
--                                                     3
--                                                   FROM TOURNAMENT_PLAYER
--                                                   WHERE id > 13;
-- INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) SELECT
--                                                  id,
--                                                  6
--                                                FROM TOURNAMENT_PLAYER
--                                                WHERE id > 13;
--

UPDATE USER SET TOURNAMENT_ID = 3
WHERE ID = 1;
