/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentClassRepository;
import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.SwissDraw;
import com.jmelzer.jitty.model.dto.TournamentClassStatus;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static com.jmelzer.jitty.service.CopyManager.copy;

/**
 * Created by J. Melzer .<br><br>
 * <br>
 * Die Vorgaben der WO hier im Wortlaut:
 * <p>
 * Ähnlich dem System „Jeder gegen jeden“, wobei nicht alle Runden ausgetragen
 * werden. Die Anzahl der Runden  entspricht mindestens der Anzahl der Runden eines K.-o.-Systems der entsprechenden Teilnehmerzahl, ist im
 * Idealfall allerdings um zwei größer. Jeder Teilnehmer spielt in jeder Runde gegen einen anderen Gegner. Bei einer ungeraden Anzahl von Spielern hat
 * in jeder Runde ein anderer Spieler ein Freilos, das als gewonnenes Spiel gewertet wird.<br><br>
 * Die Spielpaarungen in jeder Runde werden so gebildet, dass möglichst jeweils Spieler mit gleich vielen Siegen gegeneinander antreten.
 * Spieler mit der höchsten Anzahl von Siegen, für die danach keine Paarung gebildet werden konnte, erhalten einen Gegner mit der nächsttieferen Anzahl von
 * Siegen. Zuletzt werden die sieglosen Spieler gegeneinander gepaart bzw. ein Freilos ausgegeben.<br>
 * Bei der Auslosung der ersten Runde sollten möglichst die stärksten Spieler gesetzt werden. Vor der Auslosung der nächsten Runden ist der aktuelle Zwischenstand zu berechnen und die Spieler nac
 * h Anzahl der Siege zu sortieren.<br><br>
 * Spieler mit gleicher Anzahl von Siegen können untereinander durch die Anzahl der Siege ihrer bisherigen Gegner
 * (Buchholzzahl) feinsortiert werden, wobei Freilosspiele mit der Sieganzahl des Tabe llenletzten gewertet werden.<br>
 * Gibt ein Spieler eines seiner Gruppenspiele kampflos ab oder beendet er eines dieser Gruppenspiele vorzeitig,
 * kann er nicht weiter am Turnier teilnehmen. Er wird jedoch mit den erzielten Siegen weiter in der Spieler-Rangliste
 * des Turniers geführt, wobei alle weiteren Spiele kampflos für den jeweiligen Gegner gewertet werden.<br><br>
 * Nach der letzten Runde hat der Spieler mit den meisten Siegen das Turnier gewonnen; bei gleicher Anzahl an Siegen ist die Buchholzzahl maßgeblich.
 * Ist auch diese gleich entscheidet der direkte Vergleich.<br><br>
 */
@Component
public class SwissSystemManager {

    static final Logger LOG = LoggerFactory.getLogger(SwissSystemManager.class);

    @Resource
    TournamentClassRepository tcRepository;

    @Autowired
    TournamentService tournamentService;

    @Resource
    QueueManager queueManager;

    @Resource
    TournamentPlayerRepository playerRepository;

    public SwissDraw createGamesForTheFirstRound(List<TournamentPlayerDTO> player) {
        int size = player.size() / 2;
        List<TournamentPlayerDTO> copyList = new ArrayList<>(player);
        List<TournamentSingleGameDTO> games = new ArrayList<>(size);
        SwissDraw swissDraw = new SwissDraw();
        for (int i = 0; i < size; i++) {
            //todo handle bye
            TournamentSingleGameDTO game = new TournamentSingleGameDTO();
            game.setPlayer1AndBackReference(player.get(i));
            game.setPlayer2AndBackReference(player.get(player.size() - 1 - i));
            games.add(game);
            copyList.remove(player.get(i));
            copyList.remove(player.get(player.size() - 1 - i));
        }
        if (isOdd(size)) {
            assert copyList.size() == 1;
            copyList.get(0).setFreilos(true);
            swissDraw.setFreilos(copyList.get(0));
        }
        swissDraw.setGames(games);
        return swissDraw;
    }

