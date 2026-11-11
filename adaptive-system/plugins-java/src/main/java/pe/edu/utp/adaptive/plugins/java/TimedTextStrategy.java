package pe.edu.utp.adaptive.plugins.java;

import pe.edu.utp.adaptive.api.Result;
import pe.edu.utp.adaptive.api.Strategy;

public abstract class TimedTextStrategy implements Strategy<String, String> {

    private final String id;

    protected TimedTextStrategy(String id) {
        this.id = id;
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final Result<String> execute(String input) {
        long start = System.nanoTime();
        String output = process(input);
        long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
        return new Result<>(output, id, elapsedMillis);
    }

    protected abstract String process(String input);
}