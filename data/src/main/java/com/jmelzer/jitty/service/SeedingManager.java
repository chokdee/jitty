package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.TournamentGroupDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.utl.RandomUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by J. Melzer on 17.06.2016.
 * Seeding operations for KO and Group fields
 */
@Component
public class SeedingManager {

    List<TournamentPlayer> prepareRunnerupList(List<TournamentGroup> groups) {
        List<TournamentPlayer> second = new ArrayList<>();
        int r = groups.size()+1;
        for (TournamentGroup group : groups) {
            second.add(group.getRanking().get(1).player);
            group.getRanking().get(1).player.ranking = r++;
        }
        //sort with qttr
        Collections.sort(second, (o1, o2) -> Integer.compare(o1.getQttr(), o2.getQttr()) * -1);
        return second;
    }
    List<TournamentPlayer> prepareWinnerList(List<TournamentGroup> groups) {
        List<TournamentPlayer> winner = new ArrayList<>();
        for (TournamentGroup group : groups) {
            winner.add(group.getRanking().get(0).player);
        }
        //sort with qttr
        Collections.sort(winner, (o1, o2) -> Integer.compare(o1.getQttr(), o2.getQttr()) * -1);
        return winner;
    }
    /**
     * calculate the first round of a KO field
     *
     * @param field  to store the results
     * @param groups for read from
     * @return list of games
     */
    public List<TournamentSingleGame> assignPlayerToKoField(KOField field, List<TournamentGroup> groups) {
        List<TournamentPlayer> winner = prepareWinnerList(groups);
        List<TournamentPlayer> second = prepareRunnerupList(groups);

        Round round = field.getRound();
        int fieldSize = round.playerSize();
        TournamentPlayer[] players = new TournamentPlayer[fieldSize + 1];

        List<TournamentPlayer> rest = new ArrayList<>(winner);
        List<Integer> positionOfSeededPlayers = new ArrayList<>();

        for (int i = 0; i < winner.size() && i < fieldSize; i++) {
            TournamentPlayer player = winner.get(i);
            if (i < 16) {
                rest.remove(player);
            }
            int pos = -1;
            switch (i) {
                case 0:
                    pos = 1;
                    break;
                case 1:
                    pos = fieldSize;
                    break;
                case 2:
                    pos = fieldSize / 2 + 1;
                    break;
                case 3:
                    pos = fieldSize / 2;
                    break;
                case 4:
                    pos = fieldSize / 2 - fieldSize / 4 + 1;
                    break;
                case 5:
                    pos = (fieldSize / 2) + (fieldSize / 4);
                    break;
                case 6:
                    pos = fieldSize - (fieldSize / 4) + 1;
                    break;
                case 7:
                    pos = fieldSize / 4;
                    break;
                case 8:
                    pos = (fieldSize / 8) + 1;
                    break;
                case 9:
                    pos = fieldSize - (fieldSize / 8);
                    break;
                case 10:
                    pos = fieldSize / 2 + (fieldSize / 8) + 1;
                    break;
                case 11:
                    pos = fieldSize / 2 - (fieldSize / 8);
                    break;
                case 12:
                    pos = fieldSize / 2 - (fieldSize / 8) + 1;
                    break;
                case 13:
                    pos = fieldSize / 2 + 1 + (fieldSize / 16);
                    break;
                case 14:
                    pos = fieldSize - (fieldSize / 16);
                    break;
                case 15:
                    pos = (fieldSize / 8);
                    break;
                default:
                    break;
                //todo add more if needed
            }
            if (pos >= 0) {
                positionOfSeededPlayers.add(pos - 1); //0 indexed;
                players[pos] = player;
            }
        }

        //transform in to games
        List<TournamentSingleGame> games = new ArrayList<>();
        for (int i = 1; i <= fieldSize; i++) {
            TournamentSingleGame game = new TournamentSingleGame();
            game.setGameName(""+i);
            TournamentPlayer player1 = players[i];
            TournamentPlayer player2 = players[++i];
            game.setPlayer1(player1);
            game.setPlayer2(player2);
            games.add(game);
        }

        rest.addAll(second);

        freePlacesToTopSeededPlayers(fieldSize, rest, positionOfSeededPlayers, games);


        fillupWithOtherPlayer(rest, games);
        field.getRound().addAllGames(games);

        return games;
    }

    /**
     * Sind Freilose notwendig, um die Feldgröße eines KO-Felds (8; 16; 32; ...) zu erreichen, so
     * werden diese Freilose in der 1. KO-Runde den auf die Positionen 1 bis G gesetzten Spielern zugewiesen.
     * <p>
     * <p>
     * set the no of free places to the top seeded players
     *
     * @param fieldSize               field size
     * @param rest                    other player
     * @param positionOfSeededPlayers position of seeded players in list
     * @param games                   to be filled
     */
    private void freePlacesToTopSeededPlayers(int fieldSize, List<TournamentPlayer> rest, List<Integer> positionOfSeededPlayers, List<TournamentSingleGame> games) {
        int freePos = fieldSize - positionOfSeededPlayers.size() - rest.size();
        for (int i = 0; i < positionOfSeededPlayers.size() && i < freePos; i++) {
            int pos = positionOfSeededPlayers.get(i);
            int gamePos = pos / 2;
            if (gamePos >= games.size()) {
                gamePos--;
            }
            TournamentSingleGame game = games.get(gamePos);
            if (game.getPlayer1() != null) {
                if (game.getPlayer2() == null) {
                    game.setPlayer2(TournamentPlayer.BYE);
                }
            } else if (game.getPlayer2() != null) {
                if (game.getPlayer1() == null) {
                    game.setPlayer1(TournamentPlayer.BYE);
                }
            }
        }
    }

    /**
     * fill up with other if free places are avaible
     *
     * @param rest  other player
     * @param games to be filled
     */
    private void fillupWithOtherPlayer(List<TournamentPlayer> rest, List<TournamentSingleGame> games) {
        for (int run = 0; run < 2; run++) {
            //need two runs here

            for (int i = 0; i < games.size(); i++) {
                TournamentSingleGame game = games.get(i);
                if (game.getPlayer1() != null && game.getPlayer2() != null) {
                    continue;
                }
                if (rest.size() == 0) {
                    break;
                }
                int n = RandomUtil.randomIntFromInterval(0, rest.size() - 1);
                if (game.getPlayer1() == null) {
                    game.setPlayer1(rest.get(n));
                    rest.remove(n);
                } else if (game.getPlayer2() == null) {
                    game.setPlayer2(rest.get(n));
                    rest.remove(n);
                }
            }
        }
    }

    //todo don't forget the rules see auslosung.txt
    public void setPlayerRandomAccordingToQTTR(List<TournamentGroupDTO> groups, List<TournamentPlayerDTO> allPlayer) {
        Random randomGenerator = new Random();
        System.out.println("set player for #" + groups.size() + " groups and #" + allPlayer.size() + " players");
        int groupCount = groups.size();
        while (allPlayer.size() > 0) {
            //set the best n player in the groups
            for (int i = 0; i < groupCount; i++) {
                int index = randomGenerator.nextInt(groupCount - i);
                if (index >= allPlayer.size()) {
//                    index--;
                    if (allPlayer.size() < 2) {
                        index = 0;
                    } else {
                        index = randomGenerator.nextInt(allPlayer.size() - 1);
                    }
                }
                if (index == -1 || allPlayer.size() == 0) {
                    break;
                }
                TournamentPlayerDTO p = allPlayer.get(index);
                allPlayer.remove(index);
                groups.get(i).addPlayer(p);

            }
        }
    }


}
