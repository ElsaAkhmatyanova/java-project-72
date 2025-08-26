package hexlet.code.exception;

public class UnirestFetchException extends RuntimeException {

    public UnirestFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
