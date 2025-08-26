package hexlet.code.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UrlPage extends BasePage {
    private UrlInfoDto urlInfoDto;
    private List<UrlCheckInfoDto> urlCheckInfoDtoList;

    public UrlPage(FlashMessage flashMessage, UrlInfoDto urlInfoDto, List<UrlCheckInfoDto> urlCheckInfoDtoList) {
        super(flashMessage);
        this.urlInfoDto = urlInfoDto;
        this.urlCheckInfoDtoList = urlCheckInfoDtoList;
    }
}
