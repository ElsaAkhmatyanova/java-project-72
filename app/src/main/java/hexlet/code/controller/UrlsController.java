package hexlet.code.controller;

import hexlet.code.dto.AlertType;
import hexlet.code.dto.FlashMessage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlRecord;
import hexlet.code.util.UrlUtil;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context ctx) {
        List<UrlRecord> urls = List.of(
                new UrlRecord(1, "example.com", LocalDate.of(2025, 8, 21), 200),
                new UrlRecord(2, "google.com", LocalDate.of(2025, 8, 20), 200),
                new UrlRecord(3, "broken-site.com", LocalDate.of(2025, 8, 19), 404)
        );
        UrlPage page = new UrlPage(urls);
        ctx.render("page/urls.jte", model("page", page));
    }

    public static void create(Context ctx) {
        String inputUrl = ctx.formParam("url");
        log.info("Get url value from form: {}", inputUrl);
        String formattedUrl = null;
        try {
            formattedUrl = UrlUtil.parseUrlToDbFormat(inputUrl);
        } catch (Exception e) {
            log.error("Exception while format URL", e);
            FlashMessage flashMessage = new FlashMessage(AlertType.DANGER, "Некорректный URL!");
            ctx.sessionAttribute("flash", flashMessage);
        }
        log.info("Formatted url: {}", formattedUrl);
        FlashMessage flashMessage = new FlashMessage(AlertType.SUCCESS, "Страница успешно добавлена");
        ctx.sessionAttribute("flash", flashMessage);
        ctx.redirect("/");
    }
}
