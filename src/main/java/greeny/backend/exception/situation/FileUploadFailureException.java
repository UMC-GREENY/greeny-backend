package greeny.backend.exception.situation;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(String message) {
        super(message);
    }
}
