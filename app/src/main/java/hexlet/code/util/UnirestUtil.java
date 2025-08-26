package hexlet.code.util;

import hexlet.code.exception.UnirestFetchException;
import hexlet.code.model.UrlChecks;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UnirestUtil {

    public static UrlChecks fetchPageInfoIntoUrlChecks(String url) throws UnirestFetchException {
        Map<String, String> result = new HashMap<>();
        try {
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
        } catch (Exception e) {
            log.error("Exception while calling url: {}", url, e);
            throw new UnirestFetchException(e.getMessage(), e);
        }
    }
}
