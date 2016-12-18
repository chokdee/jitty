
    create table GAME_SET (
        id bigint generated by default as identity (start with 1),
        points1 integer,
        points2 integer,
        primary key (id)
    );

    create table GAME_TO_PLAYER (
        GAME_ID bigint not null,
        PLAYER_ID bigint not null
    );

    create table TC_PLAYER (
        classes_id bigint not null,
        players_id bigint not null
    );

    create table TG_PLAYER (
        tournament_group_id bigint not null,
        players_id bigint not null
    );

    create table TOURNAMENT_SINGLE_GAME (
        id bigint generated by default as identity (start with 1),
        called boolean not null,
        end_time timestamp,
        NAME varchar(255),
        played boolean not null,
        start_time timestamp,
        table_no integer,
        tournament_class_name varchar(255) not null,
        winner integer not null,
        group_id bigint,
        NEXT_GAME_ID bigint,
        player1_id bigint,
        player2_id bigint,
        ROUND_ID bigint,
        primary key (id)
    );

    create table TOURNAMENT_SINGLE_GAME_SET (
        TOURNAMENT_SINGLE_GAME_id bigint not null,
        sets_id bigint not null
    );

    create table association (
        id bigint generated by default as identity (start with 1),
        longname varchar(255) not null,
        shortname varchar(255) not null,
        primary key (id)
    );

    create table club (
        id bigint generated by default as identity (start with 1),
        name varchar(255) not null,
        association_id bigint,
        primary key (id)
    );

    create table ko_field (
        id bigint generated by default as identity (start with 1),
        NO_OF_ROUNDS integer,
        round_id bigint,
        primary key (id)
    );

    create table round (
        id bigint generated by default as identity (start with 1),
        GAME_SIZE integer,
        ROUND_TYPE integer,
        PREV_ROUND_ID bigint,
        primary key (id)
    );

    create table tournament (
        id bigint generated by default as identity (start with 1),
        end_date date not null,
        name varchar(255) not null,
        running boolean not null,
        start_date date not null,
        table_count integer not null,
        primary key (id)
    );

    create table tournament_class (
        id bigint generated by default as identity (start with 1),
        end_ttr integer,
        game_mode_phase_1 varchar(1),
        game_mode_phase_2 varchar(1),
        group_count integer,
        max_age date,
        min_age date,
        name varchar(255) not null,
        open_for_men boolean not null,
        open_for_women boolean not null,
        phase integer,
        player_per_group integer,
        running boolean not null,
        start_ttr integer,
        start_time timestamp,
        type varchar(255),
        KOFIELD_ID bigint,
        T_ID bigint,
        primary key (id)
    );

    create table tournament_group (
        id bigint generated by default as identity (start with 1),
        name varchar(10) not null,
        TC_ID bigint,
        primary key (id)
    );

    create table tournament_player (
        id bigint generated by default as identity (start with 1),
        birthday date,
        email varchar(255),
        firstname varchar(255) not null,
        gender varchar(1),
        last_game_at timestamp,
        lastname varchar(255) not null,
        mobilenumber varchar(255),
        qttr integer,
        ttr integer,
        association_id bigint,
        club_id bigint,
        primary key (id)
    );

    create table user (
        id bigint generated by default as identity (start with 1),
        avatar varbinary(255),
        email varchar(255),
        locale varchar(255),
        locked boolean not null,
        loginname varchar(255) not null,
        name varchar(255) not null,
        password varchar(255) not null,
        type integer,
        TOURNAMENT_ID bigint,
        primary key (id)
    );

    create table user_role (
        id bigint generated by default as identity (start with 1),
        name varchar(255),
        primary key (id)
    );

    alter table TOURNAMENT_SINGLE_GAME_SET 
        add constraint UK_qsllp7dkc2v1b8k3vdmi6tb7i unique (sets_id);

    alter table association 
        add constraint UK_ogdrfbju5finu018l8mvhdvug unique (longname);

    alter table association 
        add constraint UK_oqsncgu633nvys2yo9ehj7j14 unique (shortname);

    alter table GAME_TO_PLAYER 
        add constraint FK_7ib8ce8wd79s2px5w8j9kwymo 
        foreign key (PLAYER_ID) 
        references TOURNAMENT_SINGLE_GAME;

    alter table GAME_TO_PLAYER 
        add constraint FK_1beglyxll8orfa0arhrpg8ref 
        foreign key (GAME_ID) 
        references tournament_player;

    alter table TC_PLAYER 
        add constraint FK_j1gkvwc7o55bxst5rs8ymsbgk 
        foreign key (players_id) 
        references tournament_player;

    alter table TC_PLAYER 
        add constraint FK_qxf2qn0ihgjq7p82fo462o82c 
        foreign key (classes_id) 
        references tournament_class;

    alter table TG_PLAYER 
        add constraint FK_qf0eotnnolfs141a49sdse92l 
        foreign key (players_id) 
        references tournament_player;

    alter table TG_PLAYER 
        add constraint FK_ipidk7vloonp421slacf3fuil 
        foreign key (tournament_group_id) 
        references tournament_group;

    alter table TOURNAMENT_SINGLE_GAME 
        add constraint FK_ea0mkvq89t9u7fktbfhrol0e 
        foreign key (group_id) 
        references tournament_group;

    alter table TOURNAMENT_SINGLE_GAME 
        add constraint FK_k3le5pcjb03nnotlq3cc1sy5a 
        foreign key (NEXT_GAME_ID) 
        references TOURNAMENT_SINGLE_GAME;

    alter table TOURNAMENT_SINGLE_GAME 
        add constraint FK_P1 
        foreign key (player1_id) 
        references tournament_player;

    alter table TOURNAMENT_SINGLE_GAME 
        add constraint FK_P2 
        foreign key (player2_id) 
        references tournament_player;

    alter table TOURNAMENT_SINGLE_GAME 
        add constraint FK_qva0r23wy13oo9xcqfxt5edqy 
        foreign key (ROUND_ID) 
        references round;

    alter table TOURNAMENT_SINGLE_GAME_SET 
        add constraint FK_qsllp7dkc2v1b8k3vdmi6tb7i 
        foreign key (sets_id) 
        references GAME_SET;

    alter table TOURNAMENT_SINGLE_GAME_SET 
        add constraint FK_i34ho4dv460fg16yj12yybeuh 
        foreign key (TOURNAMENT_SINGLE_GAME_id) 
        references TOURNAMENT_SINGLE_GAME;

    alter table club 
        add constraint FK_lksuditej44rp3dc99p39397k 
        foreign key (association_id) 
        references association;

    alter table ko_field 
        add constraint FK_ml5ekxtc0vjbduphe0lv0lbqs 
        foreign key (round_id) 
        references round;

    alter table round 
        add constraint FK_gl0u9qmkh4bg96kommsxv22uh 
        foreign key (PREV_ROUND_ID) 
        references round;

    alter table tournament_class 
        add constraint FK_36pr8q59wyph7eroro6wm1xm2 
        foreign key (KOFIELD_ID) 
        references ko_field;

    alter table tournament_class 
        add constraint FK_61kldru8t8vxyffj5ign0v0ka 
        foreign key (T_ID) 
        references tournament;

    alter table tournament_group 
        add constraint FK_dv15p88njdcf4vr8ot8wiajs0 
        foreign key (TC_ID) 
        references tournament_class;

    alter table tournament_player 
        add constraint FK_j5mtotffj08ony0ypv9nwoi3p 
        foreign key (association_id) 
        references association;

    alter table tournament_player 
        add constraint FK_h4pwv9pqf0fhmajr8rx8ueq6k 
        foreign key (club_id) 
        references club;

    alter table user 
        add constraint FK_jyl9a30maocxl1jmocgin4mct 
        foreign key (TOURNAMENT_ID) 
        references tournament;
