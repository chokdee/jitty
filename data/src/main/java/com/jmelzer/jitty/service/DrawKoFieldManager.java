package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentClassRepository;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.KOFieldDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.utl.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.jmelzer.jitty.service.CopyManager.copy;

/**
 * Created by J. Melzer on 01.06.2016.
 * manage the draw of the ko field
 */
@Component
public class DrawKoFieldManager {
    static final Logger LOG = LoggerFactory.getLogger(DrawKoFieldManager.class);
    @Autowired
    TournamentService tournamentService;
    @Resource
    TournamentClassRepository tcRepository;
    @Resource
    SeedingManager seedingManager;


    @Transactional(readOnly = true)
    public int calcKOSizeInInt(long cId) {
        TournamentClass tc = tcRepository.findOne(cId);
        return calcKOSize(tc).getValue();

    }

    public RoundType calcKOSize(TournamentClass tournamentClass) {
        int player = tournamentClass.getGroups().size() * 2;
        if (player <= 8) {
            return RoundType.QUARTER;
        }
        if (player <= 16) {
            return RoundType.R16;
        }
        if (player <= 32) {
            return RoundType.R32;
        }
        if (player <= 64) {
            return RoundType.R64;
        }
        if (player <= 128) {
            return RoundType.R128;
        }
        throw new IllegalArgumentException("could not calc size for " + player);
    }

    private int calcRounds(RoundType roundType) {
        switch (roundType) {
            case QUARTER:
                return 4;
            case R16:
                return 5;
            case R32:
                return 6;
            case R64:
                return 7;
            case R128:
                return 8;
        }
        throw new RuntimeException("not yet implemented");
    }

    public KOField createKOField(RoundType roundType) {
        KOField field = new KOField();
        field.setRound(new Round(roundType));
        createSubRounds(field.getRound(), calcRounds(roundType) - 2, roundType.getValue() / 2);
        int nrOdRounds = calcRounds(roundType);
        field.setNoOfRounds(nrOdRounds);
        return field;
    }

    private void createSubRounds(Round round, int i, int size) {
        Round lastRound = round;
        int n = size;
        lastRound.setGameSize(n);
        for (int j = 0; j < i; j++) {
            n = n / 2;
            lastRound.setNextRound(new Round(n));
            lastRound = lastRound.getNextRound();
            if (lastRound.getRoundType() == RoundType.FINAL) {
                break;
            }
        }
    }

    public List<TournamentSingleGame> assignPlayerToKoField(KOField field, List<TournamentGroup> groups) {
        return seedingManager.assignPlayerToKoField(field, groups);
    }

    /**
     * draw a ko field but still not started
     *
     * @param assignPlayer automatic assignment of the players, trigger creating empty games
     * @return new field
     */
    @Transactional
    public KOFieldDTO drawKO(Long tcId, boolean assignPlayer) {
        TournamentClass tc = tcRepository.findOne(tcId);
        if (tc.getPhase() != null && tc.getPhase() == 2) {
            return copy(tc.getKoField(), tc);
        }
        for (TournamentGroup tournamentGroup : tc.getGroups()) {
            tournamentService.calcRankingForGroup(tournamentGroup);
        }
        RoundType roundType = calcKOSize(tc);
        tc.setKoField(createKOField(roundType));
        tc = tcRepository.saveAndFlush(tc);
        if (assignPlayer) {
            List<TournamentSingleGame> games = assignPlayerToKoField(tc.getKoField(), tc.getGroups());
            for (TournamentSingleGame game : games) {
                game.setTcName(tc.getName());
                game.setRound(tc.getKoField().getRound());
                tournamentService.save(game);
            }
            Round nextRound = tc.getKoField().getRound().getNextRound();
            while (nextRound != null) {
                List<TournamentSingleGame> nextGames = createEmptyGamesForRound(nextRound);
                for (TournamentSingleGame game : nextGames) {
                    game.setTcName(tc.getName());
                    game.setRound(nextRound);
                    tournamentService.save(game);
                }
                nextRound = nextRound.getNextRound();
                games = nextGames;
            }
//            tc.setPhase(2);
        }
        tc = tcRepository.saveAndFlush(tc);
        return copy(tc.getKoField(), tc);
    }

    private List<TournamentSingleGame> createEmptyGamesForRound(Round nextRound) {
        List<TournamentSingleGame> gamesLastRound = nextRound.getPrevRound().getGames();
        List<TournamentSingleGame> nextGames = new ArrayList<>(gamesLastRound.size() / 2);

        for (int i = 0; i < gamesLastRound.size(); i++) {
            TournamentSingleGame nextGame = new TournamentSingleGame();
            nextGame.setGameName(gamesLastRound.get(i).getGameName() + " - " + gamesLastRound.get(i+1).getGameName());
            gamesLastRound.get(i).setNextGame(nextGame);
            gamesLastRound.get(i+1).setNextGame(nextGame);

            nextGames.add(nextGame);
            nextRound.addGame(nextGame);
            i++;
        }
        return nextGames;
    }

    @Transactional
    public void startKOField(Long tcId) {
        TournamentClass tc = tcRepository.findOne(tcId);
        if (tc.getPhase() != null && tc.getPhase() == 2) {
            return;
        }
        tc.setPhase(2);
        tournamentService.addPossibleKoGamesToQueue(tc);
        tcRepository.saveAndFlush(tc);

    }

    @Transactional(readOnly = true)
    public List<TournamentPlayerDTO> getPossiblePlayerForKOField(Long cid) {
        TournamentClass tc = tcRepository.findOne(cid);
        List<TournamentGroup> groups = tc.getGroups();
        List<TournamentPlayerDTO> winner = new ArrayList<>();
        for (TournamentGroup group : groups) {
            tournamentService.calcRankingForGroup(group);
            //todo config winner count
            winner.add(copy(group.getRanking().get(0).player));
            winner.add(copy(group.getRanking().get(1).player));
        }
        filterAlreadyAssignKOPlayer(tc, winner);
        return winner;
    }

    private void filterAlreadyAssignKOPlayer(TournamentClass tc, List<TournamentPlayerDTO> winner) {
        if (tc.getKoField() == null) {
            return;
        }

        Round round = tc.getKoField().getRound();
        for (TournamentSingleGame game : round.getGames()) {
            ListUtil.removeIfContains(winner, game.getPlayer1());
            ListUtil.removeIfContains(winner, game.getPlayer2());
        }
    }

    @Transactional
    public void resetKO(Long tcId) {
        TournamentClass tc = tcRepository.findOne(tcId);
        RoundType roundType = calcKOSize(tc);
        KOField field = createKOField(roundType);
        tc.setKoField(field);
        tc.setPhase(1);
        tcRepository.saveAndFlush(tc);
    }
}
