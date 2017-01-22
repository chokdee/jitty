create table association (id bigint generated by default as identity (start with 1), longname varchar(255) not null, shortname varchar(255) not null, primary key (id));
create table club (id bigint generated by default as identity (start with 1), name varchar(255) not null, association_id bigint, primary key (id));
create table GAME_QUEUE (id bigint generated by default as identity (start with 1), primary key (id));
create table GAME_SET (id bigint generated by default as identity (start with 1), points1 integer, points2 integer, primary key (id));
create table GAME_TO_PLAYER (GAME_ID bigint not null, PLAYER_ID bigint not null);
create table group_phase (group_count integer, player_per_group integer, quali_group_count integer not null, id bigint not null, primary key (id));
create table ko_field (id bigint generated by default as identity (start with 1), NO_OF_ROUNDS integer, round_id bigint, primary key (id));
create table ko_phase (id bigint not null, KOFIELD_ID bigint, primary key (id));
create table Phase (id bigint generated by default as identity (start with 1), system_id bigint, INDEX integer, primary key (id));
create table round (id bigint generated by default as identity (start with 1), GAME_SIZE integer, ROUND_TYPE integer, PREV_ROUND_ID bigint, primary key (id));
create table t_system (id bigint generated by default as identity (start with 1), TC_ID bigint, primary key (id));
create table TC_PLAYER (classes_id bigint not null, players_id bigint not null);
create table TG_PLAYER (TOURNAMENT_GROUP_ID bigint not null, PLAYER_ID bigint not null);
create table tournament (id bigint generated by default as identity (start with 1), end_date date not null, name varchar(255) not null, running boolean not null, start_date date not null, table_count integer not null, primary key (id));
create table tournament_class (id bigint generated by default as identity (start with 1), ACTIVE_PHASE integer not null, end_ttr integer, max_age date, min_age date, name varchar(255) not null, open_for_men boolean not null, open_for_women boolean not null, running boolean not null, start_ttr integer, start_time timestamp, type varchar(255), T_ID bigint, primary key (id));
create table tournament_group (id bigint generated by default as identity (start with 1), name varchar(10) not null, GP_ID bigint, primary key (id));
create table tournament_player (id bigint generated by default as identity (start with 1), birthday date, email varchar(255), firstname varchar(255) not null, gender varchar(1), last_game_at timestamp, lastname varchar(255) not null, mobilenumber varchar(255), qttr integer, ttr integer, association_id bigint, club_id bigint, primary key (id));
create table TOURNAMENT_SINGLE_GAME (id bigint generated by default as identity (start with 1), called boolean not null, end_time timestamp, NAME varchar(255), played boolean not null, start_time timestamp, table_no integer, tournament_class_name varchar(255) not null, t_id bigint not null, winner integer not null, GAME_QUEUE_ID bigint, group_id bigint, NEXT_GAME_ID bigint, player1_id bigint, player2_id bigint, ROUND_ID bigint, primary key (id));
create table TOURNAMENT_SINGLE_GAME_SET (TOURNAMENT_SINGLE_GAME_ID bigint not null, SETS_ID bigint not null);
create table user (id bigint generated by default as identity (start with 1), avatar varbinary(255), email varchar(255), locale varchar(255), locked boolean not null, loginname varchar(255) not null, name varchar(255) not null, password varchar(255) not null, type integer, TOURNAMENT_ID bigint, primary key (id));
create table user_role (id bigint generated by default as identity (start with 1), name varchar(255), primary key (id));
alter table association add constraint UK_ogdrfbju5finu018l8mvhdvug unique (longname);
alter table association add constraint UK_oqsncgu633nvys2yo9ehj7j14 unique (shortname);
alter table club add constraint FKrft7ntsm4eurtpm0h9ng648vp foreign key (association_id) references association;
alter table GAME_TO_PLAYER add constraint FKk5bx3r6y9tw6719cky1xvpg80 foreign key (PLAYER_ID) references TOURNAMENT_SINGLE_GAME;
alter table GAME_TO_PLAYER add constraint FKca0r1lrsyhm231ja4fihlj789 foreign key (GAME_ID) references tournament_player;
alter table group_phase add constraint FKjofpwlrorhysm3ss9d18fya56 foreign key (id) references Phase;
alter table ko_field add constraint FK6i30o3c25tnnbcn18k38fan4c foreign key (round_id) references round;
alter table ko_phase add constraint FK8kotu3nslnrnh5yq6fj65x4jr foreign key (KOFIELD_ID) references ko_field;
alter table ko_phase add constraint FKfa55lj6k09jwffa7hghfihkof foreign key (id) references Phase;
alter table Phase add constraint FKmimrf7k26lywbgygsp7g05ljr foreign key (system_id) references t_system;
alter table round add constraint FKhkndx0l8sli5r6gf7m0eveqn7 foreign key (PREV_ROUND_ID) references round;
alter table t_system add constraint FKer0y033wsm6ysyosohkxlykss foreign key (TC_ID) references tournament_class;
alter table TC_PLAYER add constraint FK996yua0ff90b7wx8m9mm0bqc7 foreign key (players_id) references tournament_player;
alter table TC_PLAYER add constraint FK9q89qmejhqft0x204goa1eg0n foreign key (classes_id) references tournament_class;
alter table TG_PLAYER add constraint FKlvtcmf9uuan17vu73lysr3afk foreign key (PLAYER_ID) references tournament_player;
alter table TG_PLAYER add constraint FK9euge99x86qrjlgl1cfn976ga foreign key (TOURNAMENT_GROUP_ID) references tournament_group;
alter table tournament_class add constraint FK78e4gyb2n3nahl2de4ex9c9qp foreign key (T_ID) references tournament;
alter table tournament_group add constraint FKte22s1nwj63gybkq0lfvn0vyx foreign key (GP_ID) references group_phase;
alter table tournament_player add constraint FKckudad7hjiry453vbbtsj3ql9 foreign key (association_id) references association;
alter table tournament_player add constraint FKiogds7xpq7tbscr9rlcgto05j foreign key (club_id) references club;
alter table TOURNAMENT_SINGLE_GAME add constraint FKnyl2ic1enqd5gq03biksl16pq foreign key (GAME_QUEUE_ID) references GAME_QUEUE;
alter table TOURNAMENT_SINGLE_GAME add constraint FKpsrfvitcw0ku88tvltj9hfqko foreign key (group_id) references tournament_group;
alter table TOURNAMENT_SINGLE_GAME add constraint FKox6oe5yfn631dkf7sj8uis938 foreign key (NEXT_GAME_ID) references TOURNAMENT_SINGLE_GAME;
alter table TOURNAMENT_SINGLE_GAME add constraint FK_P1 foreign key (player1_id) references tournament_player;
alter table TOURNAMENT_SINGLE_GAME add constraint FK_P2 foreign key (player2_id) references tournament_player;
alter table TOURNAMENT_SINGLE_GAME add constraint FKlgis5bk304f6ka7v33y3k1o24 foreign key (ROUND_ID) references round;
alter table TOURNAMENT_SINGLE_GAME_SET add constraint UK_et5i78b8ngopb9nvxxw7dsurr unique (SETS_ID);
alter table TOURNAMENT_SINGLE_GAME_SET add constraint FKcp88fdy2kinrta57sji3byaye foreign key (SETS_ID) references GAME_SET;
alter table TOURNAMENT_SINGLE_GAME_SET add constraint FKqv165vg5rkcjouv8lx0p4gon0 foreign key (TOURNAMENT_SINGLE_GAME_ID) references TOURNAMENT_SINGLE_GAME;
alter table user add constraint FKmhhj09rh4m7qyavphtlx12j49 foreign key (TOURNAMENT_ID) references tournament;
