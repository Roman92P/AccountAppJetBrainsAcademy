package account.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserExistException.class})
    protected ResponseEntity<Object> handleUserExistException(UserExistException ex, WebRequest request){
        ExceptionMessageResponse exceptionMessageResponse = new ExceptionMessageResponse();
        exceptionMessageResponse.setTimestamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX)));
        exceptionMessageResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionMessageResponse.setError("Bad Request");
        exceptionMessageResponse.setMessage("User exist!");
        exceptionMessageResponse.setPath("/api/auth/singup");
        return handleExceptionInternal(ex, exceptionMessageResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
