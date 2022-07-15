package MyProjectGradle.models.exeptions;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class})
    public String entityNotFoundHandler(RuntimeException e) {
        return "errors/error404";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataIntegrityViolationException.class})
    public String conflict(DataIntegrityViolationException e) {
        return "errors/error400";
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError(Throwable e) throws Throwable {
        return "errors/error403";
    }

    @ExceptionHandler(Throwable.class)
    public String handleError(Throwable throwable) throws Throwable {
        return "errors/error404";
    }

    @ExceptionHandler({AccessDeniedException.class})
    public String handleAccessDeniedException(
            Exception ex) {
        return "errors/error403";
    }
}
