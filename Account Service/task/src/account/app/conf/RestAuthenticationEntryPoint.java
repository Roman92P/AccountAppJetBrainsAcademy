package account.app.conf;

import account.app.model.EventName;
import account.app.model.SecurityEvent;
import account.app.service.SecurityEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    SecurityEventService securityEventService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
        Optional<String> authHeader = Optional.ofNullable(request.getHeader("Authorization"));
        String userEmail = "";
        if (authHeader.isPresent()) {
            String authorization = request.getHeader("Authorization");
            String creds = authorization.substring("Basic".length()).trim();
            byte[] decoded = Base64.getDecoder().decode(creds);
            String credsStrUserPaswd = new String(decoded, StandardCharsets.UTF_8);
            String[] upArr = credsStrUserPaswd.split(":");
            userEmail = upArr[0];
            if (!authException.getMessage().equals("User account is locked"))
                securityEventService.loginFailureSecurityEvent(new SecurityEvent(EventName.LOGIN_FAILED, userEmail, request.getServletPath(), request.getServletPath()), request);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

}
