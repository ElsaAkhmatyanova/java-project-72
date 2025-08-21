package hexlet.code.util;

import hexlet.code.config.DataSourceProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DataBaseInitializer {

    public static void runSqlScript(Path sqlFile) throws IOException, SQLException {
        String sql = Files.readString(sqlFile);
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            for (String command : sql.split(";")) {
                String trimmed = command.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }

    public static void initializeSchema() throws SQLException, IOException {
        log.info("Initialize schema schema");
        runSqlScript(Path.of("src/main/resources/schema.sql"));
    }
}
