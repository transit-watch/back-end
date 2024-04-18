package transit.transitwatch.exception;

import lombok.Getter;
import transit.transitwatch.util.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
