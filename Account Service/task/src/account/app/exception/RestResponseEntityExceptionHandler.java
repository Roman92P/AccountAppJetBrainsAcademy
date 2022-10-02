package account.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NegativeSalaryException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleNegativeSalaryException(NegativeSalaryException ex, WebRequest request){
        ExceptionMessageResponse exceptionMessageResponse = new ExceptionMessageResponse();
        exceptionMessageResponse.setTimestamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX)));
        exceptionMessageResponse.setStatus(BAD_REQUEST.value());
        exceptionMessageResponse.setError("Bad Request");
        exceptionMessageResponse.setMessage("Salary should be greater than zero!");
        exceptionMessageResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        return handleExceptionInternal(ex, exceptionMessageResponse,
                new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        ExceptionMessageResponse exceptionMessageResponse = new ExceptionMessageResponse();
        exceptionMessageResponse.setTimestamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX)));
        exceptionMessageResponse.setStatus(BAD_REQUEST.value());
        exceptionMessageResponse.setError("Bad Request");
        exceptionMessageResponse.setMessage("Couldn't find user with email "+ex.getNotExistingUserEmail());
        exceptionMessageResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        return handleExceptionInternal(ex, exceptionMessageResponse,
                new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {UserExistException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleUserExistException(UserExistException ex, WebRequest request){
        ExceptionMessageResponse exceptionMessageResponse = new ExceptionMessageResponse();
        exceptionMessageResponse.setTimestamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX)));
        exceptionMessageResponse.setStatus(BAD_REQUEST.value());
        exceptionMessageResponse.setError("Bad Request");
        exceptionMessageResponse.setMessage("User exist!");
        exceptionMessageResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        return handleExceptionInternal(ex, exceptionMessageResponse,
                new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {PasswordWasHackedException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleUserPswdWasHacked(PasswordWasHackedException ex, WebRequest request){
        ExceptionMessageResponse exceptionMessageResponse = new ExceptionMessageResponse();
        exceptionMessageResponse.setTimestamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX)));
        exceptionMessageResponse.setStatus(BAD_REQUEST.value());
        exceptionMessageResponse.setError("Bad Request");
        exceptionMessageResponse.setMessage("The password is in the hacker's database!");
        exceptionMessageResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        return handleExceptionInternal(ex, exceptionMessageResponse,
                new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {UserChangesPasswordSamePasswordException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleUserSamePaswd(UserChangesPasswordSamePasswordException ex, WebRequest request){
        ExceptionMessageResponse exceptionMessageResponse = new ExceptionMessageResponse();
        exceptionMessageResponse.setTimestamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX)));
        exceptionMessageResponse.setStatus(BAD_REQUEST.value());
        exceptionMessageResponse.setError("Bad Request");
        exceptionMessageResponse.setMessage("The passwords must be different!");
        exceptionMessageResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        return handleExceptionInternal(ex, exceptionMessageResponse,
                new HttpHeaders(), BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String errorMessage = fieldErrors.get(0).getDefaultMessage();
        ExceptionMessageResponse exceptionMessageResponse = new ExceptionMessageResponse();
        exceptionMessageResponse.setTimestamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.MAX)));
        exceptionMessageResponse.setStatus(BAD_REQUEST.value());
        exceptionMessageResponse.setError("Bad Request");
        exceptionMessageResponse.setMessage(errorMessage);
        exceptionMessageResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        return ResponseEntity.badRequest().body(exceptionMessageResponse);
        //return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}
