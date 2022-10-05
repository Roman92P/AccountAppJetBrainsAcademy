package account.app.service;

import account.app.model.SecurityEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SecurityEventService {

    List<SecurityEvent> getAllSecurityEvents();

    SecurityEvent createSecurityEvent(SecurityEvent securityEvent);

    SecurityEvent loginFailureSecurityEvent(SecurityEvent securityEvent, HttpServletRequest request);
}
