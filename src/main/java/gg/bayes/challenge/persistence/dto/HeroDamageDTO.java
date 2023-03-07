package gg.bayes.challenge.persistence.dto;

/**
 * Interface to act as a proxy in retrieving hero damage data
 */
public interface HeroDamageDTO {

    String getTarget();

    Integer getDamageInstances();

    Integer getTotalDamage();
}
