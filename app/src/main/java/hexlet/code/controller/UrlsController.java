package hexlet.code.controller;

import hexlet.code.dto.AlertType;
import hexlet.code.dto.FlashMessage;
import hexlet.code.dto.UrlListPage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsWithCheckDto;
import hexlet.code.exception.NotFoundException;
import hexlet.code.mapper.UrlsMapper;
import hexlet.code.model.UrlChecks;
import hexlet.code.model.Urls;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.repository.projection.UrlsWithCheckProjection;
import hexlet.code.util.UrlUtil;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context ctx) throws Exception {
        List<UrlsWithCheckProjection> urlList = UrlsRepository.findAllWithLatestCheck();
        List<UrlsWithCheckDto> urlsWithCheckDtoList = urlList.stream()
                .map(UrlsMapper::mapToDto)
                .toList();
        FlashMessage flash = ctx.consumeSessionAttribute("flash");
        UrlListPage page = new UrlListPage(flash, urlsWithCheckDtoList);
        ctx.render("page/url_list.jte", model("page", page));
    }

    public static void create(Context ctx) throws Exception {
        String inputUrl = ctx.formParam("url");
        log.info("Get url value from form: {}", inputUrl);
        String formattedUrl;
        try {
            formattedUrl = UrlUtil.parseUrlToDbFormat(inputUrl);
            log.info("Formatted url: {}", formattedUrl);
        } catch (Exception e) {
            log.error("Exception while format URL", e);
            FlashMessage flashMessage = new FlashMessage(AlertType.DANGER, "Некорректный URL!");
            ctx.sessionAttribute("flash", flashMessage);
            ctx.redirect("/");
            return;
        }

        boolean isExisted = UrlsRepository.isExistByName(formattedUrl);
        if (isExisted) {
            log.error("Entity urls already exist with this name!");
            FlashMessage flashMessage = new FlashMessage(AlertType.WARNING, "Страница уже существует");
            ctx.sessionAttribute("flash", flashMessage);
            ctx.redirect("/");
        } else {
            UrlsRepository.save(new Urls(formattedUrl));
            FlashMessage flashMessage = new FlashMessage(AlertType.SUCCESS, "Страница успешно добавлена");
            ctx.sessionAttribute("flash", flashMessage);
            ctx.redirect("/urls");
        }
    }

    public static void findById(Context ctx) throws Exception {
        Long urlsId = ctx.pathParamAsClass("id", Long.class).get();
        log.info("Get urls by id: {}", urlsId);
        Urls urls = UrlsRepository.findById(urlsId)
                .orElseThrow(() -> new NotFoundException("Urls entity with id=" + urlsId + " not found!"));
        List<UrlChecks> urlChecks = UrlChecksRepository.findAllByUrlId(urlsId);
        FlashMessage flash = ctx.consumeSessionAttribute("flash");
        UrlPage page = new UrlPage(
                flash,
                UrlsMapper.mapToDto(urls),
                urlChecks.stream().map(UrlsMapper::mapToDto).toList()
        );
        ctx.render("page/url.jte", model("page", page));
    }
}
