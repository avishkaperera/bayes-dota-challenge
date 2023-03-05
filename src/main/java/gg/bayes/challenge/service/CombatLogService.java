package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;

import java.util.HashSet;

public interface CombatLogService {
    HashSet<CombatLogEntryEntity> extractEvents(String combatLog, MatchEntity matchEntity);
}
