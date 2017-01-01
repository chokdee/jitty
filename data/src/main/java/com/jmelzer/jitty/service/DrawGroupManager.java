package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.PhaseRepository;
import com.jmelzer.jitty.dao.TournamentClassRepository;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.GroupPhaseDTO;
import com.jmelzer.jitty.model.dto.TournamentGroupDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * manage the draw of the group
 */
@Component
public class DrawGroupManager {
    static final Logger LOG = LoggerFactory.getLogger(DrawGroupManager.class);
    @Autowired
    TournamentService tournamentService;
    @Resource
    TournamentClassRepository tcRepository;
    @Resource
    PhaseRepository phaseRepository;
    @Resource
    SeedingManager seedingManager;

    //todo make it javascript
    @Transactional
    public GroupPhaseDTO calcOptimalGroupSize(GroupPhaseDTO phaseDTO) {
        GroupPhase phase = (GroupPhase) phaseRepository.findOne(phaseDTO.getId());
        int ppg = phaseDTO.getPlayerPerGroup() == null ? 4 : phaseDTO.getPlayerPerGroup();
        int optGroupSize = calcOptimalGroupSize(phase.getSystem().getTournamentClass().getPlayerCount(), ppg);
        phaseDTO.setGroupCount(optGroupSize);
        phaseDTO.setPlayerPerGroup(ppg);
        return phaseDTO;
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
    public void calcGroupGames(String clzName, Long tId, List<TournamentGroup> groups) {
        for (TournamentGroup group : groups) {
//            System.out.println(group);
            List<TournamentPlayer> list = new ArrayList<>(group.getPlayers());
            if (list.size() % 2 == 1) {
                // Number of player uneven ->  add the bye player.
                list.add(TournamentPlayer.BYE);
            }
            for (int i = 1; i < list.size(); i++) {


//                System.out.println("---- games round " + i + " ----");
                //first 1 against last

                group.addGames(createOneRound(i, list, clzName, tId));

                list.add(1, list.get(list.size() - 1));
                list.remove(list.size() - 1);

//                System.out.println("-----------------");
            }

            group.removeByePlayer();
        }

    }

    /**
     * Creates one round, i.e. a set of matches where each team plays once.
     *
     * @param round   Round number.
     * @param players List of players
     * @param tcName
     */
    private List<TournamentSingleGame> createOneRound(int round, List<TournamentPlayer> players, String tcName, Long tourId) {
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
                game.setTid(tourId);
                game.setPlayer2(t2);
                game.setTcName(tcName);
                tournamentService.save(game);
                games.add(game);
            }
//            System.out.println(t1.getFullName() + " --> " + t2.getFullName());
//            System.out.println("" + round + ":" +  ((round-1)*l1.size())+(tId+1) + " " + t1.getLastName() + " -->" + t2.getLastName());
        }
        return games;
    }


    @Transactional
    public GroupPhaseDTO automaticDraw(Long cid, GroupPhaseDTO dto) {
        List<TournamentGroupDTO> groups = createDTOGroups(dto.getGroupCount());
        dto.setGroups(groups);

        List<TournamentPlayerDTO> players = tournamentService.getPlayerforClass(cid);

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

        return tournamentService.updatePhase(cid, dto);
    }

    @Transactional
    public void startClass(Long id) {
        TournamentClass clz = tcRepository.findOne(id);
        GroupPhase groupPhase = (GroupPhase) clz.getActualPhase();
        calcGroupGames(clz.getName(), clz.getTournament().getId(), groupPhase.getGroups());
        clz.setStartTime(new Date());
        clz.setRunning(true);
        LOG.info("Class " + clz.getName() + " was started.");
        tcRepository.saveAndFlush(clz);
        tournamentService.addPossibleGroupGamesToQueue(groupPhase.getGroups());
    }

    @Transactional(readOnly = true)
    public List<TournamentPlayerDTO> getPossiblePlayerForGroups(Long cid) {
        TournamentClass tc = tcRepository.findOne(cid);
        List<TournamentPlayerDTO> allPlayer = tournamentService.getPlayerforClass(cid);

        List<TournamentGroup> groups = tc.getGroups();
        List<TournamentPlayerDTO> foundPlayer = new ArrayList<>();
        for (TournamentPlayerDTO playerDTO : allPlayer) {
            boolean found = false;
            for (TournamentGroup group : groups) {
                if (group.containsPlayer(playerDTO.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                foundPlayer.add(playerDTO);
            }
        }
        return foundPlayer;
    }
}
