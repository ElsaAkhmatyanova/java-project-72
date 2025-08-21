package hexlet.code;

import hexlet.code.util.DataBaseInitializer;
import hexlet.code.config.JteEngineProvider;
import hexlet.code.repository.UrlsRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Map;

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
            ctx.render("index.jte", Map.of("name", "Elza"));
        });
        app.get("/url/list", ctx -> ctx.result(UrlsRepository.findAll().toString()));
        return app;
    }
}
