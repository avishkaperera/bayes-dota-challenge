package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.service.EventTransformer;
import gg.bayes.challenge.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
    public CombatLogEntryEntity transformEvent(String logLine, MatchEntity matchEntity) {
        CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
        List<String> words = Arrays.asList(logLine.split(" "));
        String actorFullString = words.size() >= 5 ? words.get(ACTOR_INDEX) : "";
        String targetFullString = words.get(TARGET_INDEX);

        if (actorFullString.contains(ACTOR_REPLACE_PATTERN) && targetFullString.contains(TARGET_REPLACE_PATTERN)) {
            String eventTime = words.get(TIME_INDEX).replace(TIME_REPLACE_PATTERN, "");
            combatLogEntryEntity.setTimestamp(CommonUtil.extractTimeDiffInMilliseconds(eventTime));

            combatLogEntryEntity.setType(CombatLogEntryEntity.Type.HERO_KILLED);

            String actor = actorFullString.replace(ACTOR_REPLACE_PATTERN, "");
            combatLogEntryEntity.setActor(actor);

            String target = targetFullString.replace(TARGET_REPLACE_PATTERN, "");
            combatLogEntryEntity.setTarget(target);

            combatLogEntryEntity.setMatch(matchEntity);

            return combatLogEntryEntity;
        }
        // Returns null if it is not two heroes killing each other
        return null;
    }
}
