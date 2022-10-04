package account.app.service;

import account.app.model.SecurityEvent;

import java.util.List;

public interface SecurityEventService {

    List<SecurityEvent> getAllSecurityEvents();

    SecurityEvent createSecurityEvent(SecurityEvent securityEvent);
}
