package hexlet.code.mapper;

import hexlet.code.dto.UrlCheckInfoDto;
import hexlet.code.dto.UrlInfoDto;
import hexlet.code.dto.UrlsWithCheckDto;
import hexlet.code.model.UrlChecks;
import hexlet.code.model.Urls;
import hexlet.code.repository.projection.UrlsWithCheckProjection;

import java.time.format.DateTimeFormatter;

public class UrlsMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static UrlInfoDto mapToDto(Urls entity) {
        return new UrlInfoDto(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt() != null ? DATE_TIME_FORMATTER.format(entity.getCreatedAt()) : ""
        );
    }

    public static UrlsWithCheckDto mapToDto(UrlsWithCheckProjection urlWithCheck) {
        return new UrlsWithCheckDto(
                urlWithCheck.id(),
                urlWithCheck.name(),
                urlWithCheck.lastCheck() != null
                        ? DATE_TIME_FORMATTER.format(urlWithCheck.lastCheck())
                        : null,
                urlWithCheck.checkCode()
        );
    }

    public static UrlCheckInfoDto mapToDto(UrlChecks entity) {
        return new UrlCheckInfoDto(
                entity.getId(),
                entity.getStatusCode(),
                entity.getTitle(),
                entity.getH1(),
                entity.getDescription(),
                entity.getCreatedAt() != null ? DATE_TIME_FORMATTER.format(entity.getCreatedAt()) : ""
        );
    }
}
