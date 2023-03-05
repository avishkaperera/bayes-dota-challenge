package gg.bayes.challenge.service;

public interface MatchService {

    /**
     * Method to process the uploaded log file
     *
     * @param combatLog Uploaded log file data
     * @return ID of persisted combat log entity
     */
    Long processCombatLog(String combatLog);
}
