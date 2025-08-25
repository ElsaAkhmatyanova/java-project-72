package hexlet.code;

import hexlet.code.model.Urls;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.DataBaseInitializer;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {

    private Javalin app;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        DataBaseInitializer.initializeSchema();
        app = App.getApp();
    }

    @Test
    @DisplayName("Should show main page from index.jte")
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    @SneakyThrows
    @DisplayName("Should show url list page from url_list.jte")
    public void testUrlListPage() {
        var urls1 = new Urls("https://example1.com");
        UrlsRepository.save(urls1);
        var urls2 = new Urls("https://example2.com");
        UrlsRepository.save(urls2);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            assertThat(response.body().string())
                    .contains("Сайты")
                    .contains("https://example1.com")
                    .contains("https://example2.com");
        });
    }

    @Nested
    class CreateUrl {

        @Test
        @SneakyThrows
        @DisplayName("Should create url entity")
        public void testCreateUrlSuccess() {
            final String testUrlName = "https://example.com";
            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + testUrlName;
                var response = client.post("/urls", requestBody);
                assertThat(response.code()).isEqualTo(200);
                assertThat(UrlsRepository.findById(1L))
                        .isPresent()
                        .satisfies(urls -> assertEquals(testUrlName, urls.get().getName()));
            });
        }

        @Test
        @SneakyThrows
        @DisplayName("Should not create url if already exists")
        public void testCreateUrlAlreadyExisted() {
            final String testUrlName = "https://example.com";
            var urls1 = new Urls(testUrlName);
            UrlsRepository.save(urls1);
            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + testUrlName;
                var response = client.post("/urls", requestBody);
                assertThat(response.code()).isEqualTo(200);
                assertThat(UrlsRepository.findAll())
                        .hasSize(1)
                        .first()
                        .satisfies(urls -> assertEquals(testUrlName, urls.getName()));
            });
        }

        @Test
        @SneakyThrows
        @DisplayName("Should not create url if its cant be parsed")
        public void testCreateUrlBadParse() {
            final String testUrlName = "wrong-url";
            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + testUrlName;
                var response = client.post("/urls", requestBody);
                assertThat(response.code()).isEqualTo(200);
                assertThat(UrlsRepository.findAll()).hasSize(0);
            });
        }
    }

    @Nested
    class UrlById {

        @Test
        @SneakyThrows
        @DisplayName("Should show url page by id from url.jte")
        public void testUrlPageByIdSuccess() {
            var urls = new Urls("https://example.com");
            UrlsRepository.save(urls);

            JavalinTest.test(app, (server, client) -> {
                var response = client.get("/urls/1");
                assertThat(response.code()).isEqualTo(200);
                assertNotNull(response.body());
                assertThat(response.body().string())
                        .contains("Сайт")
                        .contains("https://example.com");
            });
        }

        @Test
        @SneakyThrows
        @DisplayName("Should return 404 with error message on url page by id from url.jte")
        public void testUrlPageByIdNotFound() {
            JavalinTest.test(app, (server, client) -> {
                var response = client.get("/urls/1");
                assertThat(response.code()).isEqualTo(404);
                assertNotNull(response.body());
                assertThat(response.body().string())
                        .contains("Urls entity with id=1 not found!");
            });
        }
    }
}
