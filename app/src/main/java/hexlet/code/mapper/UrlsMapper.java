package hexlet.code.mapper;

import hexlet.code.dto.UrlRecord;
import hexlet.code.model.Urls;

import java.time.format.DateTimeFormatter;

public class UrlsMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static UrlRecord mapToRecord(Urls urlsEntity) {
        return new UrlRecord(urlsEntity.getId(),
                urlsEntity.getName(),
                urlsEntity.getCreatedAt() != null ? DATE_TIME_FORMATTER.format(urlsEntity.getCreatedAt()) : "",
                200);
    }
}
