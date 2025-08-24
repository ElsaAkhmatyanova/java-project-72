package hexlet.code.repository;

import hexlet.code.config.DataSourceProvider;
import hexlet.code.model.Urls;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UrlsRepository {

    public static Urls save(Urls urls) throws SQLException {
        String sql = "INSERT INTO urls (name) VALUES (?)";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, urls.getName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    urls.setId(rs.getLong(1));
                }
            }
        }

        // Fetch created_at, jdbc don't guarantee for each vendor that sql can return all generated columns
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT created_at FROM urls WHERE id = ?")) {
            ps.setLong(1, urls.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    urls.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
            }
        }
        return urls;
    }

    public static Optional<Urls> findById(Long id) throws SQLException {
        if (id == null) {
            throw new NullPointerException("Id must be not null!");
        }

        String sql = "SELECT id, name, created_at FROM urls WHERE id = ?";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Urls urls = new Urls(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    return Optional.of(urls);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public static List<Urls> findAll() throws SQLException {
        String sql = "SELECT id, name, created_at FROM urls";

        List<Urls> urls = new ArrayList<>();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                urls.add(new Urls(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }
        return urls;
    }

    public static boolean isExistByName(String name) throws SQLException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name must be not null!");
        }

        String sql = "SELECT 1 FROM urls WHERE name = ? LIMIT 1";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row exists
            }
        }
    }
}
