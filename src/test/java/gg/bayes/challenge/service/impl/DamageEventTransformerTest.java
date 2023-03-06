package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DamageEventTransformerTest {

    @InjectMocks
    private DamageEventTransformer eventTransformer;

    private MatchEntity matchEntity;

    @BeforeEach
    void setUp() {
        matchEntity = new MatchEntity();
    }

    @Test
    void test_transformEvent_success() {
        String logLine = "00:13:15.060] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 32 damage (698->666)";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertEquals(795060, combatLogEntryEntity.getTimestamp());
        Assertions.assertEquals("abyssal_underlord", combatLogEntryEntity.getActor());
        Assertions.assertEquals("bloodseeker", combatLogEntryEntity.getTarget());
        Assertions.assertEquals(32, combatLogEntryEntity.getDamage());
    }

    @Test
    void test_transformEvent_targetNotHero() {
        String logLine = "00:13:15.060] npc_dota_hero_abyssal_underlord hits npc_dota_blank_bloodseeker with abyssal_underlord_firestorm for 32 damage (698->666)";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertNull(combatLogEntryEntity);
    }

    @Test
    void test_transformEvent_logLineIsInvalid() {
        String logLine = "00:13:15.060] abyssal_underlord_firestorm for 32 damage (698->666)";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertNull(combatLogEntryEntity);
    }
}