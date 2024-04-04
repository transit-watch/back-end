package transit.transitwatch.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({"success", "error", "result"})
@NoArgsConstructor
@Getter
public class Response<T> {

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> Response<T> ok(T result) {
        return Response.<T>builder()
                .success(true)
                .result(result)
                .build();
    }

    public static Response<?> error(String message) {
        return Response.builder()
                .success(false)
                .error(message)
                .build();
    }

    @Builder
    public Response(boolean success, String error, T result) {
        this.success = success;
        this.error = error;
        this.result = result;
    }
}
