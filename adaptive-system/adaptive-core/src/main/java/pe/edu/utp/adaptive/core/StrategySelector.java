package pe.edu.utp.adaptive.core;

import java.util.List;
import java.util.Optional;
import pe.edu.utp.adaptive.api.Context;

public class StrategySelector {

    public static final String DEFAULT_ID = "fast";
    public static final double MAX_ERROR_RATE = 0.05;
    public static final long MAX_PRECISE_BYTES = 5_000_000L;

    private final List<Rule> rules;

    public StrategySelector(List<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    public static StrategySelector withDefaultRules() {
        Rule errorRule = c -> c.errorRate() > MAX_ERROR_RATE
                ? Optional.of(new Decision("safe", "error de formato " + percent(c.errorRate()) + " supera 5 %"))
                : Optional.empty();

        Rule precisionRule = c -> c.precisionMode() && c.sizeBytes() <= MAX_PRECISE_BYTES
                ? Optional.of(new Decision("precise", "modo precisión activado y tamaño dentro del límite"))
                : Optional.empty();

        return new StrategySelector(List.of(errorRule, precisionRule));
    }

    public Decision select(Context context) {
        return rules.stream()
                .map(rule -> rule.evaluate(context))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseGet(() -> new Decision(DEFAULT_ID, "condiciones normales: se usa el plugin por defecto"));
    }

    private static String percent(double value) {
        return String.format("%.1f %%", value * 100);
    }
}