package hexlet.code.controller;

import hexlet.code.dto.AlertType;
import hexlet.code.dto.FlashMessage;
import hexlet.code.dto.UrlListPage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlRecord;
import hexlet.code.exception.EntityAlreadyExistException;
import hexlet.code.exception.UrlParsingException;
import hexlet.code.mapper.UrlsMapper;
import hexlet.code.model.Urls;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.UrlUtil;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context ctx) {
        List<UrlRecord> urls = Collections.emptyList();
        try {
            urls = UrlsRepository.findAll().stream()
                    .map(UrlsMapper::mapToRecord)
                    .toList();
        } catch (Exception e) {
            log.error("Exception while retrieving urls data from db");
        }
        UrlListPage page = new UrlListPage(urls);
        ctx.render("page/url_list.jte", model("page", page));
    }

    public static void create(Context ctx) {
        try {
            String inputUrl = ctx.formParam("url");
            log.info("Get url value from form: {}", inputUrl);
            String formattedUrl = UrlUtil.parseUrlToDbFormat(inputUrl);
            log.info("Formatted url: {}", formattedUrl);

            boolean isExisted = UrlsRepository.isExistByName(formattedUrl);
            if (isExisted) {
                log.error("Entity urls already exist with name : {}", formattedUrl);
                throw new EntityAlreadyExistException("Entity urls already exist with same name!");
            }

            UrlsRepository.save(new Urls(formattedUrl));
            FlashMessage flashMessage = new FlashMessage(AlertType.SUCCESS, "Страница успешно добавлена");
            ctx.sessionAttribute("flash", flashMessage);
        } catch (UrlParsingException e) {
            log.error("Exception while format URL", e);
            FlashMessage flashMessage = new FlashMessage(AlertType.DANGER, "Некорректный URL!");
            ctx.sessionAttribute("flash", flashMessage);
        } catch (EntityAlreadyExistException e) {
            FlashMessage flashMessage = new FlashMessage(AlertType.WARNING, "Страница уже существует");
            ctx.sessionAttribute("flash", flashMessage);
        } catch (Exception e) {
            log.error("Exception while interaction with db!");
            FlashMessage flashMessage = new FlashMessage(AlertType.DANGER, "Ошибка сервера!");
            ctx.sessionAttribute("flash", flashMessage);
        } finally {
            ctx.redirect("/");
        }
    }

    public static void findById(Context ctx) {
        try {
            Long urlsId = ctx.pathParamAsClass("id", Long.class).get();
            log.info("Get urls by id: {}", urlsId);
            Urls urls = UrlsRepository.findById(urlsId)
                    .orElseThrow(() -> new NotFoundResponse("Urls entity with id " + urlsId + " not found!"));
            UrlPage page = new UrlPage(UrlsMapper.mapToRecord(urls));
            ctx.render("page/url.jte", model("page", page));
        } catch (NotFoundResponse e) {
            log.error("NotFoundResponse exception!", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while retrieving urls entity by id", e);
            throw new InternalServerErrorResponse("Ошибка сервера!");
        }
    }
}
