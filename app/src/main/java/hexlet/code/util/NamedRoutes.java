package hexlet.code.util;

public class NamedRoutes {

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlsPath(String id) {
        return "/urls/" + id;
    }
}
