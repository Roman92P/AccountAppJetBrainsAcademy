package account.app.service;

import account.app.model.SecurityEvent;
import account.app.ropository.SecurityEventRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityEventServiceImpl implements SecurityEventService{

    private final SecurityEventRepo securityEventRepo;

    public SecurityEventServiceImpl(SecurityEventRepo securityEventRepo) {
        this.securityEventRepo = securityEventRepo;
    }

    @Override
    public List<SecurityEvent> getAllSecurityEvents() {
        return securityEventRepo.findAll();
    }
}
