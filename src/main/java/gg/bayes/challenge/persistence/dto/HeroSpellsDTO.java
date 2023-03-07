package gg.bayes.challenge.persistence.dto;

/**
 * Interface to act as a proxy in retrieving hero spell cast data
 */
public interface HeroSpellsDTO {

    String getSpell();

    Integer getCasts();
}
