package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;

import java.time.LocalTime;

public interface EventTransformer {

    /**
     * Method to transform log data to entity
     *
     * @param logLine       Single log line
     * @param gameStartTime Game start time
     * @param matchEntity   Match entity
     * @return CombatLogEntryEntity
     */
    CombatLogEntryEntity transformEvent(String logLine, LocalTime gameStartTime, MatchEntity matchEntity);
}
