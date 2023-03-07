package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.CombatLogService;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/match")
@Validated
@RequiredArgsConstructor
public class MatchController extends ExceptionHandlerController {

    private final MatchService matchService;
    private final CombatLogService combatLogService;

    /**
     * Ingests a DOTA combat log file, parses and persists relevant events data. All events are associated with the same
     * match id.
     *
     * @param combatLog the content of the combat log file
     * @return the match id associated with the parsed events
     */
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Long> ingestCombatLog(@RequestBody @NotBlank String combatLog) {
        log.info("Received a file to extract and store combat log events");
        Long savedEntityId = matchService.processCombatLog(combatLog);
        log.info("Successfully processed received combat log for match id - [{}]", savedEntityId);
        return new ResponseEntity<>(savedEntityId, HttpStatus.CREATED);
    }

    /**
     * Fetches the heroes and their kill counts for the given match.
     *
     * @param matchId the match identifier
     * @return a collection of heroes and their kill counts
     */
    @GetMapping(
            path = "{matchId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
        log.info("Request received to get kill count of each hero in match [{}]", matchId);
        List<HeroKills> heroKills = combatLogService.getHeroKillsForMatch(matchId);
        log.info("Successfully queried for hero kills");
        return new ResponseEntity<>(heroKills, HttpStatus.OK);
    }

    /**
     * For the given match, fetches the items bought by the named hero.
     *
     * @param matchId  the match identifier
     * @param heroName the hero name
     * @return a collection of items bought by the hero during the match
     */
    @GetMapping(
            path = "{matchId}/{heroName}/items",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroItem>> getHeroItems(
            @PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        log.info("Request received to get items bought by [{}] in match [{}]", heroName, matchId);
        List<HeroItem> itemsBoughtByHero = combatLogService.getItemsBoughtByHero(matchId, heroName);
        log.info("Successfully queried items bought by [{}] in match [{}]", heroName, matchId);
        return new ResponseEntity<>(itemsBoughtByHero, HttpStatus.OK);
    }

    /**
     * For the given match, fetches the spells cast by the named hero.
     *
     * @param matchId  the match identifier
     * @param heroName the hero name
     * @return a collection of spells cast by the hero and how many times they were cast
     */
    @GetMapping(
            path = "{matchId}/{heroName}/spells",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroSpells>> getHeroSpells(
            @PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        log.info("Request received to get the spells cast by [{}] in match [{}]", heroName, matchId);
        List<HeroSpells> heroSpells = combatLogService.getHeroSpellsForMatch(matchId, heroName);
        log.info("Successfully queried the spells cast by [{}] in match [{}]", heroName, matchId);
        return new ResponseEntity<>(heroSpells, HttpStatus.OK);
    }

    /**
     * For a given match, fetches damage done data for the named hero.
     *
     * @param matchId  the match identifier
     * @param heroName the hero name
     * @return a collection of "damage done" (target, number of times and total damage) elements
     */
    @GetMapping(
            path = "{matchId}/{heroName}/damage",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroDamage>> getHeroDamages(
            @PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        log.info("Request received to get damage done by [{}] in match [{}]", heroName, matchId);
        List<HeroDamage> heroDamages = combatLogService.getHeroDamageForMatch(matchId, heroName);
        log.info("Successfully queried damage done by [{}] in match [{}]", heroName, matchId);
        return new ResponseEntity<>(heroDamages, HttpStatus.OK);
    }
}
