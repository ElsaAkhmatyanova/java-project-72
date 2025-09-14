package hexlet.code.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UrlListPage extends BasePage {
    private List<UrlsWithCheckDto> urlsWithCheckDtoList;

    public UrlListPage(FlashMessage flashMessage, List<UrlsWithCheckDto> urlsWithCheckDtoList) {
        super(flashMessage);
        this.urlsWithCheckDtoList = urlsWithCheckDtoList;
    }
}
