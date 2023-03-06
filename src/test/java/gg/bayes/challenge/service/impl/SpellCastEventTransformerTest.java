package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpellCastEventTransformerTest {

    @InjectMocks
    private SpellCastEventTransformer eventTransformer;

    private MatchEntity matchEntity;

    @BeforeEach
    void setUp() {
        matchEntity = new MatchEntity();
    }

    @Test
    void test_transformEvent_success() {
        String logLine = "00:30:06.080] npc_dota_hero_dragon_knight casts ability dragon_knight_dragon_tail (lvl 3) on npc_dota_hero_death_prophet";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertEquals(1806080, combatLogEntryEntity.getTimestamp());
        Assertions.assertEquals("dragon_knight", combatLogEntryEntity.getActor());
        Assertions.assertEquals("dragon_knight_dragon_tail", combatLogEntryEntity.getAbility());
        Assertions.assertEquals(3, combatLogEntryEntity.getAbilityLevel());
        Assertions.assertEquals("death_prophet", combatLogEntryEntity.getTarget());
    }

    @Test
    void test_transformEvent_invalidLogLine() {
        String logLine = "00:30:06.080] npc_dota_hero_dragon_knight (lvl 3) on npc_dota_hero_death_prophet";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertNull(combatLogEntryEntity);
    }
}