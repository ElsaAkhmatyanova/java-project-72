package hexlet.code.dto;

public record UrlsWithCheckDto(long id,
                               String name,
                               String lastCheck,
                               Integer code) {
}
