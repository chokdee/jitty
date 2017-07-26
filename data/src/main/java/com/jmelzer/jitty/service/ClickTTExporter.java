/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.xml.clicktt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J. Melzer on 24.07.2017.
 * Exports the result of a tournament to a file.
 */
@Component
public class ClickTTExporter {

    private static final Logger LOG = LoggerFactory.getLogger(ClickTTExporter.class);

    DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

    DateFormat dfStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public void export(Tournament tournament, File output) {
        try {
            com.jmelzer.jitty.model.xml.clicktt.Tournament jaxbT = map(tournament);
            JAXBContext jaxbContext = JAXBContext.newInstance(com.jmelzer.jitty.model.xml.clicktt.Tournament.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(jaxbT, output);
        } catch (JAXBException e) {
            LOG.error("", e);
        }
    }

    public com.jmelzer.jitty.model.xml.clicktt.Tournament map(Tournament tournament) {
        com.jmelzer.jitty.model.xml.clicktt.Tournament jaxb = new com.jmelzer.jitty.model.xml.clicktt.Tournament();
        jaxb.setStartDate(dfDate.format(tournament.getStartDate()));
        jaxb.setEndDate(dfDate.format(tournament.getEndDate()));
        jaxb.setName(tournament.getName());
        jaxb.setTournamentId(tournament.getClickTTId());
        for (TournamentClass tc : tournament.getClasses()) {
            Competition competition = new Competition();
            jaxb.getCompetition().add(competition);
            competition.setStartDate(dfStartTime.format(tc.getStartTime()));
            competition.setAgeGroup(tc.getAgeGroup());
            competition.setType("Einzel"); //todo

            competition.setPlayers(new Players());

            Map<Long, Player> playerMap = new HashMap<>();

            for (TournamentPlayer tp : tc.getPlayers()) {
                Player player = new Player();
                playerMap.put(tp.getId(), player);
                Person person = new Person();
                player.getPerson().add(person);
                competition.getPlayers().getPlayer().add(player);

                player.setType("single"); //todo
                player.setId("PLAYER" + tp.getId());
                person.setFirstname(tp.getFirstName());
                person.setLastname(tp.getLastName());
                person.setLicenceNr(tp.getClickTTLicenceNr());
                person.setInternalNr(tp.getClickTTInternalNr());
                person.setBirthyear(tp.getBirthdayYearAsString());
                person.setSex(tp.getGender().equals(Gender.W.getValue()) ? "0" : "1");
                person.setTtr("" + tp.getQttr());
                if (tp.getClub() != null) {
                    person.setClubName(tp.getClub().getName());
                }
            }
            Matches matches = new Matches();
            competition.setMatches(matches);
            for (Phase phase : tc.getAllPhases()) {
                for (TournamentSingleGame game : phase.getAllSingleGames()) {
                    Match match = new Match();
                    match.setPlayerA(playerMap.get(game.getPlayer1().getId()));
                    match.setPlayerB(playerMap.get(game.getPlayer2().getId()));

                    match.setGamesA("" + game.getId());
                    match.setGamesB("" + game.getId());
                    match.setMatchesA(game.getWinner() == 1 ? "1" : "0");
                    match.setMatchesB(game.getWinner() == 2 ? "1" : "0");
                    match.setSetsA("" + game.getWonSetFor1());
                    match.setSetsB("" + game.getWonSetFor2());

                    List<GameSet> sets = game.getSets();
                    match.setSetA1("" + sets.get(0).getPoints1());
                    match.setSetB1("" + sets.get(0).getPoints2());
                    match.setSetA2("" + sets.get(1).getPoints1());
                    match.setSetB2("" + sets.get(1).getPoints2());
                    match.setSetA3("" + sets.get(2).getPoints1());
                    match.setSetB3("" + sets.get(2).getPoints2());
                    match.setSetA4(getSetResult(3, sets, true));
                    match.setSetB4(getSetResult(3, sets, false));
                    match.setSetA5(getSetResult(4, sets, true));
                    match.setSetB5(getSetResult(4, sets, false));
                    match.setSetA6(getSetResult(5, sets, true));
                    match.setSetB6(getSetResult(5, sets, false));
                    match.setSetA7(getSetResult(6, sets, true));
                    match.setSetB7(getSetResult(6, sets, false));
                    matches.getMatch().add(match);
                }


            }
        }
        return jaxb;
    }

    private String getSetResult(int n, List<GameSet> sets, boolean first) {
        if (sets.size() > n) {
            if (first) {
                return "" + sets.get(n).getPoints1();
            } else {
                return "" + sets.get(n).getPoints2();
            }

        } else {
            return "0";
        }
    }
}
