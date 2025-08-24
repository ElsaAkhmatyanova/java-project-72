package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlashMessage {
    private final AlertType type;
    private final String message;
}
