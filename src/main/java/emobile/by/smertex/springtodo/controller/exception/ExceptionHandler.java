package emobile.by.smertex.springtodo.controller.exception;

import emobile.by.smertex.springtodo.dto.exception.ApplicationResponse;
import emobile.by.smertex.springtodo.util.ResponseMessage;
import jakarta.annotation.Nullable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Класс-обработчик ошибок. При возникновении возвращает пользователю ответ в виде JSON
 */
@RestControllerAdvice(basePackages = "by.smertex.controller.realisation")
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        Map<String, String> errors = exception.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApplicationResponse> repositoryUniqueException(DataIntegrityViolationException exception){
        return ResponseEntity.badRequest()
                .body(new ApplicationResponse(ResponseMessage.SAVE_FAILED_DUE_DUPLICATE, HttpStatus.CONFLICT, LocalDateTime.now()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApplicationResponse> incorrectData(NoSuchElementException exception){
        return ResponseEntity.badRequest()
                .body(new ApplicationResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundInDatabaseException.class)
    public ResponseEntity<ApplicationResponse> userNotFoundInDatabase(UserNotFoundInDatabaseException exception){
        return ResponseEntity.badRequest()
                .body(new ApplicationResponse(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }
}
