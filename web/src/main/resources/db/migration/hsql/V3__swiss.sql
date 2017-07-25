INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE, TABLE_COUNT, RUNNING) VALUES (3, '2017-01-29', 'Jitty Swisssystem Cup', '2017-01-30', 6, 1);

INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, SYSTEM_TYPE, START_TTR, END_TTR, START_TIME, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, AGE_GROUP) VALUES (6, 3, 'Schweizer System', 2, 0, 3000, NOW(), TRUE, TRUE, FALSE, -1, 'Damen/Herren');


INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENTS_ID) SELECT
                                                    id,
                                                    3
                                                  FROM TOURNAMENT_PLAYER
                                                  WHERE id > 13;
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) SELECT
                                                 id,
                                                 6
                                               FROM TOURNAMENT_PLAYER
                                               WHERE id > 13;


UPDATE USER SET TOURNAMENT_ID = 3
WHERE ID = 1;
