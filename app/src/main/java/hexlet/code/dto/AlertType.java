package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertType {
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info");

    private final String bootstrapClass;
}
