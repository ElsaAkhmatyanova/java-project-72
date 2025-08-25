package hexlet.code.util;

import hexlet.code.exception.UrlParsingException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlUtil {

    public static String parseUrlToDbFormat(String inputUrl) throws UrlParsingException {
        try {
            URI uri = new URI(inputUrl);
            URL url = uri.toURL();
            StringBuilder baseUrl = new StringBuilder();
            baseUrl.append(url.getProtocol())
                    .append("://")
                    .append(url.getHost());
            if (url.getPort() != -1) {
                baseUrl.append(":").append(url.getPort());
            }
            return baseUrl.toString();
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            throw new UrlParsingException(e.getMessage(), e);
        }
    }
}