    public SwissDraw createGamesForRound(int round, List<TournamentPlayerDTO> player, boolean bruteForce) {
        if (round == 1) {
            List<TournamentPlayerDTO> sortedPlayer = calcRankingFirstRound(player);
            return preventStackOverflow(createGamesForTheFirstRound(sortedPlayer));

        }
        List<TournamentPlayerDTO> realPlayer = new ArrayList<>();
        for (TournamentPlayerDTO p : player) {
            if (!p.isSuspended()) {
                realPlayer.add(p);
            }
        }
        //build groups of player with the same amount of won games
        int size = realPlayer.size() / 2;
        List<TournamentSingleGameDTO> games = new ArrayList<>(size);
        Map<Integer, List<TournamentPlayerDTO>> hashMap = new HashMap<>();
        for (TournamentPlayerDTO playerDTO : realPlayer) {
            if (!hashMap.containsKey(playerDTO.getWonGames())) {
                List<TournamentPlayerDTO> list = new ArrayList<>();
                list.add(playerDTO);
                hashMap.put(playerDTO.getWonGames(), list);
            } else {
                hashMap.get(playerDTO.getWonGames()).add(playerDTO);
            }
        }
        SwissDraw swissDraw = new SwissDraw(games);
        if (!bruteForce) {
//            in jeder Runde ein anderer Spieler
            for (int winningGames = round - 1; winningGames >= 0; winningGames--) {
                List<TournamentPlayerDTO> sameWinList = hashMap.get(winningGames);
                fillGames(round, realPlayer, swissDraw, hashMap, winningGames, sameWinList);
            }
            return preventStackOverflow(swissDraw);
        } else {
            List<TournamentPlayerDTO> allPlayer = new ArrayList<>(realPlayer);
            Collections.shuffle(allPlayer);

            while (allPlayer.size() > 1) {
                TournamentSingleGameDTO game = new TournamentSingleGameDTO();
                TournamentPlayerDTO p1 = allPlayer.get(0);
                game.setPlayer1AndBackReference(p1);
                TournamentPlayerDTO p2 = getPlayerNotPlayedEachOther(p1, allPlayer);
                if (p2 == null) {
                    throwNoResult(round, realPlayer, games);
                }
                game.setPlayer2AndBackReference(p2);
                allPlayer.remove(p1);
                allPlayer.remove(p2);
                games.add(game);
            }
            if (allPlayer.size() == 1) {
                swissDraw.setFreilos(allPlayer.get(0));
            }
            swissDraw.setGames(preventStackOverflow(games));
        }
        //prevent stackoverflow
        return swissDraw;
    }

    private SwissDraw preventStackOverflow(SwissDraw swissDraw) {
        preventStackOverflow(swissDraw.getGames());
        return swissDraw;
    }

    private List<TournamentSingleGameDTO> preventStackOverflow(List<TournamentSingleGameDTO> games) {
        for (TournamentSingleGameDTO game : games) {
            if (game.getPlayer1() != null) {
                game.getPlayer1().clearGames();
            }
            if (game.getPlayer2() != null) {
                game.getPlayer2().clearGames();
            }
        }
        return games;
    }

    boolean isOdd(int x) {
        return (x & 1) != 0;
    }

    /**
     * filling the games for player with the same amount of won games.
     * it shuffles the player, so the result will no be the same every time
     * if not enough player get the next from the less amount of won games
     *
     * @param player       list of player
     * @param swissDraw    to be filled
     * @param hashMap      Map of the level (amount of won games)
     * @param winningGames amount of won games
     * @param sameWinList  list of player with same win (todo do we need this?)
     */
    private void fillGames(int round, List<TournamentPlayerDTO> player, SwissDraw swissDraw, Map<Integer, List<TournamentPlayerDTO>> hashMap,
                           int winningGames, List<TournamentPlayerDTO> sameWinList) {
        while (sameWinList != null && sameWinList.size() > 0) {
            Collections.shuffle(sameWinList);
            TournamentSingleGameDTO game = new TournamentSingleGameDTO();
            TournamentPlayerDTO p1 = sameWinList.get(0);
            sameWinList.remove(p1);

            TournamentPlayerDTO p2 = getRandomPlayerWithSameWinCount(sameWinList, p1);
            if (p2 == null) {
                p2 = getPlayerFromNextLevel(hashMap, winningGames - 1, p1);
            } else {
                sameWinList.remove(p2);
            }

            if (p2 == null) {
                if (!isOdd(player.size())) {
                    throwNoResult(round, player, swissDraw.getGames());
                } else {

                    //allready freilos?
                    if (p1.getGamesCount() == round - 1) {
                        if (p1.getFullName().equals("Frank Pusteblume")) {
                            System.err.println("");
                        }
                        swissDraw.setFreilos(p1);
                        p1.setFreilos(true);
                    } else {
                        throwNoResult(round, player, swissDraw.getGames());
                    }
                }
                return;
            }

            game.setPlayer1(p1);
            game.setPlayer2(p2);
            swissDraw.getGames().add(game);


        }
    }

