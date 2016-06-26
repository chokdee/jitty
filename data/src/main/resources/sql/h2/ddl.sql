
    create table GameSet (
        id bigint generated by default as identity,
        points1 integer,
        points2 integer,
        primary key (id)
    );

    create table TC_PLAYER (
        tournament_class_id bigint not null,
        players_id bigint not null
    );

    create table TG_PLAYER (
        tournament_group_id bigint not null,
        players_id bigint not null
    );

    create table ko_field (
        id bigint generated by default as identity,
        round_id bigint,
        primary key (id)
    );

    create table round (
        id bigint generated by default as identity,
        roundType integer,
        size integer,
        nextRound_id bigint,
        primary key (id)
    );

    create table round_tournament_single_game (
        round_id bigint not null,
        games_id bigint not null
    );

    create table tournament (
        id bigint generated by default as identity,
        end_date date not null,
        name varchar(255) not null,
        start_date date not null,
        primary key (id)
    );

    create table tournament_class (
        id bigint generated by default as identity,
        end_ttr varchar(255),
        name varchar(255) not null,
        start_ttr integer,
        T_ID bigint,
        primary key (id)
    );

    create table tournament_group (
        id bigint generated by default as identity,
        name varchar(10) not null,
        TC_ID bigint,
        primary key (id)
    );

    create table tournament_player (
        id bigint generated by default as identity,
        birthday date,
        email varchar(255),
        firstname varchar(255) not null,
        gender varchar(1),
        lastname varchar(255) not null,
        mobilenumber varchar(255),
        qttr integer,
        ttr integer,
        primary key (id)
    );

    create table tournament_player_tournament_single_game (
        tournament_player_id bigint not null,
        games_id bigint not null
    );

    create table tournament_single_game (
        id bigint generated by default as identity,
        called boolean not null,
        played boolean not null,
        start_time timestamp,
        table_no integer,
        winner integer not null,
        player1_id bigint,
        player2_id bigint,
        TG_ID bigint,
        primary key (id)
    );

    create table tournament_single_game_GameSet (
        tournament_single_game_id bigint not null,
        sets_id bigint not null
    );

    create table user (
        id bigint generated by default as identity,
        avatar binary(255),
        email varchar(255),
        locale varchar(255),
        locked boolean not null,
        loginname varchar(255) not null,
        name varchar(255) not null,
        password varchar(255) not null,
        type integer,
        primary key (id)
    );

    create table user_role (
        id bigint generated by default as identity,
        name varchar(255),
        primary key (id)
    );

    alter table round_tournament_single_game 
        add constraint UK_3w2ugb7y1da0asgegqgjaxfp9 unique (games_id);

    alter table tournament_player_tournament_single_game 
        add constraint UK_84p5ugtr6tbk09d22i4qbchdy unique (games_id);

    alter table tournament_single_game_GameSet 
        add constraint UK_1ntuaj7ufxxifejbjvv21iip6 unique (sets_id);

    alter table TC_PLAYER 
        add constraint FK_j1gkvwc7o55bxst5rs8ymsbgk 
        foreign key (players_id) 
        references tournament_player;

    alter table TC_PLAYER 
        add constraint FK_jlrwx0t0fqa3k8rtf5lkfl3ks 
        foreign key (tournament_class_id) 
        references tournament_class;

    alter table TG_PLAYER 
        add constraint FK_qf0eotnnolfs141a49sdse92l 
        foreign key (players_id) 
        references tournament_player;

    alter table TG_PLAYER 
        add constraint FK_ipidk7vloonp421slacf3fuil 
        foreign key (tournament_group_id) 
        references tournament_group;

    alter table ko_field 
        add constraint FK_ml5ekxtc0vjbduphe0lv0lbqs 
        foreign key (round_id) 
        references round;

    alter table round 
        add constraint FK_76fq8yudkgpwt13pnogspy8l6 
        foreign key (nextRound_id) 
        references round;

    alter table round_tournament_single_game 
        add constraint FK_3w2ugb7y1da0asgegqgjaxfp9 
        foreign key (games_id) 
        references tournament_single_game;

    alter table round_tournament_single_game 
        add constraint FK_pj3ov67ab7xa8uj3bt3stp71w 
        foreign key (round_id) 
        references round;

    alter table tournament_class 
        add constraint FK_61kldru8t8vxyffj5ign0v0ka 
        foreign key (T_ID) 
        references tournament;

    alter table tournament_group 
        add constraint FK_dv15p88njdcf4vr8ot8wiajs0 
        foreign key (TC_ID) 
        references tournament_class;

    alter table tournament_player_tournament_single_game 
        add constraint FK_84p5ugtr6tbk09d22i4qbchdy 
        foreign key (games_id) 
        references tournament_single_game;

    alter table tournament_player_tournament_single_game 
        add constraint FK_cn260hkwtf65s5cb68xvoktw9 
        foreign key (tournament_player_id) 
        references tournament_player;

    alter table tournament_single_game 
        add constraint FK_dk9usfge9pakj2saha0pj2i7e 
        foreign key (player1_id) 
        references tournament_player;

    alter table tournament_single_game 
        add constraint FK_iag0aw9oxg5qh0w5s28ju7tab 
        foreign key (player2_id) 
        references tournament_player;

    alter table tournament_single_game 
        add constraint FK_969al3ers5yuli4m5q7thml7p 
        foreign key (TG_ID) 
        references tournament_group;

    alter table tournament_single_game_GameSet 
        add constraint FK_1ntuaj7ufxxifejbjvv21iip6 
        foreign key (sets_id) 
        references GameSet;

    alter table tournament_single_game_GameSet 
        add constraint FK_j48gxx1sy679tjpw2kd1fijjo 
        foreign key (tournament_single_game_id) 
        references tournament_single_game;
