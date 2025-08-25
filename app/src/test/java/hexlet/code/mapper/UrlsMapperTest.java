package hexlet.code.mapper;

import hexlet.code.dto.UrlRecord;
import hexlet.code.model.Urls;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlsMapperTest {

    @Test
    void testMapToRecordWithCreatedAt() {
        Urls urls = new Urls();
        urls.setId(1L);
        urls.setName("example.com");
        urls.setCreatedAt(LocalDateTime.of(2023, 8, 25, 14, 30));

        UrlRecord record = UrlsMapper.mapToRecord(urls);
        assertEquals(1L, record.id());
        assertEquals("example.com", record.name());
        assertEquals("25/08/2023 14:30", record.lastCheck());
        assertEquals(200, record.code());
    }

    @Test
    void testMapToRecordWithoutCreatedAt() {
        Urls urls = new Urls();
        urls.setId(2L);
        urls.setName("no-date.com");
        urls.setCreatedAt(null);

        UrlRecord record = UrlsMapper.mapToRecord(urls);
        assertEquals(2L, record.id());
        assertEquals("no-date.com", record.name());
        assertEquals("", record.lastCheck());
        assertEquals(200, record.code());
    }
}
