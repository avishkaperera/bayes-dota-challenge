package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KillEventTransformerTest {

    @InjectMocks
    private KillEventTransformer eventTransformer;

    private MatchEntity matchEntity;

    @BeforeEach
    void setUp() {
        matchEntity = new MatchEntity();
    }

    @Test
    void test_transformEvent_success() {
        String logLine = "00:26:23.601] npc_dota_hero_rubick is killed by npc_dota_hero_dragon_knight";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertEquals(1583601, combatLogEntryEntity.getTimestamp());
        Assertions.assertEquals("dragon_knight", combatLogEntryEntity.getActor());
        Assertions.assertEquals("rubick", combatLogEntryEntity.getTarget());
    }

    @Test
    void test_transformEvent_invalidLogLine() {
        String logLine = "00:26:23.601] _rubick is killed by npc_dragon_knight";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertNull(combatLogEntryEntity);
    }
}