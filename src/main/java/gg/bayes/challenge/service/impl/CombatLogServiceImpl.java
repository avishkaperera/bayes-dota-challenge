package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.common.Constants;
import gg.bayes.challenge.persistence.dto.HeroDamageDTO;
import gg.bayes.challenge.persistence.dto.HeroItemDTO;
import gg.bayes.challenge.persistence.dto.HeroKillsDTO;
import gg.bayes.challenge.persistence.dto.HeroSpellsDTO;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.CombatLogService;
import gg.bayes.challenge.service.EventTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CombatLogServiceImpl implements CombatLogService {

    public static final String INVALID_MATCH_ID_PROVIDED_ERROR_MESSAGE = "Invalid match id provided";
    public static final String INVALID_HERO_NAME_PROVIDED_ERROR_MESSAGE = "Invalid hero name provided";

    private final Map<String, EventTransformer> eventTransformers;
    private final CombatLogEntryRepository combatLogEntryRepository;
    private final MatchRepository matchRepository;

    @Override
    public HashSet<CombatLogEntryEntity> extractEvents(String combatLog, MatchEntity matchEntity) {
        log.info("Starting to extract events from combat log");
        List<String> logLines = combatLog.lines().collect(Collectors.toList());
        return processLogLines(logLines, matchEntity);
    }

    @Override
    public List<HeroKills> getHeroKillsForMatch(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new IllegalArgumentException(INVALID_MATCH_ID_PROVIDED_ERROR_MESSAGE);
        }
        List<HeroKillsDTO> heroKills = combatLogEntryRepository.findHeroKillsByMatchId(matchId);
        return heroKills.stream().map(heroKill -> new HeroKills(heroKill.getHero(), heroKill.getKills()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HeroItem> getItemsBoughtByHero(Long matchId, String heroName) {
        if (!matchRepository.existsById(matchId)) {
            throw new IllegalArgumentException(INVALID_MATCH_ID_PROVIDED_ERROR_MESSAGE);
        }
        if (Objects.isNull(combatLogEntryRepository.findFirstByActorAndType(heroName, CombatLogEntryEntity.Type.ITEM_PURCHASED))) {
            throw new IllegalArgumentException(INVALID_HERO_NAME_PROVIDED_ERROR_MESSAGE);
        }
        List<HeroItemDTO> heroItems = combatLogEntryRepository.findHeroItemsByMatchIdAndActor(matchId, heroName);
        return heroItems.stream().map(heroItem -> new HeroItem(heroItem.getItem(), heroItem.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HeroSpells> getHeroSpellsForMatch(Long matchId, String heroName) {
        if (!matchRepository.existsById(matchId)) {
            throw new IllegalArgumentException(INVALID_MATCH_ID_PROVIDED_ERROR_MESSAGE);
        }
        if (Objects.isNull(combatLogEntryRepository.findFirstByActorAndType(heroName, CombatLogEntryEntity.Type.SPELL_CAST))) {
            throw new IllegalArgumentException(INVALID_HERO_NAME_PROVIDED_ERROR_MESSAGE);
        }
        List<HeroSpellsDTO> heroSpells = combatLogEntryRepository.findHeroSpellsByMatchIdAndActor(matchId, heroName);
        return heroSpells.stream().map(heroSpell -> new HeroSpells(heroSpell.getSpell(), heroSpell.getCasts()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HeroDamage> getHeroDamageForMatch(Long matchId, String heroName) {
        if (!matchRepository.existsById(matchId)) {
            throw new IllegalArgumentException(INVALID_MATCH_ID_PROVIDED_ERROR_MESSAGE);
        }
        if (Objects.isNull(combatLogEntryRepository.findFirstByActorAndType(heroName, CombatLogEntryEntity.Type.DAMAGE_DONE))) {
            throw new IllegalArgumentException(INVALID_HERO_NAME_PROVIDED_ERROR_MESSAGE);
        }
        List<HeroDamageDTO> heroDamages = combatLogEntryRepository.findHeroDamageByMatchIdAndActor(matchId, heroName);
        return heroDamages.stream().
                map(heroDamage -> new HeroDamage(heroDamage.getTarget(), heroDamage.getDamageInstances(), heroDamage.getTotalDamage()))
                .collect(Collectors.toList());
    }

    private HashSet<CombatLogEntryEntity> processLogLines(List<String> logLines, MatchEntity matchEntity) {
        HashSet<CombatLogEntryEntity> combatLogEntryEntities = new HashSet<>();
        logLines.forEach(line -> {
            if (line.contains(Constants.EventMatchers.PURCHASE_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.PURCHASE_TRANSFORMER);
                CombatLogEntryEntity purchaseEventEntity = eventTransformer.transformEvent(line, matchEntity);
                if (Objects.nonNull(purchaseEventEntity)) {
                    combatLogEntryEntities.add(eventTransformer.transformEvent(line, matchEntity));
                }
            } else if (line.contains(Constants.EventMatchers.KILL_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.KILL_TRANSFORMER);
                CombatLogEntryEntity killEventEntry = eventTransformer.transformEvent(line, matchEntity);
                if (Objects.nonNull(killEventEntry)) {
                    combatLogEntryEntities.add(eventTransformer.transformEvent(line, matchEntity));
                }
            } else if (line.contains(Constants.EventMatchers.SPELL_CAST_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.SPELL_CAST_TRANSFORMER);
                CombatLogEntryEntity spellCastEventEntity = eventTransformer.transformEvent(line, matchEntity);
                if (Objects.nonNull(spellCastEventEntity)) {
                    combatLogEntryEntities.add(eventTransformer.transformEvent(line, matchEntity));
                }
            } else if (line.contains(Constants.EventMatchers.DAMAGE_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.DAMAGE_TRANSFORMER);
                CombatLogEntryEntity damageEventEntry = eventTransformer.transformEvent(line, matchEntity);
                if (Objects.nonNull(damageEventEntry)) {
                    combatLogEntryEntities.add(eventTransformer.transformEvent(line, matchEntity));
                }
            }
        });
        return combatLogEntryEntities;
    }
}
