package hexlet.code.mapper;

import hexlet.code.dto.UrlRecord;
import hexlet.code.model.Urls;

public class UrlsMapper {

    public static UrlRecord mapToRecord(Urls urlsEntity) {
        return new UrlRecord(urlsEntity.getId(),
                urlsEntity.getName(),
                urlsEntity.getCreatedAt().toLocalDate(),
                200);
    }
}
