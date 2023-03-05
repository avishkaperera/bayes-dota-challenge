package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

import java.util.HashSet;
import java.util.List;

public interface CombatLogService {

    /**
     * Method to extract the events from uploaded log file
     *
     * @param combatLog   Uploaded log file data
     * @param matchEntity Corresponding match entity
     * @return HashSet of CombatLogEntryEntity
     */
    HashSet<CombatLogEntryEntity> extractEvents(String combatLog, MatchEntity matchEntity);

    /**
     * Method to get hero kills for a match
     *
     * @param matchId ID of the match
     * @return List of Hero Kills
     */
    List<HeroKills> getHeroKillsForMatch(Long matchId);

    /**
     * Method to get items bought by a hero
     *
     * @param matchId  ID of the match
     * @param heroName Name of the hero
     * @return List of Hero Items
     */
    List<HeroItem> getItemsBoughtByHero(Long matchId, String heroName);

    /**
     * Method to get spells casts by a hero for a match
     *
     * @param matchId  ID of the match
     * @param heroName Name of the hero
     * @return List of Hero Spells
     */
    List<HeroSpells> getHeroSpellsForMatch(Long matchId, String heroName);

    /**
     * Method to get damage done by hero for a match
     *
     * @param matchId  ID of the match
     * @param heroName Name of the hero
     * @return List of Hero Damages
     */
    List<HeroDamage> getHeroDamageForMatch(Long matchId, String heroName);
}
