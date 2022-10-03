package account.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AcctUserNotFoundException extends RuntimeException{

    public AcctUserNotFoundException() {

    }

}
