package hexlet.code.mapper;

import hexlet.code.dto.UrlInfoDto;
import hexlet.code.model.Urls;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlsMapperTest {

    @Test
    void testMapToUrlInfoDtoWithCreatedAt() {
        Urls urls = new Urls();
        urls.setId(1L);
        urls.setName("example.com");
        urls.setCreatedAt(LocalDateTime.of(2023, 8, 25, 14, 30));

        UrlInfoDto record = UrlsMapper.mapToDto(urls);
        assertEquals(1L, record.id());
        assertEquals("example.com", record.name());
        assertEquals("25/08/2023 14:30", record.createdAt());
    }

    @Test
    void testMapToUrlInfoDtoWithoutCreatedAt() {
        Urls urls = new Urls();
        urls.setId(2L);
        urls.setName("no-date.com");
        urls.setCreatedAt(null);

        UrlInfoDto record = UrlsMapper.mapToDto(urls);
        assertEquals(2L, record.id());
        assertEquals("no-date.com", record.name());
        assertEquals("", record.createdAt());
    }
}
