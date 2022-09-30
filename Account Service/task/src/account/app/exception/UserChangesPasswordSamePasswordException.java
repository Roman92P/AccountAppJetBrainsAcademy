package account.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserChangesPasswordSamePasswordException extends RuntimeException{
    private final String reqPath;

    public UserChangesPasswordSamePasswordException(String reqPath){
        this.reqPath = reqPath;
    }

    public String getReqPath() {
        return reqPath;
    }
}
