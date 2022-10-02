package account.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException{

    private String requestPath;
    private String notExistingUserEmail;

    public UserNotFoundException(String requestPath, String notExistingUserEmail) {
        this.requestPath = requestPath;
        this.notExistingUserEmail = notExistingUserEmail;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getNotExistingUserEmail() {
        return notExistingUserEmail;
    }
}
