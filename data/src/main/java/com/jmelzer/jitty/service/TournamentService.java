package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.*;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

import static com.jmelzer.jitty.service.CopyManager.copy;
import static com.jmelzer.jitty.service.CopyManager.copyForBracket;

/**
 * Created by J. Melzer on 01.06.2016.
 * manage the tournament
 */
@Component
public class TournamentService {
    static final Logger LOG = LoggerFactory.getLogger(TournamentService.class);
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;
    //todo make persistent and get it from db
    List<TournamentSingleGame> gameQueue = new ArrayList<>();
    List<TournamentSingleGame> busyGames = new ArrayList<>();
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
    @Resource
    TableManager tableManager;


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
            if ((game.getPlayer1() != null && game.getPlayer1().equals(player)) ||
                    (game.getPlayer2() != null && game.getPlayer2().equals(player))) {
                return true;
            }
        }
        return false;
    }


    public void addBusyGame(TournamentSingleGame game) {
        if (game.getTableNo() == null) {
            throw new IllegalArgumentException("game must have a table");
        }
        busyGames.add(game);
    }

    public List<TournamentSingleGameDTO> getBusyGames() {
        List<TournamentSingleGameDTO> list = new ArrayList<>(busyGames.size());
        for (TournamentSingleGame game : busyGames) {
            list.add(copy(game, false));
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
            List<PlayerStatistic> pss = group.getRanking();
            int i = 1;
            for (PlayerStatistic ps : pss) {
                System.out.println("" + i + "\t" + ps.player.getFullName() + "\t" + ps.win + ":" + ps.lose + "\t\t" + ps.setsWon + ":" + ps.setsLost);
                i++;
            }
            System.out.println("----------------------------------  ");
        }
    }


    @Transactional
    public List<TournamentSingleGameDTO> listQueue() {
        List<TournamentSingleGameDTO> list = new ArrayList<>(gameQueue.size());
        for (TournamentSingleGame game : gameQueue) {
            list.add(copy(game, false));
        }
        return list;
    }

    public TournamentSingleGame poll() {
        if (gameQueue.size() == 0) {
            return null;
        }
        TournamentSingleGame game = gameQueue.get(0);
        gameQueue.remove(0);
        return game;
    }

    public void addPossibleKoGamesToQueue(TournamentClass tournamentClass) {
        if (tournamentClass.getKoField() != null) {

            List<TournamentSingleGame> games = tournamentClass.getKoField().getRound().getGames();
            addGamesToQueueInternally(games);
            Round r = tournamentClass.getKoField().getRound();
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
            if (game.isEmpty()) {
                continue;
            }

            TournamentPlayer player1 = game.getPlayer1();
            TournamentPlayer player2 = game.getPlayer2();
            if (!playerInQueue(player1, gameQueue) && !playerInQueue(player2, gameQueue) &&
                    !playerInQueue(player1, busyGames) && !playerInQueue(player2, busyGames) &&
                    !game.isFinishedOrCalled()) {
                System.out.println("game = " + game.getId() + " - " + game);
                gameQueue.add(game);
            }
        }
    }

    private void moveWinnerToNextRound(TournamentSingleGame game) {
        TournamentPlayer player = game.getWinningPlayer();
        TournamentSingleGame nextGame = game.getNextGame();
        if (nextGame == null) {
            return;
        }
        if (nextGame.getPlayer1() == null) {
            nextGame.setPlayer1(player);
        } else {
            nextGame.setPlayer2(player);
        }
        playerRepository.saveAndFlush(player);
        tournamentSingleGameRepository.saveAndFlush(nextGame);
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
        tableManager.setTableCount(tournament.getTableCount());
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
    public TournamentClassDTO updateClass(TournamentClassDTO dto) {
        TournamentClass tc = tcRepository.findOne(dto.getId());
        int groupSize = dto.getGroups().size();
        tc.getGroups().clear();//fetch lazy
//        tc.clearGroups();
        tc = tcRepository.saveAndFlush(tc);
        if (0 != tc.getGroups().size()) {
            System.err.println(tc);
        }
        copy(dto, tc, playerRepository);
        if (groupSize != tc.getGroups().size()) {
            System.err.println(tc);
        }
        tc = tcRepository.saveAndFlush(tc);
        if (groupSize != tc.getGroups().size()) {
            System.err.println(tc);
        }
        return copy(tc);
    }

    @Transactional
    public void deleteClass(Long aLong) throws IntegrityViolation {
        TournamentClass tc = tcRepository.findOne(aLong);
        if (tc == null) {
            throw new EmptyResultDataAccessException(1);
        }
        if (tc.getGroups().size() > 0) {
            throw new IntegrityViolation("Es wurden bereits Gruppen angelegt, die Klasse kann nicht mehr gelöscht werden");
        }
        Tournament t = tc.getTournament();
        t.removeClass(tc);

        tcRepository.delete(aLong);
        repository.saveAndFlush(t);
    }

    //todo change to DTO object
    void calcRankingForGroup(TournamentGroup group) {
        List<PlayerStatistic> list = new ArrayList<>(4);
        for (TournamentPlayer player : group.getPlayers()) {
            PlayerStatistic ps = new PlayerStatistic();
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
        for (PlayerStatistic ps : list) {
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
        tcRepository.saveAndFlush(tournamentClass);
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
        int qttr = player.getQttr();
        if (qttr == 0) {
            qttr = 1;
        }
        List<TournamentClass> classes = tcRepository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(t, qttr, qttr);
        for (TournamentClass aClass : classes) {
            ret.add(copy(aClass));
        }
        return ret;
    }

    @Transactional
    public List<TournamentClassDTO> getNotRunningOrStartPhase2(String userName) {
        Tournament t = userRepository.findByLoginName(userName).getLastUsedTournament();
        List<TournamentClassDTO> ret = new ArrayList<>();
        List<TournamentClass> classes = t.getClasses();
        for (TournamentClass aClass : classes) {
            if (!aClass.getRunning() || (aClass.getRunning() && isPhase1FinishedAndPhase2NotStarted(aClass))) {
                ret.add(copy(aClass));
            }
        }

        return ret;
    }

    public List<TournamentPlayerDTO> getPlayerforClass(Long id) {
        List<TournamentPlayer> players = playerRepository.findByClasses(Collections.singletonList(tcRepository.findOne(id)));
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
    public TournamentSingleGameDTO startGame(Long id) throws IntegrityViolation {
        //todo in a method an refresh the object
        TournamentSingleGame foundGame = null;
        for (TournamentSingleGame game : gameQueue) {
            if (game.getId().equals(id)) {
                foundGame = game;
                break;
            }
        }
        if (foundGame == null) {
            throw new IllegalArgumentException();
        }
        //refresh
        foundGame = tournamentSingleGameRepository.findOne(foundGame.getId());
        int no = tableManager.pollFreeTableNo(foundGame);
        if (no == -1) {
            throw new IntegrityViolation("Es gibt keinen freien Tisch mehr");
        }

        foundGame.setCalled(true);
        foundGame.setStartTime(new Date());
        addBusyGame(foundGame);
        gameQueue.remove(foundGame);
        tournamentSingleGameRepository.save(foundGame);
        LOG.debug("started game with id {}", id);
        return copy(foundGame, false);
    }

    @Transactional
    public void moveGameBackToPossiblegames(Long id) {
        TournamentSingleGame game = tournamentSingleGameRepository.findOne(id);
        if (game == null) {
            throw new IllegalArgumentException();
        }
        gameQueue.add(game);
        busyGames.remove(game);
        game.setCalled(false);
        game.setStartTime(null);
        tournamentSingleGameRepository.save(game);
        LOG.debug("move game back with id {}", id);

    }

    TournamentSingleGame saveGame(TournamentSingleGameDTO dto) {
        calcWinner(dto);
        TournamentSingleGame game = tournamentSingleGameRepository.findOne(dto.getId());
        copy(dto, game);
        game.setEndTime(new Date());
        game.setPlayed(true);
        TournamentPlayer p1 = game.getPlayer1();
        TournamentPlayer p2 = game.getPlayer2();
        p1.setLastGameAt(new Date());
        p2.setLastGameAt(new Date());
        save(game);
        return game;
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

    /**
     * find game from the busy game list and add new possible games to the queue
     */
    private void finishGame(TournamentSingleGame game) {
        LOG.info("finish game #" + game.getId());
        busyGames.remove(game);
        if (game.getGroup() != null) {
            addPossibleGroupGamesToQueue(Collections.singletonList(game.getGroup()));
        } else {
            Round gr = game.getRound();
            TournamentClass tc = gr.findKOField().getTournamentClass();
            moveWinnerToNextRound(game);
            addPossibleKoGamesToQueue(tc);
        }
        tableManager.pushFreeTable(game);
    }

    @Transactional
    public List<TournamentSingleGameDTO> getFinishedGames() {
        List<TournamentSingleGame> dblist = tournamentSingleGameRepository.findByPlayedOrderByEndTimeDesc(true);

        List<TournamentSingleGameDTO> list = new ArrayList<>(busyGames.size());
        for (TournamentSingleGame game : dblist) {
            list.add(copy(game, false));
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
                List<Tournament> tournaments = repository.findByRunning(true);
                //todo what about multiple tournaments and tablemanager?
                for (Tournament tournament : tournaments) {
                    tableManager.setTableCount(tournament.getTableCount());
                    List<TournamentClass> tcs = tcRepository.findByTournamentAndRunning(tournament, true);
                    for (TournamentClass tc : tcs) {
                        addPossibleGroupGamesToQueue(tc.getGroups());
                    }
                }
            }
        });

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
    public List<GroupResultDTO> getGroupResults(Long classId) {
        TournamentClass tournamentClass = tcRepository.findOne(classId);

        markGroupWinner(tournamentClass.getGroups());

        List<GroupResultDTO> results = new ArrayList<>();
        for (TournamentGroup group : tournamentClass.getGroups()) {
            GroupResultDTO groupResultDTO = new GroupResultDTO();
            groupResultDTO.setGroupName(group.getName());
            results.add(groupResultDTO);
            int pos = 1;
            for (PlayerStatistic ps : group.getRanking()) {
                GroupResultEntryDTO entry = new GroupResultEntryDTO();
                entry.setPos(pos++);
                entry.setClub(ps.player.getClub() != null ? ps.player.getClub().getName() : "");
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
        return copy(tournamentSingleGameRepository.findOne(id), false);
    }

    @Transactional
    public long saveAndFinishGame(TournamentSingleGameDTO dto) {
        TournamentSingleGame game = saveGame(dto);
        finishGame(game);
        return game.getId();
    }

    public boolean isPhase1FinishedAndPhase2NotStarted(TournamentClass tc) {
        for (TournamentGroup group : tc.getGroups()) {
            List<TournamentSingleGame> games = group.getGames();
            for (TournamentSingleGame game : games) {
                if (!game.isPlayed()) {
                    return false;
                }
            }
        }
        return !(tc.getPhase() != null && tc.getPhase() > 1);
    }

    @Transactional(readOnly = true)
    public boolean areAllGroupsFinished(Long id) {
        TournamentClass clz = tcRepository.findOne(id);
        return isPhase1FinishedAndPhase2NotStarted(clz);
    }

    @Transactional(readOnly = true)
    public Long[] anyPhaseFinished(String userName) {
        Tournament t = userRepository.findByLoginName(userName).getLastUsedTournament();
        List<TournamentClass> clzs = tcRepository.findByTournamentAndRunning(t, true);
        List<Long> ids = new ArrayList<>();
        for (TournamentClass clz : clzs) {
            boolean b = isPhase1FinishedAndPhase2NotStarted(clz);
            if (b) {
                ids.add(clz.getId());
            }
        }
        return ids.toArray(new Long[ids.size()]);
    }

    @Transactional
    public void save(TournamentSingleGame game) {
        tournamentSingleGameRepository.save(game);
    }

    @Transactional(readOnly = true)
    public KOFieldDTO getKOForClz(Long tcId) {
        TournamentClass tc = tcRepository.findOne(tcId);
        return copyForBracket(tc.getKoField());
    }

    public void saveTableCount(String actualUsername, int tablecount) throws IntegrityViolation {
        Tournament t = userRepository.findByLoginName(actualUsername).getLastUsedTournament();
        if (tablecount < t.getTableCount()) {
            throw new IntegrityViolation("Die Tischanzahl kann nicht reduziert werden, da bereits an den Tischen gespielt wird.");
        }
        t.setTableCount(tablecount);

    }

    @Transactional
    public void startPossibleGames() throws IntegrityViolation {
        int n = tableManager.getFreeTableCount();
        for (int i = 0; i < n; i++) {
            if (gameQueue.isEmpty()) {
                break;
            }

            TournamentSingleGame game = gameQueue.get(0);
            startGame(game.getId());
        }
    }
}
