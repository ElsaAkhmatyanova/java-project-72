package hexlet.code;

import hexlet.code.config.JteEngineProvider;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlRecord;
import hexlet.code.util.DataBaseInitializer;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class App {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        try {
            DataBaseInitializer.initializeSchema();
        } catch (Exception e) {
            log.error("Exception while initialize schema", e);
        }
        var app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "8080");
        return Integer.parseInt(port);
    }

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(JteEngineProvider.createTemplateEngine()));
        });

        app.get("/", ctx -> {
            ctx.render("index.jte");
        });

        app.get(NamedRoutes.urlsPath(), ctx -> {
            List<UrlRecord> urls = List.of(
                    new UrlRecord(1, "example.com", LocalDate.of(2025, 8, 21), 200),
                    new UrlRecord(2, "google.com", LocalDate.of(2025, 8, 20), 200),
                    new UrlRecord(3, "broken-site.com", LocalDate.of(2025, 8, 19), 404)
            );
            UrlPage page = new UrlPage(urls);
            ctx.render("page/urls.jte", model("page", page));
        });

        return app;
    }
}
