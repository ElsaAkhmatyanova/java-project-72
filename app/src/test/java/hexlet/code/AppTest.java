package hexlet.code;

import hexlet.code.model.UrlChecks;
import hexlet.code.model.Urls;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.DataBaseInitializer;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {

    private Javalin app;
    private static MockWebServer mockServer;
    private static String mockUrl;

    @BeforeAll
    public static void beforeAll() throws Exception {
        Path path = Paths.get("src/test/resources/fixtures/mock_page.html").toAbsolutePath().normalize();
        var mockBody = Files.readString(path);
        mockServer = new MockWebServer();
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(mockBody);
        mockServer.enqueue(mockResponse);
        mockServer.start();
        mockUrl = mockServer.url("/").toString();
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        DataBaseInitializer.initializeSchema();
        app = App.getApp();
    }

    @AfterAll
    @SneakyThrows
    public static void afterAll() {
        mockServer.shutdown();
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
        @DisplayName("Should show url page by id from url.jte without checks")
        public void testUrlPageByIdWithoutChecksSuccess() {
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
        @DisplayName("Should show url page by id from url.jte with checks")
        public void testUrlPageByIdWithChecksSuccess() {
            var urls = new Urls("https://example.com");
            UrlsRepository.save(urls);
            var urlChecks = new UrlChecks(
                    urls.getId(),
                    200,
                    "title 1",
                    "h 1 1",
                    "description 1"
            );
            UrlChecksRepository.save(urlChecks);

            JavalinTest.test(app, (server, client) -> {
                var response = client.get("/urls/1");
                assertThat(response.code()).isEqualTo(200);
                assertNotNull(response.body());
                assertThat(response.body().string())
                        .contains("Сайт")
                        .contains("https://example.com")
                        .contains("Проверки")
                        .contains("title 1");
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

    @Nested
    class CreateUrlCheck {

        @Test
        @SneakyThrows
        @DisplayName("Should create url check successfully")
        public void testCreateUrlCheckSuccess() {
            var urls = new Urls(mockUrl);
            UrlsRepository.save(urls);

            JavalinTest.test(app, (server, client) -> {
                var response = client.post("/urls/" + urls.getId() + "/checks");
                assertThat(response.code()).isEqualTo(200);

                var savedChecks = UrlChecksRepository.findAllByUrlId(urls.getId());
                assertThat(savedChecks).hasSize(1)
                        .first()
                        .satisfies(urlChecks -> assertEquals("Mock Page", urlChecks.getTitle()))
                        .satisfies(urlChecks -> assertEquals("Welcome to Mock Page", urlChecks.getH1()))
                        .satisfies(
                                urlChecks -> {
                                    assertEquals(
                                            "This is a mock HTML page with h1, title and description.",
                                            urlChecks.getDescription()
                                    );
                                });

            });
        }

        @Test
        @SneakyThrows
        @DisplayName("Should return error flash when url not found")
        public void testCreateUrlCheckNotFound() {
            JavalinTest.test(app, (server, client) -> {
                var response = client.post("/urls/1/checks");
                var savedChecks = UrlChecksRepository.findAllByUrlId(1L);
                assertThat(savedChecks).isEmpty();
            });
        }
    }
}
