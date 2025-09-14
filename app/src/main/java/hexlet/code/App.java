package hexlet.code;

import hexlet.code.config.JteEngineProvider;
import hexlet.code.controller.MainController;
import hexlet.code.controller.UrlChecksController;
import hexlet.code.controller.UrlsController;
import hexlet.code.dto.ErrorResponse;
import hexlet.code.exception.NotFoundException;
import hexlet.code.util.DataBaseInitializer;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

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
        app.get("/", MainController::index);
        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.get(NamedRoutes.urlsPath("{id}"), UrlsController::findById);
        app.post(NamedRoutes.urlChecksPath("{id}"), UrlChecksController::create);

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse("Internal server error", e.getMessage()));
        });

        app.exception(NotFoundException.class, (e, ctx) -> {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(new ErrorResponse("Not found!", e.getMessage()));
        });

        return app;
    }
}
