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
public class PurchaseEventTransformer implements EventTransformer {
    private static final int TIME_INDEX = 0;
    private static final String TIME_REPLACE_PATTERN_1 = "[";
    private static final String TIME_REPLACE_PATTERN_2 = "]";
    private static final int ACTOR_INDEX = 1;
    private static final String ACTOR_REPLACE_PATTERN = "npc_dota_hero_";
    private static final int ITEM_INDEX = 4;
    private static final String ITEM_REPLACE_PATTERN = "item_";

    @Override
    public CombatLogEntryEntity transformEvent(String logLine, MatchEntity matchEntity) {
        CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
        List<String> words = Arrays.asList(logLine.split(" "));

        if (words.size() >= 5) {
            String eventTime = words.get(TIME_INDEX)
                    .replace(TIME_REPLACE_PATTERN_1, "").replace(TIME_REPLACE_PATTERN_2, "");
            combatLogEntryEntity.setTimestamp(CommonUtil.extractTimeDiffInMilliseconds(eventTime));

            combatLogEntryEntity.setType(CombatLogEntryEntity.Type.ITEM_PURCHASED);

            String actor = words.get(ACTOR_INDEX).replace(ACTOR_REPLACE_PATTERN, "");
            combatLogEntryEntity.setActor(actor);

            String item = words.get(ITEM_INDEX).replace(ITEM_REPLACE_PATTERN, "");
            combatLogEntryEntity.setItem(item);

            combatLogEntryEntity.setMatch(matchEntity);

            return combatLogEntryEntity;
        }
        // Returns null if proper log line is not found
        return null;
    }
}
