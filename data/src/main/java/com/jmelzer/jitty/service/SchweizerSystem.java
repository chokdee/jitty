/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;

import java.util.*;

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
public class SchweizerSystem {

    public List<TournamentPlayerDTO> calcRankingFirstRound(List<TournamentPlayerDTO> player) {
        player.sort((o1, o2) -> new Integer(o1.getQttr()).compareTo(o2.getQttr()) * -1);
        return player;
    }

    public void calcRankingRound(int roundNr, List<TournamentPlayerDTO> player) {
        //rankking of player
        for (TournamentPlayerDTO playerDTO : player) {
            playerDTO.calcWinningGames();
        }
        for (TournamentPlayerDTO playerDTO : player) {
            playerDTO.calcBuchholz();
        }

        player.sort((o1, o2) -> {

            int sComp = Integer.compare(o1.getWonGames(), o2.getWonGames()) * -1;

            if (sComp != 0) {
                return sComp;
            } else {
                return Integer.compare(o1.getBuchholzZahl(), o2.getBuchholzZahl()) * -1;
            }
        });

    }

    public List<TournamentSingleGameDTO> createGamesForTheFirstRound(List<TournamentPlayerDTO> player) {
        int size = player.size() / 2;
        List<TournamentSingleGameDTO> games = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            //todo handle bye
            TournamentSingleGameDTO game = new TournamentSingleGameDTO();
            game.setPlayer1(player.get(i));
            game.setPlayer2(player.get(player.size() - 1 - i));
            games.add(game);
        }
        return games;
    }

    public List<TournamentSingleGameDTO> createGamesForRound(int round, List<TournamentPlayerDTO> player, boolean bruteForce) {
        //build groups of player with the same amount of won games
        int size = player.size() / 2;
        List<TournamentSingleGameDTO> games = new ArrayList<>(size);
        Map<Integer, List<TournamentPlayerDTO>> hashMap = new HashMap<>();
        for (TournamentPlayerDTO playerDTO : player) {
            if (!hashMap.containsKey(playerDTO.getWonGames())) {
                List<TournamentPlayerDTO> list = new ArrayList<>();
                list.add(playerDTO);
                hashMap.put(playerDTO.getWonGames(), list);
            } else {
                hashMap.get(playerDTO.getWonGames()).add(playerDTO);
            }
        }
        if (!bruteForce) {
            for (int winningGames = round - 1; winningGames >= 0; winningGames--) {
                List<TournamentPlayerDTO> sameWinList = hashMap.get(winningGames);
                fillGames(round, player, games, hashMap, winningGames, sameWinList);
            }
        } else {
            List<TournamentPlayerDTO> allPlayer  = new ArrayList<>(player);
            Collections.shuffle(allPlayer);
            while (allPlayer.size() > 0) {
                TournamentSingleGameDTO game = new TournamentSingleGameDTO();
                TournamentPlayerDTO p1 = allPlayer.get(0);
                game.setPlayer1AndBackReference(p1);
                TournamentPlayerDTO p2 = getPlayerNotPlayedEachOther(p1, allPlayer);
                if (p2 == null) throwNoResult(round, player, games);
                game.setPlayer2AndBackReference(p2);
                allPlayer.remove(p1);
                allPlayer.remove(p2);
                games.add(game);
            }
        }
        return games;
    }

    private void fillGames(int round, List<TournamentPlayerDTO> player, List<TournamentSingleGameDTO> games, Map<Integer, List<TournamentPlayerDTO>> hashMap, int winningGames, List<TournamentPlayerDTO> sameWinList) {
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
                throwNoResult(round, player, games);
                return;
            }
            game.setPlayer1(p1);
            game.setPlayer2(p2);
            games.add(game);


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
        if (p1 != null) return p1;
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

}
