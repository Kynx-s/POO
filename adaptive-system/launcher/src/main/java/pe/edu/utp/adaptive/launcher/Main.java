package pe.edu.utp.adaptive.launcher;

import pe.edu.utp.adaptive.core.AdaptiveEngine;
import pe.edu.utp.adaptive.core.Outcome;

public class Main {

    public static void main(String[] args) {
        AdaptiveEngine engine = new AdaptiveEngine();

        String normal = "El sistema adaptativo procesa documentos.\nCada línea está sana y legible.";
        String danado = "línea\u0000 rota\nlínea \u0001 dañada\nuna línea buena";

        run(engine, "Escenario 1: documento normal", normal, false);
        run(engine, "Escenario 2: documento con errores de formato", danado, false);
        run(engine, "Escenario 3: modo precisión (plugin Scala)", normal, true);

        System.out.println("\n=== Registro del sistema ===");
        engine.history().forEach(System.out::println);
    }

    private static void run(AdaptiveEngine engine, String title, String document, boolean precision) {
        Outcome o = engine.process(document, precision);
        System.out.println("\n" + title);
        System.out.printf("  Medido   : tamaño=%d bytes, error=%.1f %%, precisión=%b%n",
                o.context().sizeBytes(), o.context().errorRate() * 100, o.context().precisionMode());
        System.out.println("  Decisión : " + o.decision().strategyId() + " (" + o.decision().reason() + ")");
        System.out.println("  Resultado: " + o.result().value());
    }
}