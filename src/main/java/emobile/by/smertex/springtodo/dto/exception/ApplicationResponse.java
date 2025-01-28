package emobile.by.smertex.springtodo.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Schema(description = "Статус запроса, зачастую находится в обертке")
public record ApplicationResponse(String massage,
                                  HttpStatusCode httpStatusCode,
                                  LocalDateTime occurrenceTime) {
}
