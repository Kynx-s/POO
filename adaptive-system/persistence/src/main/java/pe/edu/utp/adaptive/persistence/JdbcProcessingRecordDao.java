package pe.edu.utp.adaptive.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcProcessingRecordDao implements ProcessingRecordDao {

    private final Database database;

    public JdbcProcessingRecordDao(Database database) {
        this.database = database;
    }

    @Override
    public ProcessingRecord create(ProcessingRecord r) {
        String sql = "INSERT INTO processing_record (strategy_id, reason, size_bytes, error_rate, "
                + "precision_mode, elapsed_millis) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = database.connect();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, r);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new ProcessingRecord(keys.getLong(1), r.strategyId(), r.reason(),
                        r.sizeBytes(), r.errorRate(), r.precisionMode(), r.elapsedMillis());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo crear el registro", e);
        }
    }

    @Override
    public Optional<ProcessingRecord> findById(long id) {
        try (Connection c = database.connect();
                PreparedStatement ps = c.prepareStatement("SELECT * FROM processing_record WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo leer el registro", e);
        }
    }

    @Override
    public List<ProcessingRecord> findAll() {
        List<ProcessingRecord> result = new ArrayList<>();
        try (Connection c = database.connect();
                PreparedStatement ps = c.prepareStatement("SELECT * FROM processing_record ORDER BY id");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo listar los registros", e);
        }
    }

    @Override
    public boolean update(ProcessingRecord r) {
        String sql = "UPDATE processing_record SET strategy_id = ?, reason = ?, size_bytes = ?, "
                + "error_rate = ?, precision_mode = ?, elapsed_millis = ? WHERE id = ?";
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, r);
            ps.setLong(7, r.id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar el registro", e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection c = database.connect();
                PreparedStatement ps = c.prepareStatement("DELETE FROM processing_record WHERE id = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo borrar el registro", e);
        }
    }

    private static void bind(PreparedStatement ps, ProcessingRecord r) throws SQLException {
        ps.setString(1, r.strategyId());
        ps.setString(2, r.reason());
        ps.setLong(3, r.sizeBytes());
        ps.setDouble(4, r.errorRate());
        ps.setBoolean(5, r.precisionMode());
        ps.setLong(6, r.elapsedMillis());
    }

    private static ProcessingRecord map(ResultSet rs) throws SQLException {
        return new ProcessingRecord(rs.getLong("id"), rs.getString("strategy_id"), rs.getString("reason"),
                rs.getLong("size_bytes"), rs.getDouble("error_rate"), rs.getBoolean("precision_mode"),
                rs.getLong("elapsed_millis"));
    }
}