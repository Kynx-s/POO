package pe.edu.utp.adaptive.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final String url;
    private final String user;
    private final String password;

    public Database() {
        this(env("DB_URL", "jdbc:h2:./data/adaptive"), env("DB_USER", "sa"), env("DB_PASSWORD", ""));
    }

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        initSchema();
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void initSchema() {
        try (InputStream in = Database.class.getResourceAsStream("/schema.sql")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró schema.sql");
            }
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection connection = connect(); Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("No se pudo inicializar la base de datos", e);
        }
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }
}