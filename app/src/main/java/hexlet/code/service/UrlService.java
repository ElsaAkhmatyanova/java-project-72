package hexlet.code.service;

import hexlet.code.model.UrlChecks;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URL;

@Slf4j
public class UrlService {

    public static UrlChecks fetchPageInfoIntoUrlChecks(String url) {
        HttpResponse<String> response = Unirest.get(url).asString();
        log.info("Fetch info from url: {}, response: {}", url, response);
        int statusCode = response.getStatus();

        Document doc = Jsoup.parse(response.getBody());

        String title = doc.title();

        Element h1 = doc.selectFirst("h1");
        String h1Text = h1 != null ? h1.text() : "";

        Element metaDescription = doc.selectFirst("meta[name=description]");
        String description = metaDescription != null ? metaDescription.attr("content") : "";

        return UrlChecks.builder()
                .statusCode(statusCode)
                .title(title)
                .h1(h1Text)
                .description(description)
                .build();
    }

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
