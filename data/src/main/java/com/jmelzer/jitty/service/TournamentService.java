package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by J. Melzer on 01.06.2016.
 * manage the tournament
 */
@Component
public class TournamentService {
    Queue<TournamentSingleGame> gameQueue = new LinkedList<>();
    List<TournamentSingleGame> busyGames = new ArrayList<>();
    List<TournamentGroup> groups = new ArrayList<>();
    //todo use spring
    private SeedingManager seedingManager = new SeedingManager();
    private KOField field;

    @Resource
    TournamentRepository repository;

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
        tournamentClass.setGroups(groups);

        List<TournamentPlayer> allPlayer = new ArrayList<>(tournamentClass.getPlayers());


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


        seedingManager.setPlayerRandomAccordingToQTTR(groups, allPlayer);

    }

    public List<TournamentGroup> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    public int getQueueSize() {
        return gameQueue.size();
    }

    /**
     * iterate thrw all groups and add not played games. but take care of the player isn't already in the list
     */
    public void addPossibleGroupGamesToQueue() {
        for (TournamentGroup group : groups) {
            List<TournamentSingleGame> games = group.getGames();
            addGamesToQueueInternally(games);
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
        field = new KOField();
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
        //todo persist it
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
        List<TournamentSingleGame> games = seedingManager.assignPlayerToKoField(field, groups);
//        qu
        return games;
    }

    public TournamentSingleGame poll() {
        return gameQueue.poll();
    }

    public void addPossibleKoGamesToQueue() {
        if (field != null) {

            List<TournamentSingleGame> games = field.getRound().getGames();
            addGamesToQueueInternally(games);
            Round r = field.getRound();
            while (r != null) {
                if (r.getNextRound() != null) {
                    r = r.getNextRound();
                    addGamesToQueueInternally(r.getGames());
                } else {
                    break;
                }
            }
        }
    }

    private void addGamesToQueueInternally(List<TournamentSingleGame> games) {
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

    @Transactional
    public void enterResult(TournamentSingleGame game) {
        if (field != null) {
            moveWinnerToNextRound(game);
        }
        removeBusyGame(game);
    }

    private void moveWinnerToNextRound(TournamentSingleGame game) {
        TournamentPlayer player = game.getWinningPlayer();
    }

    @Transactional
    public List<Tournament> findAll() {
        List<Tournament> list = repository.findAll();
        for (Tournament tournament : list) {
            loadAssocs(tournament);
        }

        return list;
    }

    private void loadAssocs(Tournament tournament) {
        for (TournamentClass tournamentClass : tournament.getClasses()) {
            for (TournamentGroup group : tournamentClass.getGroups()) {
                group.getId();
            }
        }
    }

    @Transactional
    public Tournament findOne(Long id) {
        Tournament tournament = repository.findOne(id);
        loadAssocs(tournament);
        return tournament;
    }
    @Transactional
    public Tournament create(Tournament tournament) {
        //todo where to config it?
        //todo add double and women and child classes
        tournament.addClass(createTC("A-Klasse", 0, 3000));
        tournament.addClass(createTC("B-Klasse", 0, 1800));
        tournament.addClass(createTC("C-Klasse", 0, 1600));
        tournament.addClass(createTC("D-Klasse", 0, 1400));
        tournament.addClass(createTC("E-Klasse", 0, 1200));
        return repository.saveAndFlush(tournament);
    }

    private TournamentClass createTC(String name, int startTTR, int endTTR) {
        TournamentClass tc = new TournamentClass(name);
        tc.setStartTTR(startTTR);
        tc.setEndTTR(endTTR);
        return tc;
    }

    public Tournament update(Tournament tournament) {
        return repository.saveAndFlush(tournament);
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
