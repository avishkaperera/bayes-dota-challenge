package gg.bayes.challenge.common;

public class Constants {

    public static class EventMatchers {
        public static final String PURCHASE_EVENT = "buys";
        public static final String KILL_EVENT = "killed";
        public static final String SPELL_CAST_EVENT = "casts";
        public static final String DAMAGE_EVENT = "hits";
    }

    public static class EventTransformers {
        public static final String PURCHASE_TRANSFORMER = "purchaseEventTransformer";
        public static final String KILL_TRANSFORMER = "killEventTransformer";
        public static final String SPELL_CAST_TRANSFORMER = "spellCastEventTransformer";
        public static final String DAMAGE_TRANSFORMER = "damageEventTransformer";
    }

    private Constants() {
        // Private constructor to stop initialization
    }
}
