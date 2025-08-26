package hexlet.code.repository;

import hexlet.code.config.DataSourceProvider;
import hexlet.code.model.UrlChecks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlChecksRepository {

    public static UrlChecks save(UrlChecks urlChecks) throws SQLException {
        String sql = "INSERT INTO url_checks "
                + "(url_id, status_code, title, h1, description) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, urlChecks.getUrlId());
            ps.setInt(2, urlChecks.getStatusCode());
            ps.setString(3, urlChecks.getTitle());
            ps.setString(4, urlChecks.getH1());
            ps.setString(5, urlChecks.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    urlChecks.setId(rs.getLong(1));
                }
            }
        }

        // Fetch created_at, jdbc don't guarantee for each vendor that sql can return all generated columns
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT created_at FROM url_checks WHERE id = ?")) {
            ps.setLong(1, urlChecks.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    urlChecks.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
            }
        }
        return urlChecks;
    }

    public static List<UrlChecks> findAllByUrlId(Long id) throws SQLException, NullPointerException {
        if (id == null) {
            throw new NullPointerException("Id must be not null!");
        }

        String sql = "SELECT "
                + "id, url_id, status_code, title, h1, description, created_at "
                + "FROM url_checks "
                + "WHERE url_id = ?";

        List<UrlChecks> urlChecks = new ArrayList<>();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    urlChecks.add(new UrlChecks(
                            rs.getLong("id"),
                            rs.getLong("url_id"),
                            rs.getInt("status_code"),
                            rs.getString("title"),
                            rs.getString("h1"),
                            rs.getString("description"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        }
        return urlChecks;
    }

    public static List<UrlChecks> findAllLatest() throws SQLException {
        String sql = "SELECT "
                + "uc.id, uc.url_id, uc.status_code, uc.title, uc.h1, uc.description, uc.created_at "
                + "FROM url_checks uc "
                + "WHERE uc.created_at = ( "
                + "    SELECT MAX(uc2.created_at) "
                + "    FROM url_checks uc2 "
                + "    WHERE uc2.url_id = uc.url_id "
                + ") "
                + "AND uc.id = ( "
                + "    SELECT MIN(uc3.id) "
                + "    FROM url_checks uc3 "
                + "    WHERE uc3.url_id = uc.url_id "
                + "      AND uc3.created_at = uc.created_at "
                + ");";

        List<UrlChecks> urls = new ArrayList<>();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                urls.add(new UrlChecks(
                        rs.getLong("id"),
                        rs.getLong("url_id"),
                        rs.getInt("status_code"),
                        rs.getString("title"),
                        rs.getString("h1"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }
        return urls;
    }
}
