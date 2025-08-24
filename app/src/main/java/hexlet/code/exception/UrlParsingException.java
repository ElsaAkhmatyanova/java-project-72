package hexlet.code.exception;

public class UrlParsingException extends RuntimeException {

    public UrlParsingException() {
    }

    public UrlParsingException(String message) {
        super(message);
    }

    public UrlParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
