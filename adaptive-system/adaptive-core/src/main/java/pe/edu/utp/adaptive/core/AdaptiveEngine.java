package pe.edu.utp.adaptive.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pe.edu.utp.adaptive.api.Context;
import pe.edu.utp.adaptive.api.Strategy;

public class AdaptiveEngine {

    private final Monitor monitor;
    private final StrategySelector selector;
    private final PluginRegistry registry;
    private final List<String> history = new ArrayList<>();

    public AdaptiveEngine() {
        this(new Monitor(), StrategySelector.withDefaultRules(), new PluginRegistry());
    }

    public AdaptiveEngine(Monitor monitor, StrategySelector selector, PluginRegistry registry) {
        this.monitor = monitor;
        this.selector = selector;
        this.registry = registry;
    }

    public Outcome process(String document, boolean precisionMode) {
        Context context = monitor.measure(document, precisionMode);
        Decision decision = selector.select(context);
        Strategy<String, String> plugin = registry.create(decision.strategyId());

        if (!plugin.supports(context)) {
            decision = new Decision(StrategySelector.DEFAULT_ID,
                    decision.strategyId() + " no soporta el contexto; se usa el plugin por defecto");
            plugin = registry.create(decision.strategyId());
        }

        Outcome outcome = new Outcome(context, decision, plugin.execute(document));
        history.add(String.format("plugin=%s | motivo=%s | tiempo=%d ms",
                decision.strategyId(), decision.reason(), outcome.result().elapsedMillis()));
        return outcome;
    }

    public List<String> history() {
        return Collections.unmodifiableList(history);
    }
}