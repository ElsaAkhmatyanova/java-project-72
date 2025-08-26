package hexlet.code.exception;

public class UrlParsingException extends RuntimeException {

    public UrlParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
