package pe.edu.utp.adaptive.persistence;

public record ProcessingRecord(Long id, String strategyId, String reason,
        long sizeBytes, double errorRate, boolean precisionMode, long elapsedMillis) {
}