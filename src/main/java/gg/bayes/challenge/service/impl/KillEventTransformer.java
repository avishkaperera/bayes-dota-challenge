package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.service.EventTransformer;
import gg.bayes.challenge.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
@Slf4j
public class KillEventTransformer implements EventTransformer {
    private static final int TIME_INDEX = 0;
    private static final String TIME_REPLACE_PATTERN = "]";
    private static final int ACTOR_INDEX = 5;
    private static final String ACTOR_REPLACE_PATTERN = "npc_dota_hero_";
    private static final int TARGET_INDEX = 1;
    private static final String TARGET_REPLACE_PATTERN = "npc_dota_hero_";

    @Override
    public CombatLogEntryEntity transformEvent(String logLine, LocalTime gameStartTime, MatchEntity matchEntity) {
        log.info("Transforming kill event to CombatLogEntryEntity");
        CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
        List<String> words = Arrays.asList(logLine.split(" "));

        String eventTime = words.get(TIME_INDEX).replace(TIME_REPLACE_PATTERN, "");
        long purchasedTime = MILLIS.between(gameStartTime, CommonUtil.extractTimeInMilliseconds(eventTime));
        combatLogEntryEntity.setTimestamp(purchasedTime);

        combatLogEntryEntity.setType(CombatLogEntryEntity.Type.HERO_KILLED);

        String actor = words.get(ACTOR_INDEX).replace(ACTOR_REPLACE_PATTERN, "");
        combatLogEntryEntity.setActor(actor);

        String target = words.get(TARGET_INDEX).replace(TARGET_REPLACE_PATTERN, "");
        combatLogEntryEntity.setTarget(target);

        combatLogEntryEntity.setMatch(matchEntity);

        return combatLogEntryEntity;
    }
}
