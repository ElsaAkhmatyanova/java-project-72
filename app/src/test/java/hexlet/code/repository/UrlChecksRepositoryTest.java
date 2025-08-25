package hexlet.code.repository;

import hexlet.code.model.UrlChecks;
import hexlet.code.model.Urls;
import hexlet.code.util.DataBaseInitializer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlChecksRepositoryTest {

    @BeforeEach
    @SneakyThrows
    void setUp() {
        DataBaseInitializer.initializeSchema();
    }

    @Test
    void testFindAllByUrlIdSuccess() throws Exception {
        var urls = new Urls("https://example1.com");
        UrlsRepository.save(urls);
        var check1 = new UrlChecks(
                urls.getId(),
                200,
                "Example Title 1",
                "Example H1 1",
                "Some description 1"
        );
        UrlChecksRepository.save(check1);
        var check2 = new UrlChecks(
                urls.getId(),
                404,
                "Example Title 2",
                "Example H1 2",
                "Some description 2"
        );
        UrlChecksRepository.save(check2);

        List<UrlChecks> result = UrlChecksRepository.findAllByUrlId(urls.getId());
        assertThat(result)
                .hasSize(2)
                .anySatisfy(urlChecks -> assertEquals(check1.getTitle(), urlChecks.getTitle()))
                .anySatisfy(urlChecks -> assertEquals(check2.getTitle(), urlChecks.getTitle()));
    }

    @Test
    void testFindLastByUrlIdSuccess() throws Exception {
        var urls = new Urls("https://example1.com");
        UrlsRepository.save(urls);
        var check1 = new UrlChecks(
                urls.getId(),
                200,
                "Example Title 1",
                "Example H1 1",
                "Some description 1"
        );
        UrlChecksRepository.save(check1);
        var check2 = new UrlChecks(
                urls.getId(),
                404,
                "Example Title 2",
                "Example H1 2",
                "Some description 2"
        );
        UrlChecksRepository.save(check2);

        Optional<UrlChecks> result = UrlChecksRepository.findLastByUrlId(urls.getId());

        assertTrue(result.isPresent());
        assertEquals("Example Title 2", result.get().getTitle());
        assertEquals(404, result.get().getStatusCode());
    }

    @Test
    void testFindLastByUrlIdEmpty() throws Exception {
        Optional<UrlChecks> result = UrlChecksRepository.findLastByUrlId(1L);
        assertTrue(result.isEmpty());
    }
}