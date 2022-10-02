package account.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordWasHackedException extends RuntimeException{
    private String reqPath;
    public PasswordWasHackedException(String path){
        this.reqPath = path;
    }

    public String getReqPath() {
        return this.reqPath;
    }
}
