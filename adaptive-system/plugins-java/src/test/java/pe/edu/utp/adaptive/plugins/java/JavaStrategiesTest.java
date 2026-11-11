package pe.edu.utp.adaptive.plugins.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import pe.edu.utp.adaptive.api.Result;
import pe.edu.utp.adaptive.api.Strategy;

class JavaStrategiesTest {

    @Test
    void fastCuentaPalabras() {
        Result<String> r = new FastStrategy().execute("hola mundo  cruel");
        assertEquals("fast", r.strategyId());
        assertTrue(r.value().contains("palabras=3"));
    }

    @Test
    void safeLimpiaCaracteresDeControl() {
        Result<String> r = new SafeStrategy().execute("hola\u0000 mundo");
        assertEquals("safe", r.strategyId());
        assertTrue(r.value().contains("caracteres_eliminados=1"));
    }

    @Test
    void polimorfismoMismoContrato() {
        List<Strategy<String, String>> plugins = List.of(new FastStrategy(), new SafeStrategy());
        for (Strategy<String, String> plugin : plugins) {
            Result<String> r = plugin.execute("uno dos tres");
            assertEquals(plugin.id(), r.strategyId());
        }
    }
}