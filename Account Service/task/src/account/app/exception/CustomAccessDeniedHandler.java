package account.app.exception;

import account.app.model.EventName;
import account.app.model.SecurityEvent;
import account.app.service.SecurityEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    SecurityEventService securityEventService;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        securityEventService.createSecurityEvent(new SecurityEvent(EventName.ACCESS_DENIED, request.getRemoteUser(), request.getServletPath(), request.getServletPath()));
        response.sendError(403, "Access Denied!");
    }
}
