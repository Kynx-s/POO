package pe.edu.utp.adaptive.plugins.java;

import java.util.Arrays;
import pe.edu.utp.adaptive.api.Context;

public class SafeStrategy extends TimedTextStrategy {

    public SafeStrategy() {
        super("safe");
    }

    @Override
    public boolean supports(Context context) {
        return true;
    }

    @Override
    protected String process(String input) {
        String clean = input.chars()
                .filter(c -> !Character.isISOControl(c) || c == '\n' || c == '\t' || c == '\r')
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        int removed = input.length() - clean.length();
        long words = Arrays.stream(clean.split("\\s+"))
                .filter(w -> !w.isEmpty())
                .count();
        long lines = clean.lines().count();
        return "[safe] palabras=" + words + ", lineas=" + lines
                + ", caracteres_eliminados=" + removed;
    }
}