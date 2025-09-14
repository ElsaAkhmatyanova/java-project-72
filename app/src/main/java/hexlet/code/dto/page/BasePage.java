package hexlet.code.dto.page;

import hexlet.code.dto.FlashMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BasePage {
    protected FlashMessage flashMessage;
}
