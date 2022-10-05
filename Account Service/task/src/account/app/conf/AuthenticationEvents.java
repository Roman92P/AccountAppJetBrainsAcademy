package account.app.conf;

import account.app.model.UserFailLoginCounter;
import account.app.service.UserFailLoginCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationEvents {

    @Autowired
    UserFailLoginCounterService userFailLoginCounterService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        String name = success.getAuthentication().getName();
        if (userFailLoginCounterService.findUserPrevLoginFails(name).isPresent()) {
            UserFailLoginCounter userFailLoginCounter = userFailLoginCounterService.findUserPrevLoginFails(name).get();
            userFailLoginCounterService.cleanUserLoginFailureCount(userFailLoginCounter);
        }
    }
}
