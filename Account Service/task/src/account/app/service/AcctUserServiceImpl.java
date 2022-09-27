package account.app.service;

import account.app.exception.UserExistException;
import account.app.model.AcctUser;
import account.app.ropository.AcctUserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcctUserServiceImpl implements AcctUserService, UserDetailsService {

    Logger logger = LogManager.getLogger(AcctUserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    private final AcctUserRepo userRepo;

    public AcctUserServiceImpl(PasswordEncoder passwordEncoder, AcctUserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public AcctUser getAcctUserByName(String username) {
        Optional<AcctUser> byName = userRepo.findByName(username);
        return byName.get();
    }

    @Override
    public AcctUser saveNewUser(AcctUser user) throws UserExistException {
        logger.warn("Checking if user exist: "+ userRepo.existsAcctUserByEmail(user.getEmail()));
        if (userRepo.existsAcctUserByEmail(user.getEmail().toLowerCase())) {
            throw new UserExistException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AcctUser> byUsername = userRepo.findByEmail(username.toLowerCase());
        if (byUsername.isPresent()){
            return byUsername.get();
        }else{
            throw new UsernameNotFoundException(String.format("Username[%s] not found"));
        }
    }
}
