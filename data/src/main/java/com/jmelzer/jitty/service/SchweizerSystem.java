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

    public List<TournamentSingleGameDTO> createGamesForRound(int i, List<TournamentPlayerDTO> player) {
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
        for (int winningGames = i-1; winningGames >= 0; winningGames--) {
            List<TournamentPlayerDTO> sameWinList = hashMap.get(winningGames);

            while (sameWinList.size() > 0) {
//                    RandomUtil randomUtil = new RandomUtil(0, sameWinList.size() - 1);
//                    int n = randomUtil.nextInt();
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
                    throw new RuntimeException("retry");
                }
                game.setPlayer1(p1);
                game.setPlayer2(p2);
                games.add(game);


            }
        }
        return games;
    }

    private TournamentPlayerDTO getPlayerFromNextLevel(Map<Integer, List<TournamentPlayerDTO>> hashMap, int key, TournamentPlayerDTO p) {
        List<TournamentPlayerDTO> nextLevel = hashMap.get(key);
        if (nextLevel == null) {
            System.out.println("no opponent found for player " + p);
            return null;
        }
        Collections.shuffle(nextLevel);
        for (TournamentPlayerDTO p1 : nextLevel) {
            if (!p1.playedAgainst(p)) {
                nextLevel.remove(p1);
                return p1;
            }

        }
        return getPlayerFromNextLevel(hashMap, key - 1, p);
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
