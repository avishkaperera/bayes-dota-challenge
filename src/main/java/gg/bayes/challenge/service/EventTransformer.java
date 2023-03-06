package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;

public interface EventTransformer {

    /**
     * Method to transform log data to entity
     *
     * @param logLine     Single log line
     * @param matchEntity Match entity
     * @return CombatLogEntryEntity if conditions applied meet or null
     */
    CombatLogEntryEntity transformEvent(String logLine, MatchEntity matchEntity);
}
