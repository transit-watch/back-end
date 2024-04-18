package transit.transitwatch.exception.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import transit.transitwatch.dto.response.Response;
import transit.transitwatch.exception.CustomException;
import transit.transitwatch.exception.ServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static transit.transitwatch.util.ErrorCode.BAD_REQUEST;
import static transit.transitwatch.util.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public Response<?> handleConstraintViolationException(ConstraintViolationException ex) {

        Map<String, List<String>> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldKey = violation.getPropertyPath().toString();
            errors.computeIfAbsent(fieldKey, k -> new ArrayList<>()).add(violation.getMessage());
        }
        return Response.fail(errors, BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return Response.fail(bindingResult, BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ServiceException.class, CustomException.class})
    public Response<?> handleCustomServiceException(ServiceException ex) {
        return Response.error("Internal Server Error: " + ex.getErrorCode().getDescription(), ex.getErrorCode().getCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception ex) {
        return Response.error("Internal Server Error!: " + ex.getMessage(), INTERNAL_SERVER_ERROR.getCode());
    }
}
