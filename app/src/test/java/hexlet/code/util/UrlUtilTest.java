package hexlet.code.util;

import hexlet.code.exception.UrlParsingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlUtilTest {

    @Test
    @DisplayName("Should parse simple HTTP URL without port")
    void testHttpUrlWithoutPort() {
        String input = "http://example.com/some/path?query=123";
        String result = UrlUtil.parseUrlToDbFormat(input);
        assertEquals("http://example.com", result);
    }

    @Test
    @DisplayName("Should parse HTTPS URL without port")
    void testHttpsUrlWithoutPort() {
        String input = "https://sub.domain.org/resource";
        String result = UrlUtil.parseUrlToDbFormat(input);
        assertEquals("https://sub.domain.org", result);
    }

    @Test
    @DisplayName("Should parse URL with explicit port")
    void testUrlWithPort() {
        String input = "https://localhost:8080/api/v1/data";
        String result = UrlUtil.parseUrlToDbFormat(input);
        assertEquals("https://localhost:8080", result);
    }

    @Test
    @DisplayName("Should handle URL with IP address and port")
    void testIpAddressWithPort() {
        String input = "http://127.0.0.1:9090/test";
        String result = UrlUtil.parseUrlToDbFormat(input);
        assertEquals("http://127.0.0.1:9090", result);
    }

    @Test
    @DisplayName("Should parse URL with default port (not included in result)")
    void testUrlWithDefaultPortNotAdded() {
        String input = "http://example.com:80/index.html";
        String result = UrlUtil.parseUrlToDbFormat(input);
        assertEquals("http://example.com:80", result,
                "Even default port should be included because checkCode adds it if specified");
    }

    @Test
    @DisplayName("Should throw UrlParsingException for completely invalid string")
    void testInvalidString() {
        String badUrl = "not-a-url";
        UrlParsingException ex = assertThrows(
                UrlParsingException.class,
                () -> UrlUtil.parseUrlToDbFormat(badUrl)
        );
        assertNotNull(ex.getMessage());
    }
}
