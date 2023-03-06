package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.dto.HeroDamageDTO;
import gg.bayes.challenge.persistence.dto.HeroItemDTO;
import gg.bayes.challenge.persistence.dto.HeroKillsDTO;
import gg.bayes.challenge.persistence.dto.HeroSpellsDTO;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntryEntity, Long> {

    /**
     * Method to query hero kills by match id
     *
     * @param matchId ID of the match
     * @return List of Hero Kills DTO
     */
    @Query(value = "SELECT ACTOR AS HERO, COUNT(*) AS KILLS FROM DOTA_COMBAT_LOG WHERE MATCH_ID = :matchId AND ENTRY_TYPE = 'HERO_KILLED' GROUP BY ACTOR",
            nativeQuery = true)
    List<HeroKillsDTO> getHeroKillsByMatchId(Long matchId);

    /**
     * Method to query hero items by match id and actor
     *
     * @param matchId ID of the match
     * @param actor   name of the actor
     * @return List of Hero Item DTO
     */
    @Query(value = "SELECT ITEM, ENTRY_TIMESTAMP AS TIMESTAMP FROM DOTA_COMBAT_LOG WHERE ACTOR = :actor AND MATCH_ID = :matchId AND ENTRY_TYPE = 'ITEM_PURCHASED'",
            nativeQuery = true)
    List<HeroItemDTO> getHeroItemsByMatchIdAndActor(Long matchId, String actor);

    /**
     * Method to query hero spells by match id and actor
     *
     * @param matchId  ID of the match
     * @param heroName Name of the hero
     * @return List of Hero Spells DTO
     */
    @Query(value = "SELECT ABILITY AS SPELL, COUNT(*) AS CASTS FROM DOTA_COMBAT_LOG WHERE ACTOR = :heroName AND MATCH_ID = :matchId AND ENTRY_TYPE = 'SPELL_CAST' GROUP BY ABILITY",
            nativeQuery = true)
    List<HeroSpellsDTO> getHeroSpellsByMatchIdAndActor(Long matchId, String heroName);

    /**
     * Method to query hero damages by match id and actor
     *
     * @param matchId  ID of the match
     * @param heroName Name of the hero
     * @return List of Hero Damage DTO
     */
    @Query(value = "SELECT TARGET, COUNT(*) AS DAMAGEINSTANCES, SUM(DAMAGE) AS TOTALDAMAGE FROM DOTA_COMBAT_LOG WHERE ACTOR = :heroName AND MATCH_ID = :matchId AND ENTRY_TYPE = 'DAMAGE_DONE' GROUP BY TARGET",
            nativeQuery = true)
    List<HeroDamageDTO> getHeroDamageByMatchIdAndActor(Long matchId, String heroName);

    /**
     * Method to query a combat log entry by actor and event type
     *
     * @param actor Name of the hero
     * @param type Event type
     * @return CombatLogEntryEntity
     */
    CombatLogEntryEntity findFirstByActorAndType(String actor, CombatLogEntryEntity.Type type);
}
