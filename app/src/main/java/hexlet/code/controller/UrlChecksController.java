package hexlet.code.controller;

import hexlet.code.dto.AlertType;
import hexlet.code.dto.FlashMessage;
import hexlet.code.exception.NotFoundException;
import hexlet.code.model.UrlChecks;
import hexlet.code.model.Urls;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UnirestUtil;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlChecksController {

    public static void create(Context ctx) throws Exception {
        Long urlsId = ctx.pathParamAsClass("id", Long.class).get();
        log.info("Check url by id: {}", urlsId);
        Urls urls = UrlsRepository.findById(urlsId)
                .orElseThrow(() -> new NotFoundException("Urls entity with id=" + urlsId + " not found!"));
        UrlChecks urlChecks;
        try {
            urlChecks = UnirestUtil.fetchPageInfoIntoUrlChecks(urls.getName());
        } catch (Exception e) {
            log.error("Exception while calling url: {}", urls.getName(), e);
            FlashMessage flashMessage = new FlashMessage(AlertType.DANGER, "Ошибка получения данных по URL!");
            ctx.sessionAttribute("flash", flashMessage);
            ctx.redirect(NamedRoutes.urlsPath(urlsId));
            return;
        }

        urlChecks.setUrlId(urlsId);
        UrlChecksRepository.save(urlChecks);
        FlashMessage flashMessage = new FlashMessage(AlertType.SUCCESS, "Страница успешно проверена");
        ctx.sessionAttribute("flash", flashMessage);
        ctx.redirect(NamedRoutes.urlsPath(urlsId));
    }
}
