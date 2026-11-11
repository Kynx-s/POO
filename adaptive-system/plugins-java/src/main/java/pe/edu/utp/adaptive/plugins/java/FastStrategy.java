package pe.edu.utp.adaptive.plugins.java;

import pe.edu.utp.adaptive.api.Context;

public class FastStrategy extends TimedTextStrategy {

    public FastStrategy() {
        super("fast");
    }

    @Override
    public boolean supports(Context context) {
        return true;
    }

    @Override
    protected String process(String input) {
        long words = 0;
        boolean inWord = false;
        for (int i = 0; i < input.length(); i++) {
            boolean space = Character.isWhitespace(input.charAt(i));
            if (!space && !inWord) {
                words++;
            }
            inWord = !space;
        }
        return "[fast] palabras=" + words;
    }
}