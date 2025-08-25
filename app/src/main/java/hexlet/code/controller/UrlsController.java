package hexlet.code.controller;

import hexlet.code.dto.AlertType;
import hexlet.code.dto.FlashMessage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlRecord;
import hexlet.code.exception.EntityAlreadyExistException;
import hexlet.code.exception.NotFoundException;
import hexlet.code.exception.UrlParsingException;
import hexlet.code.mapper.UrlsMapper;
import hexlet.code.model.Urls;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.UrlUtil;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context ctx) {
        // TODO remove it later
        List<UrlRecord> urls = new ArrayList<>(List.of(
                new UrlRecord(1, "example.com", LocalDate.of(2025, 8, 21), 200),
                new UrlRecord(2, "google.com", LocalDate.of(2025, 8, 20), 200),
                new UrlRecord(3, "broken-site.com", LocalDate.of(2025, 8, 19), 404)
        ));
        List<UrlRecord> dbUrls = Collections.emptyList();
        try {
            dbUrls = UrlsRepository.findAll().stream()
                    .map(UrlsMapper::mapToRecord)
                    .toList();
        } catch (Exception e) {
            log.error("Exception while retrieving urls data from db");
        }
        urls.addAll(dbUrls);
        UrlPage page = new UrlPage(urls);
        ctx.render("page/urls.jte", model("page", page));
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
        String result = "";
        try {
            Long urlsId = ctx.pathParamAsClass("id", Long.class).get();
            log.info("Get urls by id: {}", urlsId);
            Urls urls = UrlsRepository.findById(urlsId)
                    .orElseThrow(() -> new NotFoundException("Urls entity with id " + urlsId + " not found!"));
            result = urls.toString();
        } catch (Exception e) {
            log.error("Exception while retrieving urls entity by id");
            result = e.getMessage();
        } finally {
            ctx.result(result);
        }
    }
}
