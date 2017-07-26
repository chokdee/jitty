/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by J. Melzer on 24.07.2017.
 */
public class ClickTTExporterTest {
    ClickTTExporter exporter = new ClickTTExporter();

    @Test
    public void export() throws Exception {
        Date date = new GregorianCalendar(2017, 2, 12).getTime();
        Date startClz = new GregorianCalendar(2017, 2, 12, 12, 0).getTime();

        Tournament tournament = new Tournament();
        tournament.setName("andro WTTV-Cup");
        tournament.setStartDate(date);
        tournament.setEndDate(date);
        tournament.setClickTTId("lxnfy8egjpKwukE9myqZarSPxduPRXbs");

        TournamentClass clz = new TournamentClass("Bla");
        clz.setAgeGroup(AgeGroup.DH.getValue());
        clz.setStartTime(startClz);
        tournament.addClass(clz);

        clz.addPlayer(createPlayer(1L, "Anette", "Beitel", "156012528", "NU1891464", 1979, Gender.W, 848, "TTG St. Augustin"));
        clz.addPlayer(createPlayer(2L, "Gerd", "Bosse", "148026208", "NU210632", 1954, Gender.M, 1219, "TTV BW Neudorf"));
        clz.addPlayer(createPlayer(3L, "Georg", "Sauer", "156012499", "NU1480185", 1955, Gender.M, 1117, "TTG St. Augustin"));
        clz.addPlayer(createPlayer(4L, "Manfred", "Siry", "154017167", "NU1480185", 1959, Gender.M, 1534, "TTV Viktoria Bonn"));

        List<TournamentPlayer> players = clz.getPlayers();
        clz.createPhaseCombination(PhaseCombination.SWS);
        clz.setActivePhaseNo(0);
        ((SwissSystemPhase) clz.getActivePhase()).getGroup().addGame(createGame(100L, players.get(0), players.get(1)));
        ((SwissSystemPhase) clz.getActivePhase()).getGroup().addGame(createGame(101L, players.get(2), players.get(3)));

        File xmlFile = new File("target/clicktt-export.xml");
        exporter.export(tournament, xmlFile);
        assertThat(xmlFile.exists(), is(true));

        com.jmelzer.jitty.model.xml.clicktt.Tournament jaxb = new XMLImporter().parseClickTTPlayerExport(new FileInputStream(xmlFile));

        assertEquals("2017-03-12", jaxb.getEndDate());
        assertEquals("2017-03-12", jaxb.getStartDate());
        assertEquals(tournament.getClickTTId(), jaxb.getTournamentId());
        assertThat(jaxb.getCompetition().get(0).getAgeGroup(), is("Damen/Herren"));
        assertThat(jaxb.getCompetition().get(0).getType(), is("Einzel"));
        assertThat(jaxb.getCompetition().get(0).getStartDate(), is("2017-03-12 12:00"));

        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getFirstname(), is("Anette"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getLastname(), is("Beitel"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getLicenceNr(), is("156012528"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getInternalNr(), is("NU1891464"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getBirthyear(), is("1979"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getSex(), is("0"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getId(), is("PLAYER1"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getTtr(), is("848"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(0).getPerson().get(0).getClubName(), is("TTG St. Augustin"));

        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(1).getPerson().get(0).getBirthyear(), is("1954"));
        assertThat(jaxb.getCompetition().get(0).getPlayers().getPlayer().get(1).getPerson().get(0).getSex(), is("1"));

    }

    private TournamentPlayer createPlayer(Long id, String firstname, String lastname, String licNr, String internalNr, int birthdayyear, Gender gender, int ttr, String clubName) {
        TournamentPlayer player = new TournamentPlayer();
        player.setId(id);
        player.setFirstName(firstname);
        player.setLastName(lastname);
        player.setClickTTLicenceNr(licNr);
        player.setClickTTInternalNr(internalNr);
        player.setBirthday(getDateFromYear(birthdayyear));
        player.setGender(gender.getValue());
        player.setQttr(ttr);
        player.setClub(new Club(clubName));
        return player;
    }

    private TournamentSingleGame createGame(Long id, TournamentPlayer p1, TournamentPlayer p2) {
        TournamentSingleGame game = new TournamentSingleGame();
        game.setId(id);
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setWinner(2);
        game.addSet(new GameSet(2, 11));
        game.addSet(new GameSet(5, 11));
        game.addSet(new GameSet(9, 11));
        return game;
    }

    Date getDateFromYear(int year) {
        return new GregorianCalendar(year, 0, 1).getTime();
    }


}