package io.theawesomemogul.core.model;

/**
 * Enumerates the supported audio plugin types in the Mogul Audio ecosystem.
 *
 * <p>This shared domain enum is consumed by all services (Access Engine, LLM Engine)
 * and the C++ audio layer. Each constant maps to a distinct preset schema,
 * LLM prompt template, and audio processing pipeline.
 *
 * <p>Plugin types are categorized into four functional groups:
 * <ul>
 *   <li><strong>Tone Plugins:</strong> GUITAR, BASS, VOX, DRUMS, INSTRUMENT</li>
 *   <li><strong>Effect Plugins:</strong> REVERB, DELAY</li>
 *   <li><strong>Bus Plugins:</strong> MIXBUS</li>
 *   <li><strong>Orchestrator:</strong> ORCHESTRATOR</li>
 * </ul>
 *
 * @author Sonic Grit Ventures
 * @version 1.0.0
 */
public enum PluginType {

    /** Electric guitar amp and cabinet simulation. */
    GUITAR("Guitar Designer"),

    /** Bass guitar amp, compression, and tone shaping. */
    BASS("Bass Designer"),

    /** Vocal chain processing (compression, EQ, de-essing, sends). */
    VOX("Vox Designer"),

    /** Drum kit processing (transient shaping, gating, room). */
    DRUMS("Drums Designer"),

    /** Generic instrument processing (keys, acoustic, strings, etc.). */
    INSTRUMENT("Instrument Designer"),

    /** Reverb effect (plate, hall, spring, room, shimmer). */
    REVERB("Reverb Designer"),

    /** Delay effect (analog, tape, digital, ping-pong). */
    DELAY("Delay Designer"),

    /** Mix-bus / master-bus processing (glue compression, tape, console color). */
    MIXBUS("MixBus Designer"),

    /** AI orchestrator that coordinates multi-plugin session plans. */
    ORCHESTRATOR("Orchestrator");

    private final String displayName;

    PluginType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the user-facing display name for this plugin type.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a {@link PluginType} from a case-insensitive string value.
     *
     * @param value the plugin type name (case-insensitive, trimmed)
     * @return the matching {@link PluginType} constant, or null if value is null
     * @throws IllegalArgumentException if {@code value} is blank or does not
     *                                  match any constant
     */
    public static PluginType fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return PluginType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Checks if this plugin is a tone/instrument designer.
     * Tone plugins: GUITAR, BASS, VOX, DRUMS, INSTRUMENT
     *
     * @return true if this is a tone plugin type
     */
    public boolean isTonePlugin() {
        return this == GUITAR || this == BASS || this == VOX || this == DRUMS || this == INSTRUMENT;
    }

    /**
     * Checks if this plugin is an effect designer.
     * Effect plugins: REVERB, DELAY
     *
     * @return true if this is an effect plugin type
     */
    public boolean isEffectPlugin() {
        return this == REVERB || this == DELAY;
    }

    /**
     * Checks if this plugin is a bus/mix processor.
     * Bus plugins: MIXBUS
     *
     * @return true if this is a bus plugin type
     */
    public boolean isBusPlugin() {
        return this == MIXBUS;
    }

    /**
     * Checks if this plugin is the orchestrator.
     * Orchestrator: ORCHESTRATOR
     *
     * @return true if this is the orchestrator plugin type
     */
    public boolean isOrchestratorPlugin() {
        return this == ORCHESTRATOR;
    }
}
