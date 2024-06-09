package project.bayaraja.application.exceptions.handling;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.bayaraja.application.exceptions.BadRequestException;
import project.bayaraja.application.exceptions.DataNotFoundException;
import project.bayaraja.application.exceptions.DuplicateDataException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    private record ExceptionDetails(List<String> message, HttpStatus httpStatus) { }

    @ExceptionHandler(value = {RuntimeException.class, UnsupportedOperationException.class, IllegalStateException.class})
    public ResponseEntity<?> internalServerError(Exception ex) {
        var exceptionDetails = new ExceptionDetails(
                Collections.singletonList(ex.getMessage()),
                INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(exceptionDetails, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<?> unauthorized(Exception ex) {
        var exceptionDetails = new ExceptionDetails(
                List.of(ex.getMessage()),
                UNAUTHORIZED
        );
        return new ResponseEntity<>(exceptionDetails, UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            DataNotFoundException.class,
            BadRequestException.class,
            MethodValidationException.class
    })
    public ResponseEntity<?> badRequest(Exception ex){
        var exceptionDetails = new ExceptionDetails(
                List.of(ex.getMessage()),
                BAD_REQUEST
        );
        return new ResponseEntity<>(exceptionDetails, BAD_REQUEST);
    }

    @ExceptionHandler(value = {DuplicateDataException.class})
    public ResponseEntity<?> conflict(Exception ex){
        var exceptionDetails = new ExceptionDetails(
                List.of(ex.getMessage()),
                CONFLICT
        );
        return new ResponseEntity<>(exceptionDetails, CONFLICT);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<?> forbiddenResource(Exception ex) {
        var exceptionDetails = new ExceptionDetails(
                List.of(ex.getMessage()),
                FORBIDDEN
        );
        return new ResponseEntity<>(exceptionDetails, FORBIDDEN);
    }
}
