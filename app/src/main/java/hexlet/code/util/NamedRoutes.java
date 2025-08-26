package hexlet.code.util;

public class NamedRoutes {

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlsPath(String id) {
        return "/urls/" + id;
    }

    public static String urlsPath(Long id) {
        return "/urls/" + id.toString();
    }

    public static String urlChecksPath(Long id) {
        return "/urls/" + id.toString() + "/checks";
    }

    public static String urlChecksPath(String id) {
        return "/urls/" + id + "/checks";
    }
}
