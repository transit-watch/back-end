package transit.transitwatch.exception;

import lombok.Getter;
import transit.transitwatch.util.ErrorCode;

@Getter
public class ApiRequestException extends RuntimeException {
    private ErrorCode errorCode;
    public ApiRequestException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
    }
}
