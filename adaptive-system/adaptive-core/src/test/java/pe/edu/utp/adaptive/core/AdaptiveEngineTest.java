package pe.edu.utp.adaptive.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pe.edu.utp.adaptive.api.Context;

class AdaptiveEngineTest {

    private final AdaptiveEngine engine = new AdaptiveEngine();

    @Test
    void condicionesNormalesUsanFast() {
        Outcome o = engine.process("hola mundo\nsegunda linea", false);
        assertEquals("fast", o.result().strategyId());
    }

    @Test
    void documentoDanadoCambiaASafe() {
        Outcome o = engine.process("linea\u0000rota\nlinea\u0001mala\nlinea buena", false);
        assertEquals("safe", o.result().strategyId());
        assertTrue(o.context().errorRate() > 0.05);
    }

    @Test
    void modoPrecisionCargaElPluginScala() {
        Outcome o = engine.process("uno dos tres\ncuatro cinco", true);
        assertEquals("precise", o.result().strategyId());
        assertTrue(o.result().value().startsWith("[precise]"));
    }

    @Test
    void elRegistroGuardaElMotivo() {
        engine.process("texto normal", false);
        assertEquals(1, engine.history().size());
        assertTrue(engine.history().get(0).contains("motivo="));
    }

    @Test
    void selectorNoNecesitaPluginsParaDecidir() {
        StrategySelector selector = StrategySelector.withDefaultRules();
        assertEquals("safe", selector.select(new Context(100, 0.5, false)).strategyId());
        assertEquals("precise", selector.select(new Context(100, 0.0, true)).strategyId());
        assertEquals("fast", selector.select(new Context(100, 0.0, false)).strategyId());
    }

    @Test
    void pluginInexistenteLanzaExcepcion() {
        PluginRegistry registry = new PluginRegistry();
        assertThrows(PluginException.class, () -> registry.create("no-existe"));
    }
}