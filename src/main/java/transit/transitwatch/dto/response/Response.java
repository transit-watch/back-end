package transit.transitwatch.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import transit.transitwatch.util.ErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"success", "status", "code", "fail", "error", "result"})
@NoArgsConstructor
@Getter
public class Response<T> {
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final String ERROR = "error";

    private boolean success;
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> Response<T> ok(T result) {
        return Response.<T>builder()
                .success(true)
                .status(SUCCESS)
                .result(result)
                .build();
    }

    public static Response<?> error(String message, String errorCode) {
        return Response.builder()
                .success(false)
                .status(ERROR)
                .code(errorCode)
                .error(message)
                .build();
    }

    public static Response<?> fail(BindingResult bindingResult, ErrorCode errorCode) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(error -> {
            if (error instanceof FieldError)
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            else
                errors.put(error.getObjectName(), error.getDefaultMessage());
        });

        return Response.builder()
                .success(false)
                .status(FAIL)
                .code(errorCode.getCode())
                .result(errors)
                .build();
    }

    public static Response<?> fail(Map<String, ?> errors, ErrorCode errorCode) {
        return Response.builder()
                .success(false)
                .code(errorCode.getCode())
                .status(FAIL)
                .result(errors)
                .build();
    }

    @Builder
    public Response(boolean success, String status, String code, String fail, String error, T result) {
        this.success = success;
        this.status = status;
        this.code = code;
        this.fail = fail;
        this.error = error;
        this.result = result;
    }

    private static void accept(String key, String value) {
        System.out.println(key + ": " + value);
    }
}
