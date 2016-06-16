package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.utl.RandomUtil;

import java.util.*;

/**
 * Created by J. Melzer on 01.06.2016.
 * manage the tournament
 */
public class TournamentService {

    List<TournamentSingleGame> busyGames = new ArrayList<>();
    List<TournamentGroup> groups = new ArrayList<>();

    public void caluculateGroups(TournamentClass tournamentClass) {
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

        groups = createGroups(groupCount);

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

    }

    public List<TournamentGroup> getGroups() {
        return Collections.unmodifiableList(groups);
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
     * @param gameQueue
     * @param busyGames
     */
    public void addPossibleGamesToQueue(Collection<TournamentSingleGame> gameQueue, Collection<TournamentSingleGame> busyGames) {
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
     */
    public void calcGroupGames() {
        for (TournamentGroup group : groups) {
            System.out.println(group);
//                List<TournamentPlayer> players = group.getPlayers();
            List<TournamentPlayer> list = new ArrayList<>(group.getPlayers());
            if (list.size() % 2 == 1) {
                // Number of teams uneven ->  add the bye team.
                list.add(new TournamentPlayer(-1L, "bye", "bye"));
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

    public void markGroupWinner() {
        for (TournamentGroup group : groups) {
            System.out.println("----------------------------------");
            System.out.println("#\tSpieler\t\t\t\t\tSpiele\tSätze");
            calcRankingForGroup(group);
            List<TournamentService.PS> pss = group.getRanking();
            int i = 1;
            for (PS ps : pss) {
                System.out.println("" + i + "\t" + ps.player.getFullName() + "\t" + ps.win + ":" + ps.lose + "\t\t" + ps.setsWon + ":" + ps.setsLost);
                i++;
            }
            System.out.println("----------------------------------  ");
        }
    }

    public void addGroup(TournamentGroup group) {
        groups.add(group);
    }

    public KOField createKOField(RoundType roundType) {
        KOField field = new KOField();
        field.setRound(new Round(roundType));
        switch (roundType) {
            case R128:
                createSubRounds(field.getRound(), 6, 64);
                break;
            case R64:
                createSubRounds(field.getRound(), 5, 32);
                break;
            case R32:
                createSubRounds(field.getRound(), 4, 16);
                break;
            case R16:
                createSubRounds(field.getRound(), 3, 8);
                break;
            case QUARTER:
                createSubRounds(field.getRound(), 2, 4);
                break;
            default:
                throw new RuntimeException("not yet implemented");
        }
        return field;
    }

    private void createSubRounds(Round round, int i, int size) {
        Round lastRound = round;
        lastRound.setSize(size * 2);
        for (int j = 0; j < i; j++) {
            lastRound.setNextRound(new Round(size));
            size = size / 2;
            lastRound = lastRound.getNextRound();
        }
    }

    public List<TournamentSingleGame> assignPlayerToKoField(KOField field) {
        List<TournamentPlayer> winner = new ArrayList<>();
        for (TournamentGroup group : groups) {
            winner.add(group.getRanking().get(0).player);
        }
        List<TournamentPlayer> second = new ArrayList<>();
        int r = winner.size() + 1;
        for (TournamentGroup group : groups) {
            second.add(group.getRanking().get(1).player);
            group.getRanking().get(1).player.ranking = r++;
        }
        //sort with qttr
        Collections.sort(winner, (o1, o2) -> Integer.compare(o1.getQttr(), o2.getQttr()) * -1);
        Collections.sort(second, (o1, o2) -> Integer.compare(o1.getQttr(), o2.getQttr()) * -1);
        Round round = field.getRound();
        int fieldSize = round.getSize();
        TournamentPlayer[] players = new TournamentPlayer[round.getSize() + 1];

//        winner.addAll(second);
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
            TournamentPlayer player1 = players[i];
            TournamentPlayer player2 = players[++i];
            game.setPlayer1(player1);
            game.setPlayer2(player2);
            games.add(game);
        }

        rest.addAll(second);

//        Sind Freilose notwendig, um die Feldgröße eines KO-Felds (8; 16; 32; ...) zu erreichen, so
//        werden diese Freilose in der 1. KO-Runde den auf die Positionen 1 bis G gesetzten Spielern
//        zugewiesen.


        //set the no of free places to the top seeded players
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


        //fill up with other if free places are avaible
        //need two runs here
        for (int run = 0; run < 2; run++) {

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

        printBracket(games);
        return games;
    }

    private void printBracket(List<TournamentSingleGame> games) {
        for (TournamentSingleGame game : games) {
            System.out.println("------------------");
            System.out.println(game.getPlayer1().getFullName());
            System.out.println(game.getPlayer2().getFullName());
            System.out.println("------------------");
            System.out.println();
        }
    }

    static public class PS implements Comparable<PS> {
        TournamentPlayer player;
        int win;
        int lose;
        int setsWon;
        int setsLost;

        //todo points

        //todo C 8.5.2 Punkt- und Satzgleichheit bei mehr als 2 Spielern
//        Bei Punkt- und Satzgleichheit von mehr als zwei Spielern einer Gruppe werden nur die Ergeb-
//        nisse dieser Spieler untereinander verglichen. Kommt man bei diesem Punkt- und Satzdiffe-
//        renzvergleichen Spielern immer  noch nicht zu einem Ergebnis, so entscheidet die größere Dif-
//        ferenz zwischen gewonnenen und verlorenen Bällen. Die Spiele gegen die anderen Spieler die-
//        ser Gruppe werden beim direkten Vergleich nicht berücksichtigt.

        @Override
        public int compareTo(PS o) {
            int w = Integer.compare(win, o.win);
            if (w != 0) {
                return w;
            }
            int l = Integer.compare(lose, o.lose);
            if (l != 0) {
                return l;
            }
            return Integer.compare(setsRatio(), o.setsRatio());
            //todo compare balls or make it generic
        }

        int setsRatio() {
            return setsWon - setsLost;
        }

        @Override
        public String toString() {
            return "PS{" +
                    "player=" + player.getFullName() +
                    ", win=" + win +
                    ", lose=" + lose +
                    ", setsWon=" + setsWon +
                    ", setsLost=" + setsLost +
                    '}';
        }
    }

    void calcRankingForGroup(TournamentGroup group) {
        List<PS> list = new ArrayList<>(4);
        for (TournamentPlayer player : group.getPlayers()) {
            PS ps = new PS();
            ps.player = player;
            for (TournamentSingleGame singleGame : group.getGames()) {
                if (singleGame.getPlayer1().equals(player)) {
                    for (GameSet gameSet : singleGame.getSets()) {
                        if (gameSet.getPoints1() < gameSet.getPoints2()) {
                            ps.setsLost++;
                        } else {
                            ps.setsWon++;
                        }
                    }
                    if (singleGame.getWinner() == 1) {
                        ps.win++;
                    } else {
                        ps.lose++;
                    }
                } else if (singleGame.getPlayer2().equals(player)) {
                    for (GameSet gameSet : singleGame.getSets()) {
                        if (gameSet.getPoints1() > gameSet.getPoints2()) {
                            ps.setsLost++;
                        } else {
                            ps.setsWon++;
                        }
                    }
                    if (singleGame.getWinner() == 2) {
                        ps.win++;
                    } else {
                        ps.lose++;
                    }
                }

            }
            list.add(ps);
        }
        Collections.sort(list);
        Collections.reverse(list);
        group.setRanking(list);
//        for (int i = 0; i < list.size(); i++) {
//            PS ps = list.get(i);
//            if (i < list.size()-1 && ps.win == list.get(i+1).win) {
//                System.out.println("same");
//            }
//        }
//        for (PS ps : list) {
//            System.out.println("ps = " + ps);
//        }
    }
}
