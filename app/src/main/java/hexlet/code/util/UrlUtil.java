package hexlet.code.util;

import java.net.URI;
import java.net.URL;

public class UrlUtil {

    public static String parseUrlToDbFormat(String inputUrl) throws Exception {
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
    }
}
