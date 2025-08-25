package hexlet.code.dto;

public record UrlRecord(long id,
                        String name,
                        String lastCheck,
                        int code) {
}
