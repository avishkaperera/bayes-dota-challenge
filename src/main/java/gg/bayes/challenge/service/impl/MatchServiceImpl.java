package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.service.CombatLogService;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final CombatLogService combatLogService;
    private final MatchRepository matchRepository;

    @Override
    public Long processCombatLog(String combatLog) {
        MatchEntity matchEntity = new MatchEntity();
        HashSet<CombatLogEntryEntity> combatLogEntryEntities = combatLogService.extractEvents(combatLog, matchEntity);
        matchEntity.setCombatLogEntries(combatLogEntryEntities);
        log.info("Persisting events of combat log");
        MatchEntity savedMatchEntity = matchRepository.save(matchEntity);
        return savedMatchEntity.getId();
    }
}
