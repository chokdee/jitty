CREATE TABLE association (
  id        BIGINT       NOT NULL AUTO_INCREMENT,
  longname  VARCHAR(255) NOT NULL,
  shortname VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE club (
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  click_tt_nr    VARCHAR(255),
  name           VARCHAR(255) NOT NULL,
  association_id BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE GAME_QUEUE (
  id BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);
CREATE TABLE GAME_SET (
  id      BIGINT NOT NULL AUTO_INCREMENT,
  points1 INTEGER,
  points2 INTEGER,
  PRIMARY KEY (id)
);
CREATE TABLE GAME_TO_PLAYER (
  GAME_ID   BIGINT NOT NULL,
  PLAYER_ID BIGINT NOT NULL
);
CREATE TABLE group_phase (
  group_count       INTEGER,
  player_per_group  INTEGER,
  quali_group_count INTEGER NOT NULL,
  id                BIGINT  NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE ko_field (
  id           BIGINT NOT NULL AUTO_INCREMENT,
  NO_OF_ROUNDS INTEGER,
  round_id     BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE ko_phase (
  id         BIGINT NOT NULL,
  KOFIELD_ID BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE Phase (
  id     BIGINT NOT NULL AUTO_INCREMENT,
  S_ID   BIGINT,
  INDEXP INTEGER,
  PRIMARY KEY (id)
);
CREATE TABLE round (
  id            BIGINT NOT NULL AUTO_INCREMENT,
  GAME_SIZE     INTEGER,
  ROUND_TYPE    INTEGER,
  PREV_ROUND_ID BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE swiss_system_phase (
  MAX_ROUNDS INTEGER NOT NULL,
  ROUND      INTEGER NOT NULL,
  id         BIGINT  NOT NULL,
  group_id   BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE T_PLAYER (
  Tournament_id BIGINT NOT NULL,
  players_id    BIGINT NOT NULL,
  PRIMARY KEY (Tournament_id, players_id)
);
CREATE TABLE t_system (
  id    BIGINT NOT NULL AUTO_INCREMENT,
  TC_ID BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE TC_PLAYER (
  classes_id BIGINT NOT NULL,
  players_id BIGINT NOT NULL
);
CREATE TABLE TG_PLAYER (
  TOURNAMENT_GROUP_ID BIGINT NOT NULL,
  PLAYER_ID           BIGINT NOT NULL
);
CREATE TABLE tournament (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  click_tt_id VARCHAR(255),
  end_date    DATE         NOT NULL,
  name        VARCHAR(255) NOT NULL,
  running     BIT          NOT NULL,
  start_date  DATE         NOT NULL,
  table_count INTEGER      NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE tournament_class (
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  ACTIVE_PHASE   INTEGER      NOT NULL,
  age_group      VARCHAR(255) NOT NULL,
  end_ttr        INTEGER,
  max_age        DATE,
  min_age        DATE,
  name           VARCHAR(255) NOT NULL,
  open_for_men   BIT          NOT NULL,
  open_for_women BIT          NOT NULL,
  RUNNING        BIT          NOT NULL,
  start_ttr      INTEGER,
  START_TIME     DATETIME,
  status         VARCHAR(255) NOT NULL,
  SYSTEM_TYPE    INTEGER      NOT NULL,
  T_ID           BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE tournament_group (
  id    BIGINT      NOT NULL AUTO_INCREMENT,
  name  VARCHAR(10) NOT NULL,
  GP_ID BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE tournament_player (
  id                   BIGINT       NOT NULL AUTO_INCREMENT,
  birthday             DATE,
  click_tt_internal_nr VARCHAR(255),
  click_tt_licence_nr  VARCHAR(255),
  email                VARCHAR(255),
  firstname            VARCHAR(255) NOT NULL,
  gender               VARCHAR(1),
  last_game_at         DATETIME,
  lastname             VARCHAR(255) NOT NULL,
  mobilenumber         VARCHAR(255),
  qttr                 INTEGER,
  suspended            BIT,
  ttr                  INTEGER,
  association_id       BIGINT,
  club_id              BIGINT,
  tournament_id        BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE TOURNAMENT_SINGLE_GAME (
  id                    BIGINT       NOT NULL AUTO_INCREMENT,
  called                BIT          NOT NULL,
  end_time              DATETIME,
  NAME                  VARCHAR(255),
  played                BIT          NOT NULL,
  start_time            DATETIME,
  table_no              INTEGER,
  tournament_class_name VARCHAR(255) NOT NULL,
  t_id                  BIGINT       NOT NULL,
  win_by_default        BIT,
  win_reason            INTEGER,
  winner                INTEGER      NOT NULL,
  GAME_QUEUE_ID         BIGINT,
  group_id              BIGINT,
  NEXT_GAME_ID          BIGINT,
  player1_id            BIGINT,
  player2_id            BIGINT,
  ROUND_ID              BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE TOURNAMENT_SINGLE_GAME_SET (
  TOURNAMENT_SINGLE_GAME_ID BIGINT NOT NULL,
  SETS_ID                   BIGINT NOT NULL
);
CREATE TABLE user (
  id            BIGINT       NOT NULL AUTO_INCREMENT,
  avatar        TINYBLOB,
  email         VARCHAR(255),
  locale        VARCHAR(255),
  locked        BIT          NOT NULL,
  loginname     VARCHAR(255) NOT NULL,
  name          VARCHAR(255) NOT NULL,
  password      VARCHAR(255) NOT NULL,
  type          INTEGER,
  TOURNAMENT_ID BIGINT,
  PRIMARY KEY (id)
);
CREATE TABLE user_role (
  id   BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255),
  PRIMARY KEY (id)
);
ALTER TABLE association
  ADD CONSTRAINT UK_ogdrfbju5finu018l8mvhdvug UNIQUE (longname);
ALTER TABLE association
  ADD CONSTRAINT UK_oqsncgu633nvys2yo9ehj7j14 UNIQUE (shortname);
ALTER TABLE T_PLAYER
  ADD CONSTRAINT UK_a8o22qx76ytwm779shvhkhd9r UNIQUE (players_id);
ALTER TABLE TOURNAMENT_SINGLE_GAME_SET
  ADD CONSTRAINT UK_et5i78b8ngopb9nvxxw7dsurr UNIQUE (SETS_ID);
ALTER TABLE club
  ADD CONSTRAINT FKrft7ntsm4eurtpm0h9ng648vp FOREIGN KEY (association_id) REFERENCES association (id);
ALTER TABLE GAME_TO_PLAYER
  ADD CONSTRAINT FKk5bx3r6y9tw6719cky1xvpg80 FOREIGN KEY (PLAYER_ID) REFERENCES TOURNAMENT_SINGLE_GAME (id);
ALTER TABLE GAME_TO_PLAYER
  ADD CONSTRAINT FKca0r1lrsyhm231ja4fihlj789 FOREIGN KEY (GAME_ID) REFERENCES tournament_player (id);
ALTER TABLE group_phase
  ADD CONSTRAINT FKjofpwlrorhysm3ss9d18fya56 FOREIGN KEY (id) REFERENCES Phase (id);
ALTER TABLE ko_field
  ADD CONSTRAINT FK6i30o3c25tnnbcn18k38fan4c FOREIGN KEY (round_id) REFERENCES round (id);
ALTER TABLE ko_phase
  ADD CONSTRAINT FK8kotu3nslnrnh5yq6fj65x4jr FOREIGN KEY (KOFIELD_ID) REFERENCES ko_field (id);
ALTER TABLE ko_phase
  ADD CONSTRAINT FKfa55lj6k09jwffa7hghfihkof FOREIGN KEY (id) REFERENCES Phase (id);
ALTER TABLE Phase
  ADD CONSTRAINT FKb8sbbbk5ai0d5o0w1oiu2fcd0 FOREIGN KEY (S_ID) REFERENCES t_system (id);
ALTER TABLE round
  ADD CONSTRAINT FKhkndx0l8sli5r6gf7m0eveqn7 FOREIGN KEY (PREV_ROUND_ID) REFERENCES round (id);
ALTER TABLE swiss_system_phase
  ADD CONSTRAINT FK4p84g4b67qrf076pmupyq3psk FOREIGN KEY (group_id) REFERENCES tournament_group (id);
ALTER TABLE swiss_system_phase
  ADD CONSTRAINT FKghusqw84hup11ghej8i5ad9go FOREIGN KEY (id) REFERENCES Phase (id);
ALTER TABLE T_PLAYER
  ADD CONSTRAINT FKox4dt24cvyx5jdn4rs9lyeyk5 FOREIGN KEY (players_id) REFERENCES tournament_player (id);
ALTER TABLE T_PLAYER
  ADD CONSTRAINT FK9sdl5mre08eax08oau8wfudro FOREIGN KEY (Tournament_id) REFERENCES tournament (id);
ALTER TABLE t_system
  ADD CONSTRAINT FKer0y033wsm6ysyosohkxlykss FOREIGN KEY (TC_ID) REFERENCES tournament_class (id);
ALTER TABLE TC_PLAYER
  ADD CONSTRAINT FK996yua0ff90b7wx8m9mm0bqc7 FOREIGN KEY (players_id) REFERENCES tournament_player (id);
ALTER TABLE TC_PLAYER
  ADD CONSTRAINT FK9q89qmejhqft0x204goa1eg0n FOREIGN KEY (classes_id) REFERENCES tournament_class (id);
ALTER TABLE TG_PLAYER
  ADD CONSTRAINT FKlvtcmf9uuan17vu73lysr3afk FOREIGN KEY (PLAYER_ID) REFERENCES tournament_player (id);
ALTER TABLE TG_PLAYER
  ADD CONSTRAINT FK9euge99x86qrjlgl1cfn976ga FOREIGN KEY (TOURNAMENT_GROUP_ID) REFERENCES tournament_group (id);
ALTER TABLE tournament_class
  ADD CONSTRAINT FK78e4gyb2n3nahl2de4ex9c9qp FOREIGN KEY (T_ID) REFERENCES tournament (id);
ALTER TABLE tournament_group
  ADD CONSTRAINT FKte22s1nwj63gybkq0lfvn0vyx FOREIGN KEY (GP_ID) REFERENCES group_phase (id);
ALTER TABLE tournament_player
  ADD CONSTRAINT FKckudad7hjiry453vbbtsj3ql9 FOREIGN KEY (association_id) REFERENCES association (id);
ALTER TABLE tournament_player
  ADD CONSTRAINT FKiogds7xpq7tbscr9rlcgto05j FOREIGN KEY (club_id) REFERENCES club (id);
ALTER TABLE tournament_player
  ADD CONSTRAINT FKrrm3jbmm1fxx5t9t5f8t46ebc FOREIGN KEY (tournament_id) REFERENCES tournament (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME
  ADD CONSTRAINT FKnyl2ic1enqd5gq03biksl16pq FOREIGN KEY (GAME_QUEUE_ID) REFERENCES GAME_QUEUE (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME
  ADD CONSTRAINT FKpsrfvitcw0ku88tvltj9hfqko FOREIGN KEY (group_id) REFERENCES tournament_group (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME
  ADD CONSTRAINT FKox6oe5yfn631dkf7sj8uis938 FOREIGN KEY (NEXT_GAME_ID) REFERENCES TOURNAMENT_SINGLE_GAME (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME
  ADD CONSTRAINT FK_P1 FOREIGN KEY (player1_id) REFERENCES tournament_player (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME
  ADD CONSTRAINT FK_P2 FOREIGN KEY (player2_id) REFERENCES tournament_player (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME
  ADD CONSTRAINT FKlgis5bk304f6ka7v33y3k1o24 FOREIGN KEY (ROUND_ID) REFERENCES round (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME_SET
  ADD CONSTRAINT FKcp88fdy2kinrta57sji3byaye FOREIGN KEY (SETS_ID) REFERENCES GAME_SET (id);
ALTER TABLE TOURNAMENT_SINGLE_GAME_SET
  ADD CONSTRAINT FKqv165vg5rkcjouv8lx0p4gon0 FOREIGN KEY (TOURNAMENT_SINGLE_GAME_ID) REFERENCES TOURNAMENT_SINGLE_GAME (id);
ALTER TABLE user
  ADD CONSTRAINT FKmhhj09rh4m7qyavphtlx12j49 FOREIGN KEY (TOURNAMENT_ID) REFERENCES tournament (id);
