package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.common.Constants;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.service.CombatLogService;
import gg.bayes.challenge.service.EventTransformer;
import gg.bayes.challenge.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CombatLogServiceImpl implements CombatLogService {
    private static final int GAME_START_TIME_INDEX = 1;

    @Autowired
    private Map<String, EventTransformer> eventTransformers;

    @Override
    public HashSet<CombatLogEntryEntity> extractEvents(String combatLog, MatchEntity matchEntity) {
        log.info("Starting to extract events from combat log");
        List<String> logLines = Arrays.asList(combatLog.split("\\["));
        return processLogLines(logLines, CommonUtil.extractTimeInMilliseconds(logLines.get(GAME_START_TIME_INDEX)), matchEntity);
    }

    private HashSet<CombatLogEntryEntity> processLogLines(List<String> logLines, LocalTime gameStartTime, MatchEntity matchEntity) {
        HashSet<CombatLogEntryEntity> combatLogEntryEntities = new HashSet<>();
        logLines.forEach(line -> {
            if (line.contains(Constants.EventMatchers.PURCHASE_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.PURCHASE_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            } else if (line.contains(Constants.EventMatchers.KILL_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.KILL_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            } else if (line.contains(Constants.EventMatchers.SPELL_CAST_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.SPELL_CAST_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            } else if (line.contains(Constants.EventMatchers.DAMAGE_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.DAMAGE_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            }
        });
        return combatLogEntryEntities;
    }
}
