package hexlet.code.dto;

public record UrlCheckInfoDto(long id,
                              int code,
                              String title,
                              String h1,
                              String description,
                              String createdAt) {
}
