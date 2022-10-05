package account.app.service;

import account.app.model.*;
import account.app.ropository.SecurityEventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityEventServiceImpl implements SecurityEventService {

    private final SecurityEventRepo securityEventRepo;

    private final UserFailLoginCounterService userFailLoginCounterService;
    @Autowired
    AcctUserService acctUserService;

    public SecurityEventServiceImpl(SecurityEventRepo securityEventRepo, UserFailLoginCounterService userFailLoginCounterService) {
        this.securityEventRepo = securityEventRepo;
        this.userFailLoginCounterService = userFailLoginCounterService;
    }

    @Override
    public List<SecurityEvent> getAllSecurityEvents() {
        List<SecurityEvent> all = securityEventRepo.findAll();
//        List<SecurityEvent> collect = all.stream().filter(securityEvent -> securityEvent.getAction() != EventName.GRANT_ROLE).collect(Collectors.toList());
        return all;
    }

    @Override
    public SecurityEvent createSecurityEvent(SecurityEvent securityEvent) {
        return securityEventRepo.save(securityEvent);
    }

    @Override
    public SecurityEvent loginFailureSecurityEvent(SecurityEvent securityEvent, HttpServletRequest request) {
        Optional<UserFailLoginCounter> userPrevLoginFails = userFailLoginCounterService.findUserPrevLoginFails(securityEvent.getSubject());
        SecurityEvent saved = securityEventRepo.save(securityEvent);
        if (userPrevLoginFails.isPresent() &&
                !acctUserService.getUserByEmail(userPrevLoginFails.get().getUserEmail()
                        .toLowerCase()).get().getRoles().contains(ROLE.ROLE_ADMINISTRATOR)) {
            UserFailLoginCounter userFailLoginCounter = userPrevLoginFails.get();
            userFailLoginCounter.setCount(userFailLoginCounter.getCount() + 1);
            if (userFailLoginCounter.getCount() == 5) {
                userFailLoginCounterService.cleanUserLoginFailureCount(userFailLoginCounter);
                securityEventRepo.save(new SecurityEvent(EventName.BRUTE_FORCE, securityEvent.getSubject(), securityEvent.getObject(), securityEvent.getPath()));
                acctUserService.lockUnlockAcctUser(securityEvent.getSubject(), UserOperation.LOCK, request, securityEvent.getSubject());
            } else {
                userFailLoginCounterService.increaseUserLoginFailureCount(userFailLoginCounter);
            }
        } else {
            UserFailLoginCounter userFailLoginCounter = userFailLoginCounterService.saveNewLoginCounter(new UserFailLoginCounter(1, securityEvent.getSubject()));
        }
        return saved;
    }
}
