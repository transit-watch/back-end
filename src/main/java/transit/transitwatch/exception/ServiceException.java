package transit.transitwatch.exception;

import lombok.Getter;
import transit.transitwatch.util.ErrorCode;

@Getter
public class ServiceException extends RuntimeException {

    private ErrorCode errorCode;

    public ServiceException(String message) {
        super(message);
        this.errorCode = null;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    public ServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}