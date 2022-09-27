package account.app.exception;

import org.springframework.stereotype.Component;

@Component
public class ExceptionMessageResponse {

    private String timestamp;
    private int status;
    private String error;
    private String message;
}
