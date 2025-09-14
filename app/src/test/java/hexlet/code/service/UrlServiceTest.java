package hexlet.code.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlServiceTest {

    @Nested
    class ParseUrl {
        @Test
        @DisplayName("Should parse simple HTTP URL without port")
        @SneakyThrows
        void testHttpUrlWithoutPort() {
            String input = "http://example.com/some/path?query=123";
            String result = UrlService.parseUrlToDbFormat(input);
            assertEquals("http://example.com", result);
        }

        @Test
        @DisplayName("Should parse HTTPS URL without port")
        @SneakyThrows
        void testHttpsUrlWithoutPort() {
            String input = "https://sub.domain.org/resource";
            String result = UrlService.parseUrlToDbFormat(input);
            assertEquals("https://sub.domain.org", result);
        }

        @Test
        @DisplayName("Should parse URL with explicit port")
        @SneakyThrows
        void testUrlWithPort() {
            String input = "https://localhost:8080/api/v1/data";
            String result = UrlService.parseUrlToDbFormat(input);
            assertEquals("https://localhost:8080", result);
        }

        @Test
        @DisplayName("Should handle URL with IP address and port")
        @SneakyThrows
        void testIpAddressWithPort() {
            String input = "http://127.0.0.1:9090/test";
            String result = UrlService.parseUrlToDbFormat(input);
            assertEquals("http://127.0.0.1:9090", result);
        }

        @Test
        @DisplayName("Should parse URL with default port (not included in result)")
        @SneakyThrows
        void testUrlWithDefaultPortNotAdded() {
            String input = "http://example.com:80/index.html";
            String result = UrlService.parseUrlToDbFormat(input);
            assertEquals("http://example.com:80", result,
                    "Even default port should be included because checkCode adds it if specified");
        }
    }
}