    private void throwNoResult(int round, List<TournamentPlayerDTO> player, List<TournamentSingleGameDTO> games) {
        System.out.println("games before exception");
        for (TournamentSingleGameDTO g : games) {
            System.out.println(g);
        }
        cleanGames(player, round);
        throw new SwissRuntimeException("retry");
    }

    private void cleanGames(List<TournamentPlayerDTO> player, int round) {
        for (TournamentPlayerDTO playerDTO : player) {
            if (playerDTO.getPlayedGames().size() >= round) {
                playerDTO.removeLastGame();
            }
        }
    }

    private TournamentPlayerDTO getPlayerFromNextLevel(Map<Integer, List<TournamentPlayerDTO>> hashMap, int key, TournamentPlayerDTO p) {
        List<TournamentPlayerDTO> nextLevel = hashMap.get(key);
        if (nextLevel == null) {
            System.out.println("no opponent found for player " + p);
            return null;
        }
        Collections.shuffle(nextLevel);
        TournamentPlayerDTO p1 = getPlayerNotPlayedEachOther(p, nextLevel);
        if (p1 != null) {
            return p1;
        }
        return getPlayerFromNextLevel(hashMap, key - 1, p);
    }

    private TournamentPlayerDTO getPlayerNotPlayedEachOther(TournamentPlayerDTO p, List<TournamentPlayerDTO> list) {
        for (TournamentPlayerDTO p1 : list) {
            if (!p1.playedAgainst(p)) {
                list.remove(p1);
                return p1;
            }

        }
        return null;
    }

    TournamentPlayerDTO getRandomPlayerWithSameWinCount(List<TournamentPlayerDTO> players, TournamentPlayerDTO player) {
        for (TournamentPlayerDTO p : players) {
            if (!player.playedAgainst(p)) {
                return p;
            }
        }
        return null;
    }

    @Transactional
    public void startClass(Long id) throws IntegrityViolation {

        TournamentClass clz = tcRepository.findOne(id);
        if (clz.getActivePhase() == null) {
            tournamentService.selectPhaseCombination(id, PhaseCombination.SWS);
        }
        if (clz.getRunning() != null && clz.getRunning()) {
            throw new IntegrityViolation("Die Klasse wurde bereits gestartet");
        }
        clz.setStartTime(new Date());
        clz.setRunning(true);
        LOG.info("started swiss class {}", id);
        clz = tcRepository.saveAndFlush(clz);

        assert clz.getActivePhase() != null;
    }

    @Transactional(readOnly = true)
    public int getRoundNr(Long id) {
        TournamentClass clz = tcRepository.findOne(id);
        SwissSystemPhase swissSystemPhase = (SwissSystemPhase) clz.getActivePhase();
        if (swissSystemPhase.isFinished()) {
            return swissSystemPhase.getRound() + 1;
        } else {
            return swissSystemPhase.getRound();
        }
    }

    @Transactional
    public void startRound(long classId, int round, List<TournamentSingleGameDTO> games) throws IntegrityViolation {
        if (games.isEmpty()) {
            throw new IntegrityViolation("Anzahl der Spiele sind 0");
        }
        TournamentClass clz = tcRepository.findOne(classId);
        createtNextSwissRoundIfNecessary(classId, round);


        SwissSystemPhase swissSystemPhase = (SwissSystemPhase) clz.getActivePhase();
        swissSystemPhase.setRound(round);
        LOG.info("Started Round #{}", round);
        clz = tcRepository.saveAndFlush(clz);

        List<TournamentSingleGame> pGames = copy(games, clz.getName(), clz.getTournament().getId());
        for (int i = 0; i < games.size(); i++) {
            TournamentSingleGame pGame = pGames.get(i);
            pGame.setPlayer1(playerRepository.findOne(games.get(i).getPlayer1().getId()));
            pGame.setPlayer2(playerRepository.findOne(games.get(i).getPlayer2().getId()));
            tournamentService.save(pGame);
            swissSystemPhase.getGroup().addGame(pGame);
        }

        queueManager.addAll(pGames);

    }

