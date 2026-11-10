package pe.edu.utp.adaptive.core;

import java.nio.charset.StandardCharsets;
import pe.edu.utp.adaptive.api.Context;

public class Monitor {

    public Context measure(String document, boolean precisionMode) {
        long sizeBytes = document.getBytes(StandardCharsets.UTF_8).length;
        long totalLines = document.lines().count();
        long damagedLines = document.lines().filter(Monitor::isDamaged).count();
        double errorRate = totalLines == 0 ? 0.0 : (double) damagedLines / totalLines;
        return new Context(sizeBytes, errorRate, precisionMode);
    }

    private static boolean isDamaged(String line) {
        return line.chars().anyMatch(c -> Character.isISOControl(c) && c != '\t' && c != '\r');
    }
}