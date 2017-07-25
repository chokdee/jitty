/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.Gender;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.xml.clicktt.Competition;
import com.jmelzer.jitty.model.xml.clicktt.Person;
import com.jmelzer.jitty.model.xml.clicktt.Player;
import com.jmelzer.jitty.model.xml.clicktt.Players;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

    private com.jmelzer.jitty.model.xml.clicktt.Tournament map(Tournament tournament) {
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

            for (TournamentPlayer tp : tc.getPlayers()) {
                Player player = new Player();
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
        }
        return jaxb;
    }
}
