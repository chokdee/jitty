
/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model.xml.clicktt;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jmelzer.jitty.model.xml.playerimport package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jmelzer.jitty.model.xml.playerimport
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Players }
     * 
     */
    public Players createPlayers() {
        return new Players();
    }

    /**
     * Create an instance of {@link Player }
     * 
     */
    public Player createPlayer() {
        return new Player();
    }

    /**
     * Create an instance of {@link Person }
     * 
     */
    public Person createPerson() {
        return new Person();
    }

    /**
     * Create an instance of {@link Match }
     * 
     */
    public Match createMatch() {
        return new Match();
    }

    /**
     * Create an instance of {@link TournamentLocation }
     * 
     */
    public TournamentLocation createTournamentLocation() {
        return new TournamentLocation();
    }

    /**
     * Create an instance of {@link Competition }
     * 
     */
    public Competition createCompetition() {
        return new Competition();
    }

    /**
     * Create an instance of {@link Matches }
     * 
     */
    public Matches createMatches() {
        return new Matches();
    }

    /**
     * Create an instance of {@link Tournament }
     * 
     */
    public Tournament createTournament() {
        return new Tournament();
    }

}
