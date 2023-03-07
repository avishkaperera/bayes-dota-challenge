package gg.bayes.challenge.persistence.dto;

/**
 * Interface to act as a proxy in retrieving hero kill data
 */
public interface HeroKillsDTO {

    String getHero();

    Integer getKills();
}
