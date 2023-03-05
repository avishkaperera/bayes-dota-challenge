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
public class DamageEventTransformer implements EventTransformer {
    private static final int TIME_INDEX = 0;
    private static final String TIME_REPLACE_PATTERN = "]";
    private static final int ACTOR_INDEX = 1;
    private static final String ACTOR_REPLACE_PATTERN = "npc_dota_hero_";
    private static final int TARGET_INDEX = 3;
    private static final String TARGET_REPLACE_PATTERN = "npc_dota_hero_";

    private static final int DAMAGE_INDEX = 7;

    @Override
    public CombatLogEntryEntity transformEvent(String logLine, LocalTime gameStartTime, MatchEntity matchEntity) {
        CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
        List<String> words = Arrays.asList(logLine.split(" "));

        String eventTime = words.get(TIME_INDEX).replace(TIME_REPLACE_PATTERN, "");
        long purchasedTime = MILLIS.between(gameStartTime, CommonUtil.extractTimeInMilliseconds(eventTime));
        combatLogEntryEntity.setTimestamp(purchasedTime);

        combatLogEntryEntity.setType(CombatLogEntryEntity.Type.DAMAGE_DONE);

        String actor = words.get(ACTOR_INDEX).replace(ACTOR_REPLACE_PATTERN, "");
        combatLogEntryEntity.setActor(actor);

        String target = words.get(TARGET_INDEX).replace(TARGET_REPLACE_PATTERN, "");
        combatLogEntryEntity.setTarget(target);

        String damage = words.get(DAMAGE_INDEX);
        combatLogEntryEntity.setDamage(Integer.valueOf(damage));

        combatLogEntryEntity.setMatch(matchEntity);

        return combatLogEntryEntity;
    }
}