    /**
     * create the next round object if the actual round is finished
     *
     * @param cid         class id
     * @param actualRound actual round number
     * @return # of actual active round
     * @throws IntegrityViolation if error
     */
    @Transactional
    public int createtNextSwissRoundIfNecessary(Long cid, int actualRound) throws IntegrityViolation {
        TournamentClass clz = tcRepository.findOne(cid);

        if (clz.calcStatus() == TournamentClassStatus.FINISHED) {
            throw new IntegrityViolation("Die Turnierklasse ist abgeschlossen");
        }

        SwissSystemPhase activePhase = (SwissSystemPhase) clz.getActivePhase();
        if (activePhase.isFinished()) {
            int pc = clz.getPhaseCount();
            int nextRound = pc + 1;
            //don't create more than necessary
            if (pc < actualRound + 1) {
                SwissSystemPhase phase = new SwissSystemPhase("Runde " + (nextRound));
                clz.addPhase(phase);
                clz.setActivePhaseNo(pc); //zero based
                phase.setRound(nextRound);

                LOG.info("created Round #{}", (nextRound));

                tcRepository.saveAndFlush(clz);
            }


        }

        activePhase = (SwissSystemPhase) clz.getActivePhase();
        return activePhase.getRound();
    }

    @Transactional
    public SwissDraw calcDraw(Long cid) {
        List<TournamentPlayerDTO> ps = tournamentService.getPlayerforClass(cid);
        TournamentClass tc = tcRepository.findOne(cid);
        if (tc.getActivePhase() == null) {
            tournamentService.selectPhaseCombination(cid, PhaseCombination.SWS);
            tc = tcRepository.findOne(cid);
        }

        for (Phase phase : tc.getSystem().getPhases()) {
            SwissSystemPhase sphase = (SwissSystemPhase) phase;
            for (TournamentSingleGame tournamentSingleGame : sphase.getGroup().getGames()) {
                for (TournamentPlayerDTO p : ps) {
                    if (p.getId().equals(tournamentSingleGame.getPlayer1().getId())) {
                        p.inkrementGamesCount();
                    }
                    if (p.getId().equals(tournamentSingleGame.getPlayer2().getId())) {
                        p.inkrementGamesCount();
                    }
                }
            }
        }
        SwissSystemPhase phase = ((SwissSystemPhase) tc.getActivePhase());
        TournamentSystemType stype = TournamentSystemType.enumOf(tc.getSystemType());
        int round = phase.getRound();
        calcRankingRound(stype, round, ps);
        for (int i = 1; i <= 100; i++) {
            try {
                LOG.debug("------ #" + i + " run -------------- ");
                return createGamesForRound(round, ps, i > 80);
            } catch (SwissRuntimeException e) {
                LOG.debug("catched exception but ignoring");
            }
        }
        //tod exception
        return createGamesForRound(round, ps, false);
    }

    @Transactional
    public List<TournamentPlayerDTO> getRanking(Long cid) {
        List<TournamentPlayerDTO> ps = tournamentService.getPlayerforClass(cid);
        TournamentClass tc = tcRepository.findOne(cid);
        int round = 1;
        if (tc.getActivePhase() != null) {
            round = ((SwissSystemPhase) tc.getActivePhase()).getRound();
        } else {
            tournamentService.selectPhaseCombination(cid, PhaseCombination.SWS);
        }
        calcRankingRound(TournamentSystemType.enumOf(tc.getSystemType()), round, ps);
        //clear games, we don't need at the client
        for (TournamentPlayerDTO p : ps) {
            p.clearGames();
            if (p.isSuspended()) {
                p.setSuspendedText("Spieler hat aufgegeben");
            }
        }
        return ps;

    }

    public void calcRankingRound(TournamentSystemType stype, int roundNr, List<TournamentPlayerDTO> player) {
        if (roundNr == 1) {
            calcRankingFirstRound(player);
        }

        //calculate parameter for ranking of the player
        for (TournamentPlayerDTO playerDTO : player) {
            playerDTO.calcWinningGames();
        }
        for (TournamentPlayerDTO playerDTO : player) {
            playerDTO.calcBuchholz(player);
        }
        for (TournamentPlayerDTO playerDTO : player) {
            playerDTO.calcFeinBuchholz(player);
        }

        //todo add sorting for different Verbaende
        if (stype == TournamentSystemType.AC) {
            WTTVCupSorter.sort(player);
        } else {
            throw new UnsupportedOperationException("not yet implemented for " + stype);
        }


    }

    public List<TournamentPlayerDTO> calcRankingFirstRound(List<TournamentPlayerDTO> player) {
        player.sort((o1, o2) -> new Integer(o1.getQttr()).compareTo(o2.getQttr()) * -1);
        return player;
    }
}
