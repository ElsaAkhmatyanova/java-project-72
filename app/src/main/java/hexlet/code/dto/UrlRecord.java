package hexlet.code.dto;

import java.time.LocalDate;

public record UrlRecord(long id,
                        String name,
                        LocalDate lastCheck,
                        int code) {
}
