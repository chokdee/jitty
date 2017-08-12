INSERT INTO TOURNAMENT (ID, END_DATE, NAME, START_DATE, TABLE_COUNT, RUNNING) VALUES (3, '2017-01-29', 'Jitty Swisssystem Cup', '2017-01-30', 6, 1);

INSERT INTO TOURNAMENT_CLASS (ID, T_ID, NAME, SYSTEM_TYPE, START_TTR, END_TTR, START_TIME, OPEN_FOR_MEN, OPEN_FOR_WOMEN, RUNNING, ACTIVE_PHASE, AGE_GROUP) VALUES (6, 3, 'Schweizer System', 2, 0, 3000, NOW(), TRUE, TRUE, FALSE, -1, 'Damen/Herren');


INSERT INTO T_PLAYER (PLAYERS_ID, TOURNAMENT_ID) SELECT
                                                    id,
                                                    3
                                                 FROM TOURNAMENT_PLAYER
                                                  WHERE id > 13;
INSERT INTO TC_PLAYER (PLAYERS_ID, CLASSES_ID) SELECT
                                                 id,
                                                 6
                                               FROM TOURNAMENT_PLAYER
                                               WHERE id > 13;


UPDATE USER
SET TOURNAMENT_ID = 3
WHERE ID = 1;

INSERT INTO `tournament_group` (`id`, `name`, `GP_ID`) VALUES (6, 'Runde 1', NULL);
INSERT INTO `tournament_group` (`id`, `name`, `GP_ID`) VALUES (7, 'Runde 2', NULL);
INSERT INTO `tournament_group` (`id`, `name`, `GP_ID`) VALUES (8, 'Runde 3', NULL);
INSERT INTO `tournament_group` (`id`, `name`, `GP_ID`) VALUES (9, 'Runde 4', NULL);
INSERT INTO `tournament_group` (`id`, `name`, `GP_ID`) VALUES (10, 'Runde 5', NULL);
INSERT INTO `tournament_group` (`id`, `name`, `GP_ID`) VALUES (11, 'Runde 6', NULL);

INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (20, 1, now(), NULL, 1, now(), 1, 'Schweizer System', 3, 0, NULL, 1, NULL, 6, NULL, 16, 18, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (21, 1, now(), NULL, 1, now(), 2, 'Schweizer System', 3, 0, NULL, 2, NULL, 6, NULL, 15, 17, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (22, 1, now(), NULL, 1, now(), 3, 'Schweizer System', 3, 0, NULL, 1, NULL, 6, NULL, 24, 19, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (23, 1, now(), NULL, 1, now(), 4, 'Schweizer System', 3, 0, NULL, 2, NULL, 6, NULL, 23, 14, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (24, 1, now(), NULL, 1, now(), 5, 'Schweizer System', 3, 0, NULL, 1, NULL, 6, NULL, 22, 20, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (25, 1, now(), NULL, 1, now(), 1, 'Schweizer System', 3, 0, NULL, 1, NULL, 7, NULL, 14, 22, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (26, 1, now(), NULL, 1, now(), 2, 'Schweizer System', 3, 0, NULL, 2, NULL, 7, NULL, 16, 17, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (27, 1, now(), NULL, 1, now(), 3, 'Schweizer System', 3, 0, NULL, 1, NULL, 7, NULL, 24, 18, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (28, 1, now(), NULL, 1, now(), 4, 'Schweizer System', 3, 0, NULL, 1, NULL, 7, NULL, 20, 19, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (29, 1, now(), NULL, 1, now(), 5, 'Schweizer System', 3, 0, NULL, 1, NULL, 7, NULL, 21, 15, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (30, 1, now(), NULL, 1, now(), 1, 'Schweizer System', 3, 0, NULL, 1, NULL, 8, NULL, 14, 24, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (31, 1, now(), NULL, 1, now(), 2, 'Schweizer System', 3, 0, NULL, 2, NULL, 8, NULL, 17, 22, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (32, 1, now(), NULL, 1, now(), 3, 'Schweizer System', 3, 0, NULL, 1, NULL, 8, NULL, 16, 21, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (33, 1, now(), NULL, 1, now(), 4, 'Schweizer System', 3, 0, NULL, 1, NULL, 8, NULL, 20, 18, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (34, 1, now(), NULL, 1, now(), 5, 'Schweizer System', 3, 0, NULL, 2, NULL, 8, NULL, 15, 23, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (35, 1, now(), NULL, 1, now(), 1, 'Schweizer System', 3, 0, NULL, 1, NULL, 9, NULL, 14, 17, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (36, 1, now(), NULL, 1, now(), 2, 'Schweizer System', 3, 0, NULL, 1, NULL, 9, NULL, 24, 22, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (37, 1, now(), NULL, 1, now(), 3, 'Schweizer System', 3, 0, NULL, 2, NULL, 9, NULL, 20, 16, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (38, 1, now(), NULL, 1, now(), 4, 'Schweizer System', 3, 0, NULL, 1, NULL, 9, NULL, 23, 21, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (39, 1, now(), NULL, 1, now(), 5, 'Schweizer System', 3, 0, NULL, 1, NULL, 9, NULL, 19, 15, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (40, 1, now(), NULL, 1, now(), 1, 'Schweizer System', 3, 0, NULL, 1, NULL, 10, NULL, 14, 16, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (41, 1, now(), NULL, 1, now(), 2, 'Schweizer System', 3, 0, NULL, 2, NULL, 10, NULL, 24, 17, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (42, 1, now(), NULL, 1, now(), 3, 'Schweizer System', 3, 0, NULL, 2, NULL, 10, NULL, 20, 23, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (43, 1, now(), NULL, 1, now(), 4, 'Schweizer System', 3, 0, NULL, 1, NULL, 10, NULL, 22, 19, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (44, 1, now(), NULL, 1, now(), 5, 'Schweizer System', 3, 0, NULL, 1, NULL, 10, NULL, 21, 18, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (45, 1, now(), NULL, 1, now(), 1, 'Schweizer System', 3, 0, NULL, 1, NULL, 11, NULL, 16, 24, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (46, 1, now(), NULL, 1, now(), 2, 'Schweizer System', 3, 0, NULL, 2, NULL, 11, NULL, 17, 23, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (47, 1, now(), NULL, 1, now(), 3, 'Schweizer System', 3, 0, NULL, 1, NULL, 11, NULL, 22, 21, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (48, 1, now(), NULL, 1, now(), 4, 'Schweizer System', 3, 0, NULL, 1, NULL, 11, NULL, 20, 15, NULL);
INSERT INTO `tournament_single_game` (`id`, `called`, `end_time`, `NAME`, `played`, `start_time`, `table_no`, `tournament_class_name`, `t_id`, `win_by_default`, `win_reason`, `winner`, `GAME_QUEUE_ID`, `group_id`, `NEXT_GAME_ID`, `player1_id`, `player2_id`, `ROUND_ID`)
VALUES (49, 1, now(), NULL, 1, now(), 5, 'Schweizer System', 3, 0, NULL, 1, NULL, 11, NULL, 19, 18, NULL);

INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (1, 11, 0);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (2, 11, 0);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (3, 11, 0);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (4, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (5, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (6, 11, 1);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (7, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (8, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (9, 11, 2);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (10, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (11, 11, 6);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (12, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (13, 7, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (14, 4, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (15, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (16, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (17, 11, 6);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (18, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (19, 11, 7);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (20, 11, 7);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (21, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (22, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (23, 11, 2);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (24, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (25, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (26, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (27, 11, 7);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (28, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (29, 11, 3);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (30, 11, 6);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (31, 11, 0);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (32, 11, 9);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (33, 11, 9);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (34, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (35, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (36, 14, 12);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (37, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (38, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (39, 11, 6);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (40, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (41, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (42, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (43, 11, 6);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (44, 11, 9);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (45, 11, 3);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (46, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (47, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (48, 11, 7);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (49, 5, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (50, 5, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (51, 6, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (52, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (53, 11, 7);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (54, 11, 9);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (55, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (56, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (57, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (58, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (59, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (60, 4, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (61, 11, 7);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (62, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (63, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (64, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (65, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (66, 13, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (67, 13, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (68, 14, 12);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (69, 11, 9);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (70, 11, 9);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (71, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (72, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (73, 7, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (74, 7, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (75, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (76, 7, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (77, 5, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (78, 13, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (79, 13, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (80, 13, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (81, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (82, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (83, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (84, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (85, 11, 4);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (86, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (87, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (88, 8, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (89, 9, 11);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (90, 11, 7);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (91, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (92, 11, 8);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (93, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (94, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (95, 11, 5);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (96, 11, 3);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (97, 11, 9);
INSERT INTO `game_set` (`id`, `points1`, `points2`) VALUES (98, 11, 9);


INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (20, 1);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (20, 2);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (20, 3);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (21, 4);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (21, 5);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (21, 6);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (21, 7);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (21, 8);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (22, 9);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (22, 10);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (22, 11);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (23, 12);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (23, 13);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (23, 14);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (24, 15);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (24, 16);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (24, 17);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (25, 18);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (25, 19);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (25, 20);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (26, 21);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (26, 22);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (26, 23);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (26, 24);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (26, 25);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (27, 26);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (27, 27);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (27, 28);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (28, 29);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (28, 30);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (28, 31);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (29, 32);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (29, 33);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (29, 34);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (29, 35);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (29, 36);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (30, 37);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (30, 38);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (30, 39);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (31, 40);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (31, 41);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (31, 42);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (32, 43);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (32, 44);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (32, 45);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (33, 46);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (33, 47);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (33, 48);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (34, 49);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (34, 50);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (34, 51);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (35, 52);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (35, 53);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (35, 54);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (36, 55);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (36, 56);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (36, 57);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (37, 58);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (37, 59);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (37, 60);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (38, 61);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (38, 62);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (38, 63);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (39, 64);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (39, 65);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (39, 66);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (39, 67);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (39, 68);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (40, 69);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (40, 70);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (40, 71);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (41, 72);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (41, 73);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (41, 74);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (42, 75);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (42, 76);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (42, 77);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (43, 78);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (43, 79);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (43, 80);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (44, 81);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (44, 82);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (44, 83);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (45, 84);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (45, 85);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (45, 86);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (46, 87);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (46, 88);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (46, 89);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (47, 90);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (47, 91);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (47, 92);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (48, 93);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (48, 94);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (48, 95);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (49, 96);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (49, 97);
INSERT INTO `tournament_single_game_set` (`TOURNAMENT_SINGLE_GAME_ID`, `SETS_ID`) VALUES (49, 98);

INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (14, 23);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (14, 25);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (14, 30);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (14, 35);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (14, 40);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (16, 20);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (16, 26);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (16, 32);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (16, 37);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (16, 40);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (16, 45);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (18, 20);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (18, 27);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (18, 33);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (18, 44);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (18, 49);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (17, 21);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (17, 26);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (17, 31);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (17, 35);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (17, 41);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (17, 46);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (21, 29);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (21, 32);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (21, 38);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (21, 44);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (21, 47);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (20, 24);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (20, 28);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (20, 33);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (20, 37);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (20, 42);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (20, 48);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (24, 22);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (24, 27);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (24, 30);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (24, 36);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (24, 41);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (24, 45);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (19, 22);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (19, 28);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (19, 39);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (19, 43);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (19, 49);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (22, 24);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (22, 25);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (22, 31);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (22, 36);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (22, 43);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (22, 47);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (15, 21);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (15, 29);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (15, 34);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (15, 39);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (15, 48);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (23, 23);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (23, 34);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (23, 38);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (23, 42);
INSERT INTO `game_to_player` (`GAME_ID`, `PLAYER_ID`) VALUES (23, 46);


INSERT INTO T_SYSTEM (ID, TC_ID) VALUES (2, 6);
INSERT INTO `phase` (`id`, `S_ID`, `INDEXP`) VALUES (3, 2, 0);
INSERT INTO `phase` (`id`, `S_ID`, `INDEXP`) VALUES (4, 2, 1);
INSERT INTO `phase` (`id`, `S_ID`, `INDEXP`) VALUES (5, 2, 2);
INSERT INTO `phase` (`id`, `S_ID`, `INDEXP`) VALUES (6, 2, 3);
INSERT INTO `phase` (`id`, `S_ID`, `INDEXP`) VALUES (7, 2, 4);
INSERT INTO `phase` (`id`, `S_ID`, `INDEXP`) VALUES (8, 2, 5);


INSERT INTO `swiss_system_phase` (`MAX_ROUNDS`, `ROUND`, `id`, `group_id`) VALUES (6, 1, 3, 6);
INSERT INTO `swiss_system_phase` (`MAX_ROUNDS`, `ROUND`, `id`, `group_id`) VALUES (6, 2, 4, 7);
INSERT INTO `swiss_system_phase` (`MAX_ROUNDS`, `ROUND`, `id`, `group_id`) VALUES (6, 3, 5, 8);
INSERT INTO `swiss_system_phase` (`MAX_ROUNDS`, `ROUND`, `id`, `group_id`) VALUES (6, 4, 6, 9);
INSERT INTO `swiss_system_phase` (`MAX_ROUNDS`, `ROUND`, `id`, `group_id`) VALUES (6, 5, 7, 10);
INSERT INTO `swiss_system_phase` (`MAX_ROUNDS`, `ROUND`, `id`, `group_id`) VALUES (6, 6, 8, 11);

UPDATE TOURNAMENT_CLASS
SET ACTIVE_PHASE = 5
WHERE id = 6;