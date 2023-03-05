package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;

import java.time.LocalTime;

public interface EventTransformer {
    CombatLogEntryEntity transformEvent(String logLine, LocalTime gameStartTime, MatchEntity matchEntity);
}
