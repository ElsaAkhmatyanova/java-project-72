package hexlet.code.repository;

import hexlet.code.config.DataSourceProvider;
import hexlet.code.model.Urls;
import hexlet.code.repository.projection.UrlsWithCheckProjection;
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
        if (urls == null) {
            throw new NullPointerException("Object to save must be not null");
        }

        String sql = "INSERT INTO urls (name) VALUES (?)";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, urls.getName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Failed to retrieve generated ID for URL=" + urls.getName());
                }
                urls.setId(rs.getLong(1));
            }
        }

        // Fetch created_at, jdbc don't guarantee for each vendor that sql can return all generated columns
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT created_at FROM urls WHERE id = ?")) {
            ps.setLong(1, urls.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Failed to retrieve created_at for URL with id=" + urls.getId());
                }
                urls.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
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
                return rs.next();
            }
        }
    }

    public static List<UrlsWithCheckProjection> findAllWithLatestCheck() throws SQLException {
        String sql = """
                SELECT u.id AS url_id,
                       u.name AS url_name,
                       uc.created_at AS check_created_at,
                       uc.status_code AS check_status_code
                FROM urls u
                LEFT JOIN url_checks uc
                    ON uc.id = (
                        SELECT uc2.id
                        FROM url_checks uc2
                        WHERE uc2.url_id = u.id
                        ORDER BY uc2.created_at DESC, uc2.id DESC
                        LIMIT 1
                    )
                ORDER BY u.id
                """;

        List<UrlsWithCheckProjection> result = new ArrayList<>();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new UrlsWithCheckProjection(
                        rs.getLong("url_id"),
                        rs.getString("url_name"),
                        rs.getTimestamp("check_created_at") != null
                                ? rs.getTimestamp("check_created_at").toLocalDateTime()
                                : null,
                        rs.getInt("check_status_code")
                ));
            }
        }
        return result;
    }
}
