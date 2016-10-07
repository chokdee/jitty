package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.*;
import com.jmelzer.jitty.exceptions.IntegrationViolation;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

import static com.jmelzer.jitty.service.CopyManager.copy;

/**
 * Created by J. Melzer on 01.06.2016.
 * manage the tournament
 */
@Component
public class TournamentService {
    static final Logger LOG = LoggerFactory.getLogger(TournamentService.class);


    List<TournamentSingleGame> gameQueue = new ArrayList<>();
    List<TournamentSingleGame> busyGames = new ArrayList<>();
    List<TournamentGroup> groups = new ArrayList<>();
    @Resource
    TournamentRepository repository;
    @Resource
    TournamentClassRepository tcRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    TournamentPlayerRepository playerRepository;
    @Resource
    TournamentSingleGameRepository tournamentSingleGameRepository;
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;

    //todo use spring
    private SeedingManager seedingManager = new SeedingManager();
    private KOField field;

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


        int groupCount = calcOptimalGroupSize(ps, 4);

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

//todo change to dtos here
//        seedingManager.setPlayerRandomAccordingToQTTR(groups, allPlayer);

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
    public void addPossibleGroupGamesToQueue(List<TournamentGroup> groups) {
        //todo add possible games for all running classes
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

    @Transactional
    public TournamentClassDTO calcOptimalGroupSize(TournamentClassDTO tournamentClassDTO) {
        TournamentClass tournamentClass = tcRepository.findOne(tournamentClassDTO.getId());
        int ppg = tournamentClassDTO.getPlayerPerGroup() == null ? 4 : tournamentClassDTO.getPlayerPerGroup();
        int optGroupSize = calcOptimalGroupSize(tournamentClass.getPlayerCount(), ppg);
        tournamentClassDTO.setGroupCount(optGroupSize);
        return tournamentClassDTO;
    }

    private int calcOptimalGroupSize(int playerSize, int groupSize) {
        //how many groups?
        int groupCount = playerSize / groupSize;
        int rest = (playerSize % groupSize);

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

    private List<TournamentGroupDTO> createDTOGroups(int groupCount) {
        List<TournamentGroupDTO> groups = new ArrayList<>(groupCount);
        //todo must be configured maybe 1, 2 better
        char name = 'A';
        for (int i = 0; i < groupCount; i++) {
            TournamentGroupDTO group = new TournamentGroupDTO();
            group.setName("" + name);
            groups.add(group);
            name++;
        }
        return groups;
    }

    /**
     * calculate the order of the possible games in the correct order
     */
    @Transactional
    public void calcGroupGames(List<TournamentGroup> groups) {
        for (TournamentGroup group : groups) {
            System.out.println(group);
//                List<TournamentPlayer> players = group.getPlayers();
            List<TournamentPlayer> list = new ArrayList<>(group.getPlayers());
            if (list.size() % 2 == 1) {
                // Number of player uneven ->  add the bye player.
                list.add(TournamentPlayer.BYE);
            }
            for (int i = 1; i < list.size(); i++) {


                System.out.println("---- games round " + i + " ----");
                //first 1 against last

                group.addGames(createOneRound(i, list));

                list.add(1, list.get(list.size() - 1));
                list.remove(list.size() - 1);

                System.out.println("-----------------");
            }

            group.removeByePlayer();
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
            if (!TournamentPlayer.BYE.equals(t1) && !TournamentPlayer.BYE.equals(t2)) {
                TournamentSingleGame game = new TournamentSingleGame();
                game.setPlayer1(t1);
                game.setPlayer2(t2);
                tournamentSingleGameRepository.save(game);
                games.add(game);
            }
            System.out.println(t1.getFullName() + " --> " + t2.getFullName());
//            System.out.println("" + round + ":" +  ((round-1)*l1.size())+(tId+1) + " " + t1.getLastName() + " -->" + t2.getLastName());
//            matches.addNew(round, ((round-1)*l1.size())+(tId+1), (String)t1.get("name"), (String)t2.get("name"));
        }
        return games;
    }

    public void addBusyGame(TournamentSingleGame game) {
        busyGames.add(game);
    }

    public List<TournamentSingleGameDTO> getBusyGames() {
        List<TournamentSingleGameDTO> list = new ArrayList<>(busyGames.size());
        for (TournamentSingleGame game : busyGames) {
            list.add(copy(game));
        }
        return list;
    }

    public void removeBusyGame(TournamentSingleGame game) {
        busyGames.remove(game);
    }

    public void markGroupWinner(List<TournamentGroup> groups) {
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
        return games;
    }

    @Transactional
    public List<TournamentSingleGameDTO> listQueue() {
        List<TournamentSingleGameDTO> list = new ArrayList<>(gameQueue.size());
        for (TournamentSingleGame game : gameQueue) {
            list.add(copy(game));
        }
        return list;
    }

    public TournamentSingleGame poll() {
        TournamentSingleGame game = gameQueue.get(0);
        gameQueue.remove(0);
        return game;
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
    public List<TournamentDTO> findAll() {
        List<Tournament> list = repository.findAll();
        List<TournamentDTO> ret = new ArrayList<>(list.size());
        for (Tournament tournament : list) {
            TournamentDTO dto = copy(tournament);
            ret.add(dto);
        }

        return ret;
    }

     @Transactional
    public TournamentDTO findOne(Long id) {
        Tournament tournament = repository.findOne(id);
        return copy(tournament);
    }

    @Transactional
    public Tournament create(TournamentDTO dto) {
        Tournament tournament = new Tournament();
        copy(dto, tournament);
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

    @Transactional
    public Tournament update(TournamentDTO dto) {
        Tournament t = repository.findOne(dto.getId());
        copy(dto, t);
        return repository.saveAndFlush(t);
    }

    @Transactional
    public TournamentClassDTO findOneClass(Long aLong) {
        TournamentClass tc = tcRepository.findOne(aLong);
        return copy(tc);
    }

    @Transactional
    public void updateClass(TournamentClassDTO dto) {
        TournamentClass tc = tcRepository.findOne(dto.getId());
        copy(dto, tc, playerRepository);
        tcRepository.saveAndFlush(tc);
    }

    @Transactional
    public void deleteClass(Long aLong) throws IntegrationViolation {
        TournamentClass tc = tcRepository.findOne(aLong);
        if (tc == null) {
            throw new EmptyResultDataAccessException(1);
        }
        if (tc.getGroups().size() > 0) {
            throw new IntegrationViolation("Es wurden bereits Gruppen angelegt, die Klasse kann nicht mehr gelöscht werden");
        }
        Tournament t = tc.getTournament();
        t.removeClass(tc);

        tcRepository.delete(aLong);
        repository.saveAndFlush(t);
    }

    //todo change to DTO object
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
                    } else if (singleGame.getWinner() == 2) {
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
                    } else if (singleGame.getWinner() == 1) {
                        ps.lose++;
                    }
                }

            }
            list.add(ps);
        }
        Collections.sort(list);
        Collections.reverse(list);
        int n = 0;
        for (PS ps : list) {
            TournamentPlayer own = ps.player;
            //find games and add entries
            for (int i = 0; i < list.size(); i++) {

                TournamentPlayer other = list.get(i).player;
                if (other.equals(own)) {
                    ps.detailResult.add("X");
                    continue;
                }
                boolean found = false;
                for (TournamentSingleGame game : group.getGames()) {
                    if ((game.getPlayer1().equals(own) || game.getPlayer2().equals(own)) &&
                            (game.getPlayer1().equals(other) || game.getPlayer2().equals(other))) {
                        ps.detailResult.add(game.getResultInShort(own));
                        found = true;
                        break;
                    }
                }
                Assert.isTrue(found);
            }
            n++;
            Assert.isTrue(ps.detailResult.size() > 1);
        }
        group.setRanking(list);
    }

    @Transactional
    public TournamentClass addTC(Long aLong, TournamentClass tournamentClass) {
        Tournament t = repository.findOne(aLong);
        t.addClass(tournamentClass);
        repository.saveAndFlush(t);
        return tournamentClass;
    }

    @Transactional
    public List<TournamentClassDTO> getAllClasses(TournamentPlayerDTO player, String userName) {
        Tournament t = userRepository.findByLoginName(userName).getLastUsedTournament();
        List<TournamentClassDTO> ret = new ArrayList<>();

        if (t == null) {
            return ret;
        }
        List<TournamentClass> classes = tcRepository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(t, player.getQttr(), player.getQttr());
        for (TournamentClass aClass : classes) {
            ret.add(copy(aClass));
        }
        return ret;
    }

    @Transactional
    public List<TournamentClassDTO> getNotRunning(String userName) {
        Tournament t = userRepository.findByLoginName(userName).getLastUsedTournament();
        List<TournamentClassDTO> ret = new ArrayList<>();
        //todo write correct find method
        List<TournamentClass> classes = tcRepository.findByTournamentAndRunning(t, false);
        for (TournamentClass aClass : classes) {
            ret.add(copy(aClass));
        }

        return ret;
    }

    public List<TournamentPlayerDTO> getPlayerforClass(Long id) {
        List<TournamentPlayer> players = playerRepository.findByClasses(Arrays.asList(tcRepository.findOne(id)));
        List<TournamentPlayerDTO> ret = new ArrayList<>();
        for (TournamentPlayer player : players) {
            TournamentPlayerDTO dto = new TournamentPlayerDTO();
            BeanUtils.copyProperties(player, dto, "classes");
            ret.add(dto);
        }
        return ret;
    }

    @Transactional
    public void createDummyPlayer(Long id) {
        TournamentClass tc = tcRepository.findOne(id);
        for (int i = 0; i < 20; i++) {
            TournamentPlayer player = new TournamentPlayer();
//            player.setId((long) i);
            player.setFirstName("Vorname#" + i);
            player.setLastName("Nachname#" + i);
            player.setTtr(randomIntFromInterval(2200, 2500));
            int qttr = randomIntFromInterval(player.getTtr() - 20, player.getTtr() + 20);
            player.setQttr(qttr);

            player.addClass(tc);
            playerRepository.save(player);
        }
    }

    int randomIntFromInterval(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    @Transactional
    public TournamentClassDTO automaticDraw(TournamentClassDTO dto) {
        List<TournamentGroupDTO> groups = createDTOGroups(dto.getGroupCount());
        dto.setGroups(groups);

        List<TournamentPlayerDTO> players = getPlayerforClass(dto.getId());

        //sort all player by qttr
        Collections.sort(players, (o1, o2) -> {
            if (o1.getQttr() < o2.getQttr()) {
                return 1;
            }
            if (o1.getQttr() > o2.getQttr()) {
                return -1;
            }
            return 0;
        });


        seedingManager.setPlayerRandomAccordingToQTTR(groups, players);

        return dto;
    }

    @Transactional
    public void startClass(Long id) {
        TournamentClass clz = tcRepository.findOne(id);
        calcGroupGames(clz.getGroups());
        clz.setStartTime(new Date());
        clz.setRunning(true);
        tcRepository.saveAndFlush(clz);
        addPossibleGroupGamesToQueue(clz.getGroups());
    }

    @Transactional
    public void startGame(Long id) {
        TournamentSingleGame foundGame = null;
        for (TournamentSingleGame game : gameQueue) {
            if (game.getId().equals(id)) {
                foundGame = game;
                gameQueue.remove(game);
                break;
            }
        }
        if (foundGame == null) {
            throw new IllegalArgumentException();
        }
        foundGame.setCalled(true);
        foundGame.setStartTime(new Date());
        addBusyGame(foundGame);
        tournamentSingleGameRepository.save(foundGame);
        LOG.debug("started game with id {}", id);

    }

    @Transactional
    public void saveGame(TournamentSingleGameDTO dto) {
        calcWinner(dto);
        TournamentSingleGame game = tournamentSingleGameRepository.findOne(dto.getId());
        copy(dto, game);
        game.setEndTime(new Date());
        game.setPlayed(true);
        tournamentSingleGameRepository.save(game);
    }

    TournamentSingleGameDTO calcWinner(TournamentSingleGameDTO dto) {
        int w = 0;
        for (GameSetDTO gameSetDTO : dto.getSets()) {
            w += gameSetDTO.getPoints1() > gameSetDTO.getPoints2() ? 1 : -1;
        }
        if (w < 0) {
            w = 2;
        } else if (w > 0) {
            w = 1;
        } else {
            throw new IllegalArgumentException("no winner could be calculated");
        }
        dto.setWinner(w);
        return dto;
    }

    public void finishGame(TournamentSingleGameDTO dto) {
        TournamentSingleGame foundGame = null;
        for (TournamentSingleGame busyGame : busyGames) {
            if (busyGame.getId().equals(dto.getId())) {
                foundGame = busyGame;
                break;
            }
        }
        if (foundGame != null) {
            busyGames.remove(foundGame);
            addPossibleGroupGamesToQueue(Collections.singletonList(foundGame.getGroup()));
        }
    }

    @Transactional
    public List<TournamentSingleGameDTO> getFinishedGames() {
        List<TournamentSingleGame> dblist = tournamentSingleGameRepository.findByPlayedOrderByEndTimeDesc(true);

        List<TournamentSingleGameDTO> list = new ArrayList<>(busyGames.size());
        for (TournamentSingleGame game : dblist) {
            list.add(copy(game));
        }
        for (TournamentSingleGameDTO singleGameDTO : list) {
            if (singleGameDTO.getWinner() == 1) {
                singleGameDTO.setWinnerName(singleGameDTO.getPlayer1().getFullName());
            } else {
                singleGameDTO.setWinnerName(singleGameDTO.getPlayer2().getFullName());

            }
        }
        return list;
    }

    @PostConstruct
    @Transactional
    public void postConstruct() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<Tournament> tournaments = repository.findAll();
                for (Tournament tournament : tournaments) {
                    List<TournamentClass> tcs = tcRepository.findByTournamentAndRunning(tournament, true);
                    for (TournamentClass tc : tcs) {
                        //todo remove later
//                        calcGroupGames(tc.getGroups());
//                        if (tc.getGroups().size() > 1) {
//                            int n = 1;
//                            int s = 1;
//                            for (TournamentGroup group : tc.getGroups()) {
//                                for (TournamentSingleGame game : group.getGames()) {
//                                    createRandomResult(game);
//                                    System.out.println("INSERT INTO TOURNAMENT_SINGLE_GAME (ID, CALLED, PLAYED, START_TIME, " +
//                                            "WINNER, GROUP_ID, PLAYER1_ID, PLAYER2_ID) VALUES(" +
//                                            n++ + ",1, 1, NOW(), " + game.getWinner() +  ", " + group.getId() + ", " +
//                                            game.getPlayer1().getId() + ", " + game.getPlayer2().getId() + ");");
//
//                                    for (GameSet gameSet : game.getSets()) {
//                                        System.out.println("INSERT INTO GAME_SET (ID, POINTS1, POINTS2) VALUES (" + s + "," + gameSet.getPoints1() + "," + gameSet.getPoints2() + ");");
//                                        System.out.println("INSERT INTO TOURNAMENT_SINGLE_GAME_SET ( TOURNAMENT_SINGLE_GAME_ID, SETS_ID) VALUES (" + + game.getId() + "," + s + ");");
//                                        s++;
//                                    }
//                                }
//                            }
//                        }
//                        tcRepository.saveAndFlush(tc);
                        addPossibleGroupGamesToQueue(tc.getGroups());

                    }
                }
            }
        });
        for (TournamentSingleGame game : gameQueue) {
            System.out.println("game = " + game);
        }

    }

    private void createRandomResult(TournamentSingleGame game) {
        int playedSets = randomIntFromInterval(3, 5);
        int winner = randomIntFromInterval(1, 2);
        int loser = 0;
        if (winner == 1) {
            loser = 2;
        } else {
            loser = 1;
        }
        game.setWinner(winner);
        for (int i = 1; i <= playedSets; i++) {
            if (i < 3) {
                addSetFor(winner, game);
            } else {
                if (playedSets == i) {
                    addSetFor(winner, game);
                } else {
                    addSetFor(loser, game);
                }
            }
        }
    }


    private void addSetFor(int winner, TournamentSingleGame game) {
        if (winner == 1) {
            game.addSet(new GameSet(11, randomIntFromInterval(0, 9)));
        } else {
            game.addSet(new GameSet(randomIntFromInterval(0, 9), 11));
        }

    }

    @Transactional
    public List<TournamentClassDTO> getStartedClasses(String actualUsername) {
        Tournament t = userRepository.findByLoginName(actualUsername).getLastUsedTournament();
        List<TournamentClass> classes = tcRepository.findByTournamentAndRunning(t, true);
        List<TournamentClassDTO> ret = new ArrayList<>();
        for (TournamentClass aClass : classes) {
            ret.add(copy(aClass));
        }

        return ret;
    }

    @Transactional
    public List<GroupResultDTO> getGroupResults(Long id) {
        TournamentClass tournamentClass = tcRepository.findOne(id);

        markGroupWinner(tournamentClass.getGroups());

        List<GroupResultDTO> results = new ArrayList<>();
        for (TournamentGroup group : tournamentClass.getGroups()) {
            GroupResultDTO groupResultDTO = new GroupResultDTO();
            groupResultDTO.setGroupName(group.getName());
            results.add(groupResultDTO);
            int pos = 1;
            for (PS ps : group.getRanking()) {
                GroupResultEntryDTO entry = new GroupResultEntryDTO();
                entry.setPos(pos++);
                entry.setClub(ps.player.getClub().getName());
                entry.setPlayerName(ps.player.getFullName());
                entry.setGameStat(ps.win + ":" + ps.lose);
                entry.setSetStat(ps.setsWon + ":" + ps.setsLost);
                entry.setDetailResult(ps.detailResult);
                groupResultDTO.getEntries().add(entry);
            }
        }

        return results;
    }

    public TournamentSingleGameDTO getGame(Long id) {
        return copy(tournamentSingleGameRepository.findOne(id));
    }

    static public class PS implements Comparable<PS> {
        TournamentPlayer player;
        int win;
        int lose;
        int setsWon;
        int setsLost;
        List<String> detailResult = new ArrayList<>();

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
}
