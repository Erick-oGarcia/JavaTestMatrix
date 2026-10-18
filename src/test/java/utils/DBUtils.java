package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Opens JDBC connections for the database validation tests.
 *
 * The connection settings come from the environment (DB_URL, DB_USER, DB_PASSWORD) or from
 * system properties (-DDB_URL=...), so the same tests can point at a real database in CI.
 * When nothing is configured, an in-memory H2 database is used, so the suite is self-contained.
 *
 * Callers own the connection: open it in a try-with-resources block so it is always closed.
 */
public final class DBUtils {

    private static final Logger logger = LogManager.getLogger(DBUtils.class);

    private static final String DEFAULT_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASSWORD = "";

    private DBUtils() {
        // utility class, no instances
    }

    public static Connection getConnection() throws SQLException {
        String url = setting("DB_URL", DEFAULT_URL);
        String user = setting("DB_USER", DEFAULT_USER);
        String password = setting("DB_PASSWORD", DEFAULT_PASSWORD);

        Connection connection = DriverManager.getConnection(url, user, password);
        logger.info("Connected to database: {}", url);
        return connection;
    }

    private static String setting(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
