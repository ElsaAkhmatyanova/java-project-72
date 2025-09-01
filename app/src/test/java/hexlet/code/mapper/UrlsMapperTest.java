package hexlet.code.mapper;

import hexlet.code.dto.UrlCheckInfoDto;
import hexlet.code.dto.UrlInfoDto;
import hexlet.code.dto.UrlsWithCheckDto;
import hexlet.code.model.UrlChecks;
import hexlet.code.model.Urls;
import hexlet.code.repository.projection.UrlsWithCheckProjection;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class UrlsMapperTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Test
    void testMapToUrlInfoDtoWithCreatedAt() {
        Urls urls = new Urls();
        urls.setId(1L);
        urls.setName("https://example.com");
        urls.setCreatedAt(LocalDateTime.of(2025, 8, 26, 12, 30));

        UrlInfoDto dto = UrlsMapper.mapToDto(urls);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("https://example.com");
        assertThat(dto.createdAt()).isEqualTo(FORMATTER.format(urls.getCreatedAt()));
    }

    @Test
    void testMapToUrlInfoDtoWithoutCreatedAt() {
        Urls urls = new Urls();
        urls.setId(2L);
        urls.setName("https://no-date.com");
        urls.setCreatedAt(null);

        UrlInfoDto dto = UrlsMapper.mapToDto(urls);

        assertThat(dto.createdAt()).isEmpty();
    }

    @Test
    void testMapToUrlsWithCheckDtoWithCheck() {
        UrlsWithCheckProjection urlsWithCheckProjection = new UrlsWithCheckProjection(
                3L,
                "https://check.com",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                200
        );
        UrlsWithCheckDto dto = UrlsMapper.mapToDto(urlsWithCheckProjection);
        assertThat(dto.id()).isEqualTo(3L);
        assertThat(dto.name()).isEqualTo("https://check.com");
        assertThat(dto.lastCheck()).isEqualTo(FORMATTER.format(urlsWithCheckProjection.lastCheck()));
        assertThat(dto.code()).isEqualTo(200);
    }

    @Test
    void testMapToUrlsWithCheckDtoWithoutCheck() {
        UrlsWithCheckProjection urlsWithCheckProjection = new UrlsWithCheckProjection(
                4L,
                "https://check.com",
                null,
                null
        );

        UrlsWithCheckDto dto = UrlsMapper.mapToDto(urlsWithCheckProjection);

        assertThat(dto.id()).isEqualTo(4L);
        assertThat(dto.name()).isEqualTo("https://check.com");
        assertThat(dto.lastCheck()).isNull();
        assertThat(dto.code()).isNull();
    }

    @Test
    void testMapToUrlCheckInfoDtoWithCreatedAt() {
        UrlChecks check = new UrlChecks();
        check.setId(10L);
        check.setStatusCode(404);
        check.setTitle("Not Found");
        check.setH1("Error Page");
        check.setDescription("Page not found");
        check.setCreatedAt(LocalDateTime.of(2025, 8, 25, 23, 59));

        UrlCheckInfoDto dto = UrlsMapper.mapToDto(check);

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.code()).isEqualTo(404);
        assertThat(dto.title()).isEqualTo("Not Found");
        assertThat(dto.h1()).isEqualTo("Error Page");
        assertThat(dto.description()).isEqualTo("Page not found");
        assertThat(dto.createdAt()).isEqualTo(FORMATTER.format(check.getCreatedAt()));
    }

    @Test
    void testMapToUrlCheckInfoDtoWithoutCreatedAt() {
        UrlChecks check = new UrlChecks();
        check.setId(11L);
        check.setStatusCode(500);
        check.setTitle("Server Error");
        check.setH1("Error 500");
        check.setDescription("Something went wrong");
        check.setCreatedAt(null);

        UrlCheckInfoDto dto = UrlsMapper.mapToDto(check);

        assertThat(dto.createdAt()).isEqualTo("");
    }
}
