package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PurchaseEventTransformerTest {

    @InjectMocks
    private PurchaseEventTransformer eventTransformer;

    private MatchEntity matchEntity;

    @BeforeEach
    void setUp() {
        matchEntity = new MatchEntity();
    }

    @Test
    void test_transformEvent_success() {
        String logLine = "00:26:46.462] npc_dota_hero_bane buys item item_ward_dispenser";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertEquals(1606462, combatLogEntryEntity.getTimestamp());
        Assertions.assertEquals("bane", combatLogEntryEntity.getActor());
        Assertions.assertEquals("ward_dispenser", combatLogEntryEntity.getItem());
    }

    @Test
    void test_transformEvent_invalidLogLine() {
        String logLine = "00:26:46.462] npc_dota_test_bane buys item_ward_dispenser";
        CombatLogEntryEntity combatLogEntryEntity = eventTransformer.transformEvent(logLine, matchEntity);
        Assertions.assertNull(combatLogEntryEntity);
    }
}