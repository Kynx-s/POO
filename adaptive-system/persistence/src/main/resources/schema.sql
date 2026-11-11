CREATE TABLE IF NOT EXISTS processing_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    strategy_id VARCHAR(40) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    size_bytes BIGINT NOT NULL,
    error_rate DOUBLE NOT NULL,
    precision_mode BOOLEAN NOT NULL,
    elapsed_millis BIGINT NOT NULL
);
