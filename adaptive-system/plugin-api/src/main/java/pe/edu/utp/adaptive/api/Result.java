package pe.edu.utp.adaptive.api;

public record Result<O>(O value, String strategyId, long elapsedMillis) {
}