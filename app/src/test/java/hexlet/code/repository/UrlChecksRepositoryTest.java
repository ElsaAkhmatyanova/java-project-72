package hexlet.code.repository;

import hexlet.code.model.UrlChecks;
import hexlet.code.model.Urls;
import hexlet.code.util.DataBaseInitializer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testFindAllLatestSuccess() throws Exception {
        var urls1 = new Urls("https://example1.com");
        UrlsRepository.save(urls1);
        var check1 = new UrlChecks(
                urls1.getId(),
                200,
                "Example Title 1",
                "Example H1 1",
                "Some description 1"
        );
        UrlChecksRepository.save(check1);
        var check2 = new UrlChecks(
                urls1.getId(),
                404,
                "Example Title 2",
                "Example H1 2",
                "Some description 2"
        );
        UrlChecksRepository.save(check2);

        var urls2 = new Urls("https://example2.com");
        UrlsRepository.save(urls2);
        var check3 = new UrlChecks(
                urls2.getId(),
                201,
                "Example Title 3",
                "Example H1 3",
                "Some description 3"
        );
        UrlChecksRepository.save(check3);
        var check4 = new UrlChecks(
                urls2.getId(),
                400,
                "Example Title 4",
                "Example H1 4",
                "Some description 4"
        );
        UrlChecksRepository.save(check4);

        List<UrlChecks> result = UrlChecksRepository.findAllLatest();
        assertThat(result)
                .hasSize(2)
                .anySatisfy(urlChecks -> assertEquals(check1.getTitle(), urlChecks.getTitle()))
                .anySatisfy(urlChecks -> assertEquals(check3.getTitle(), urlChecks.getTitle()));
    }
}
