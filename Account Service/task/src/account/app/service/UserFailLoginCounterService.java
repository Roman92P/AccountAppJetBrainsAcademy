package account.app.service;

import account.app.model.UserFailLoginCounter;

import java.util.Optional;

public interface UserFailLoginCounterService {

    void increaseUserLoginFailureCount(UserFailLoginCounter userFailLoginCounter);

    void cleanUserLoginFailureCount(UserFailLoginCounter userFailLoginCounter);

    Optional<UserFailLoginCounter> findUserPrevLoginFails(String subject);

    UserFailLoginCounter saveNewLoginCounter(UserFailLoginCounter userFailLoginCounter);
}
