package io.theawesomemogul.core.model;

/**
 * Enumerates the design intents that guide plugin parameter generation.
 *
 * <p>This enum captures the user's high-level goal or artistic direction
 * when designing or modifying a plugin. The intent is used by the LLM
 * to select appropriate parameter ranges, EQ curves, and processing strategies.
 *
 * @author Sonic Grit Ventures
 * @version 1.0.0
 */
public enum DesignIntent {

    /**
     * Enhance the input signal while preserving its core character.
     * Example: "Make the vocals brighter and more present."
     */
    ENHANCE,

    /**
     * Transform or significantly alter the input signal's character.
     * Example: "Make this guitar sound like a synth."
     */
    TRANSFORM,

    /**
     * Match the tonal characteristics of a reference signal or artist.
     * Example: "Make this vocal sound like it was sung by X artist."
     */
    MATCH_REFERENCE,

    /**
     * User-specified or custom design goal. Default when intent cannot be determined.
     */
    CUSTOM;

    /**
     * Resolves a {@link DesignIntent} from a case-insensitive string value.
     *
     * @param value the design intent name (case-insensitive, trimmed)
     * @return the matching {@link DesignIntent} constant, or CUSTOM if value is null or invalid
     */
    public static DesignIntent fromString(String value) {
        if (value == null) {
            return CUSTOM;
        }
        try {
            return DesignIntent.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return CUSTOM;
        }
    }
}
