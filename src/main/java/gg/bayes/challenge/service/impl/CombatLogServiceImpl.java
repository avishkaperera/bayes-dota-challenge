package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.common.Constants;
import gg.bayes.challenge.persistence.dto.HeroDamageDTO;
import gg.bayes.challenge.persistence.dto.HeroItemDTO;
import gg.bayes.challenge.persistence.dto.HeroKillsDTO;
import gg.bayes.challenge.persistence.dto.HeroSpellsDTO;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.CombatLogService;
import gg.bayes.challenge.service.EventTransformer;
import gg.bayes.challenge.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CombatLogServiceImpl implements CombatLogService {
    private static final int GAME_START_TIME_INDEX = 1;

    @Autowired
    private Map<String, EventTransformer> eventTransformers;

    @Autowired
    private CombatLogEntryRepository combatLogEntryRepository;

    @Override
    public HashSet<CombatLogEntryEntity> extractEvents(String combatLog, MatchEntity matchEntity) {
        log.info("Starting to extract events from combat log");
        List<String> logLines = Arrays.asList(combatLog.split("\\["));
        return processLogLines(logLines, CommonUtil.extractTimeInMilliseconds(logLines.get(GAME_START_TIME_INDEX)), matchEntity);
    }

    @Override
    public List<HeroKills> getHeroKillsForMatch(Long matchId) {
        List<HeroKillsDTO> heroKills = combatLogEntryRepository.getHeroKillsByMatchId(matchId);
        return heroKills.stream().map(heroKill -> new HeroKills(heroKill.getHero(), heroKill.getKills()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HeroItem> getItemsBoughtByHero(Long matchId, String heroName) {
        List<HeroItemDTO> heroItems = combatLogEntryRepository.getHeroItemsByMatchIdAndActor(matchId, heroName);
        return heroItems.stream().map(heroItem -> new HeroItem(heroItem.getItem(), heroItem.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HeroSpells> getHeroSpellsForMatch(Long matchId, String heroName) {
        List<HeroSpellsDTO> heroSpells = combatLogEntryRepository.getHeroSpellsByMatchIdAndActor(matchId, heroName);
        return heroSpells.stream().map(heroSpell -> new HeroSpells(heroSpell.getSpell(), heroSpell.getCasts()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HeroDamage> getHeroDamageForMatch(Long matchId, String heroName) {
        List<HeroDamageDTO> heroDamages = combatLogEntryRepository.getHeroDamageByMatchIdAndActor(matchId, heroName);
        return heroDamages.stream().
                map(heroDamage -> new HeroDamage(heroDamage.getTarget(), heroDamage.getDamageInstances(), heroDamage.getTotalDamage()))
                .collect(Collectors.toList());
    }

    private HashSet<CombatLogEntryEntity> processLogLines(List<String> logLines, LocalTime gameStartTime, MatchEntity matchEntity) {
        HashSet<CombatLogEntryEntity> combatLogEntryEntities = new HashSet<>();
        logLines.forEach(line -> {
            if (line.contains(Constants.EventMatchers.PURCHASE_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.PURCHASE_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            } else if (line.contains(Constants.EventMatchers.KILL_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.KILL_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            } else if (line.contains(Constants.EventMatchers.SPELL_CAST_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.SPELL_CAST_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            } else if (line.contains(Constants.EventMatchers.DAMAGE_EVENT)) {
                EventTransformer eventTransformer = eventTransformers.get(Constants.EventTransformers.DAMAGE_TRANSFORMER);
                combatLogEntryEntities.add(eventTransformer.transformEvent(line, gameStartTime, matchEntity));
            }
        });
        return combatLogEntryEntities;
    }
}
