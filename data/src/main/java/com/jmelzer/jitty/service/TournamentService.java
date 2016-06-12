package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.TournamentSingleGame;

import java.util.*;

/**
 * Created by J. Melzer on 01.06.2016.
 * manage the tournament
 */
public class TournamentService {

    List<TournamentSingleGame> busyGames = new ArrayList<>();

    public List<TournamentGroup> caluculateGroups(TournamentClass tournamentClass) {
        //first calc the field size , 16 / 32 etc
        //todo smallest possible size calucation
        int ps = tournamentClass.getPlayers().size();
        int fieldSize = 0;
        for (int i = 4; i < 8; i++) {
            fieldSize = (int) Math.pow(2, i);
            if (fieldSize > ps) {
                System.out.println("found field size " + (int) Math.pow(2, i) + " for player size " + ps);
                break;
            }
        }


        int groupCount = calcOptimalGroupSize(ps);

        List<TournamentGroup> groups = createGroups(groupCount);

        List<TournamentPlayer> allPlayer = new ArrayList<>(tournamentClass.getPlayers());
        Random randomGenerator = new Random();

        //sort all player by qttr
        Collections.sort(allPlayer, (o1, o2) -> {
            if (o1.getQttr() < o2.getQttr()) {
                return 1;
            }
            if (o1.getQttr() > o2.getQttr()) {
                return -1;
            }
            return 0;
        });

        //todo don't forget the rules see auslosung.txt
        setPlayerRandomAccordingToQTTR(groupCount, groups, allPlayer, randomGenerator);


        return groups;
    }


    private void setPlayerRandomAccordingToQTTR(int groupCount, List<TournamentGroup> groups, List<TournamentPlayer> allPlayer, Random randomGenerator) {
        while (allPlayer.size() > 0) {
            //set the best n player in the groups
            for (int i = 0; i < groupCount; i++) {
                int index = randomGenerator.nextInt(groupCount - i);
                if (index >= allPlayer.size()) {
                    index--;
                }
                if (index == -1 || allPlayer.size() == 0) {
                    break;
                }
                TournamentPlayer p = allPlayer.get(index);
                allPlayer.remove(index);
                groups.get(i).addPlayer(p);

            }
        }
    }

    /**
     * iterate thrw all groups and add not played games. but take care of the player isn't already in the list
     *
     * @param groups
     * @param gameQueue
     * @param busyGames
     */
    public void addPossibleGamesToQueue(List<TournamentGroup> groups, Collection<TournamentSingleGame> gameQueue, Collection<TournamentSingleGame> busyGames) {
        for (TournamentGroup group : groups) {
            List<TournamentSingleGame> games = group.getGames();
            for (TournamentSingleGame game : games) {
                TournamentPlayer player1 = game.getPlayer1();
                TournamentPlayer player2 = game.getPlayer2();
                if (!playerInQueue(player1, gameQueue) && !playerInQueue(player2, gameQueue) &&
                        !playerInQueue(player1, busyGames) && !playerInQueue(player2, busyGames) &&
                        !game.isFinishedOrCalled()) {
                    gameQueue.add(game);
                }
            }
        }

    }

    private boolean playerInQueue(TournamentPlayer player, Collection<TournamentSingleGame> gameCollection) {
        for (TournamentSingleGame game : gameCollection) {
            if (game.getPlayer1().equals(player) || game.getPlayer2().equals(player)) {
                return true;
            }
        }
        return false;
    }

    private int calcOptimalGroupSize(int playerSize) {
        //how many groups?
        int optGroupSize = 4; //todo config it or let the user do manual move players
        int groupCount = playerSize / optGroupSize;
        int rest = (playerSize % optGroupSize);

        if (rest > 0) {
            groupCount++;
        }

        return groupCount;
    }

    private List<TournamentGroup> createGroups(int groupCount) {
        List<TournamentGroup> groups = new ArrayList<>(groupCount);
        char name = 'A';
        for (int i = 0; i < groupCount; i++) {
            TournamentGroup group = new TournamentGroup("" + name);
            groups.add(group);
            name++;
        }
        return groups;
    }


    /**
     * calculate the order of the possible games in the correct order
     *
     * @param groups for calc
     */
    public void calcGroupGames(List<TournamentGroup> groups) {
        for (TournamentGroup group : groups) {
            System.out.println(group);
//                List<TournamentPlayer> players = group.getPlayers();
            List<TournamentPlayer> list = new ArrayList<>(group.getPlayers());
            if (list.size() % 2 == 1) {
                // Number of teams uneven ->  add the bye team.
                list.add(new TournamentPlayer("bye", "bye"));
            }
            for (int i = 1; i < list.size(); i++) {


                System.out.println("---- games round " + i + " ----");
                //first 1 against last

                List<TournamentSingleGame> games = createOneRound(i, list);
                group.addGames(games);

                list.add(1, list.get(list.size() - 1));
                list.remove(list.size() - 1);

                System.out.println("-----------------");
            }
        }

    }

    /**
     * Creates one round, i.e. a set of matches where each team plays once.
     *
     * @param round   Round number.
     * @param players List of players
     */
    private List<TournamentSingleGame> createOneRound(int round, List<TournamentPlayer> players) {
        int mid = players.size() / 2;
        // Split list into two

        List<TournamentPlayer> l1 = new ArrayList<>();
        // Can't use sublist (can't cast it to ArrayList - how stupid is that)??
        for (int j = 0; j < mid; j++) {
            l1.add(players.get(j));
        }

        List<TournamentPlayer> l2 = new ArrayList<>();
        // We need to reverse the other list
        for (int j = players.size() - 1; j >= mid; j--) {
            l2.add(players.get(j));
        }
        List<TournamentSingleGame> games = new ArrayList<>();
        for (int tId = 0; tId < l1.size(); tId++) {
            TournamentPlayer t1;
            TournamentPlayer t2;
            // Switch sides after each round
            if (round % 2 == 1) {
                t1 = l1.get(tId);
                t2 = l2.get(tId);
            } else {
                t1 = l2.get(tId);
                t2 = l1.get(tId);
            }
            TournamentSingleGame game = new TournamentSingleGame();
            game.setPlayer1(t1);
            game.setPlayer2(t2);
            games.add(game);
            System.out.println(t1.getLastName() + " --> " + t2.getLastName());
//            System.out.println("" + round + ":" +  ((round-1)*l1.size())+(tId+1) + " " + t1.getLastName() + " -->" + t2.getLastName());
//            matches.addNew(round, ((round-1)*l1.size())+(tId+1), (String)t1.get("name"), (String)t2.get("name"));
        }
        return games;
    }

    public void addBusyGame(TournamentSingleGame game) {
        busyGames.add(game);
    }

    public List<TournamentSingleGame> getBusyGames() {
        return busyGames;
    }

    public void removeBusyGame(TournamentSingleGame game) {
        busyGames.remove(game);
    }
}
