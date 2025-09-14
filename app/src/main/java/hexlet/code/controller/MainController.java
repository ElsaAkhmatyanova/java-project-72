package hexlet.code.controller;

import hexlet.code.dto.FlashMessage;
import hexlet.code.dto.page.MainPage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class MainController {

    public static void index(Context ctx) {
        FlashMessage flash = ctx.consumeSessionAttribute("flash");
        MainPage page = new MainPage(flash);
        ctx.render("index.jte", model("page", page));
    }
}
