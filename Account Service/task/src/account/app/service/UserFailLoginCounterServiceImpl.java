package account.app.service;

import account.app.model.UserFailLoginCounter;
import account.app.ropository.UserFailLoginCounterRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFailLoginCounterServiceImpl implements UserFailLoginCounterService{

    private final UserFailLoginCounterRepo userFailLoginCounterRepo;

    public UserFailLoginCounterServiceImpl(UserFailLoginCounterRepo userFailLoginCounterRepo) {
        this.userFailLoginCounterRepo = userFailLoginCounterRepo;
    }

    @Override
    public void increaseUserLoginFailureCount(UserFailLoginCounter userFailLoginCounter) {
        userFailLoginCounterRepo.save(userFailLoginCounter);
    }

    @Override
    public void cleanUserLoginFailureCount(UserFailLoginCounter userFailLoginCounter) {
        userFailLoginCounterRepo.delete(userFailLoginCounter);
    }

    @Override
    public Optional<UserFailLoginCounter> findUserPrevLoginFails(String subject) {
        return userFailLoginCounterRepo.findByUserEmail(subject);
    }

    @Override
    public UserFailLoginCounter saveNewLoginCounter(UserFailLoginCounter userFailLoginCounter) {
        return userFailLoginCounterRepo.save(userFailLoginCounter);
    }
}
