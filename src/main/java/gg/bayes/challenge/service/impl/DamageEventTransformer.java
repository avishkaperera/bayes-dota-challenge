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
public class DamageEventTransformer implements EventTransformer {
    private static final int TIME_INDEX = 0;
    private static final String TIME_REPLACE_PATTERN = "]";
    private static final int ACTOR_INDEX = 1;
    private static final String ACTOR_REPLACE_PATTERN = "npc_dota_hero_";
    private static final int TARGET_INDEX = 3;
    private static final String TARGET_REPLACE_PATTERN = "npc_dota_hero_";

    private static final int DAMAGE_INDEX = 7;

    @Override
    public CombatLogEntryEntity transformEvent(String logLine, MatchEntity matchEntity) {
        CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
        List<String> words = Arrays.asList(logLine.split(" "));
        String targetFullString = words.size() >= 7 ? words.get(TARGET_INDEX) : "";

        if (targetFullString.contains(TARGET_REPLACE_PATTERN)) {
            String eventTime = words.get(TIME_INDEX).replace(TIME_REPLACE_PATTERN, "");
            combatLogEntryEntity.setTimestamp(CommonUtil.extractTimeDiffInMilliseconds(eventTime));

            combatLogEntryEntity.setType(CombatLogEntryEntity.Type.DAMAGE_DONE);

            String actor = words.get(ACTOR_INDEX).replace(ACTOR_REPLACE_PATTERN, "");
            combatLogEntryEntity.setActor(actor);


            String target = targetFullString.replace(TARGET_REPLACE_PATTERN, "");
            combatLogEntryEntity.setTarget(target);

            String damage = words.get(DAMAGE_INDEX);
            combatLogEntryEntity.setDamage(Integer.valueOf(damage));

            combatLogEntryEntity.setMatch(matchEntity);

            return combatLogEntryEntity;
        }
        // Returns null if the damage is not done to a hero
        return null;
    }
}
