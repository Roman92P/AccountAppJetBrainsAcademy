package account.app.service;

import account.app.exception.*;
import account.app.model.AcctUser;
import account.app.model.Operation;
import account.app.model.ROLE;
import account.app.model.UserRoleOperationDetails;
import account.app.ropository.AcctUserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AcctUserServiceImpl implements AcctUserService, UserDetailsService {

    Logger logger = LogManager.getLogger(AcctUserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    private final AcctUserRepo userRepo;

    private final String[] breachedPasswords = {"PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"};

    public AcctUserServiceImpl(PasswordEncoder passwordEncoder, AcctUserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public Optional<AcctUser> getUserByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail);
    }

    @Override
    public AcctUser getAcctUserByName(String username) {
        Optional<AcctUser> byName = userRepo.findByName(username);
        return byName.get();
    }

    @Override
    public AcctUser saveNewUser(AcctUser user, String requestPath) throws UserExistException, PasswordWasHackedException {
        logger.warn("Checking if user exist: " + userRepo.existsAcctUserByEmail(user.getEmail()));
        if (userRepo.existsAcctUserByEmail(user.getEmail().toLowerCase())) {
            throw new UserExistException();
        }
        if (Arrays.stream(breachedPasswords).anyMatch(pswd -> pswd.equals(user.getPassword()))) {
            throw new PasswordWasHackedException(requestPath);
        }
        if (userRepo.count() == 0) {
            user.setRoles(Arrays.asList(ROLE.ROLE_ADMINISTRATOR));
        } else if (userRepo.count() > 0) {
            user.setRoles(Arrays.asList(ROLE.ROLE_USER));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public void updateUser(AcctUser user, String contextPath) throws PasswordWasHackedException {
        if (Arrays.stream(breachedPasswords).anyMatch(pswd -> pswd.equals(user.getPassword()))) {
            throw new PasswordWasHackedException(contextPath);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public List<AcctUser> getAllAcctUser() {
        return userRepo.findAll();
    }

    @Override
    public String removeAcctUser(AcctUser acctUser) {
        if (acctUser.getRoles().contains(ROLE.ROLE_ADMINISTRATOR)) {
            throw new UserAdminTryToRemoveHimselfException();
        }
        userRepo.delete(acctUser);
        return acctUser.getEmail();
    }

    @Override
    public AcctUser changeUserRoles(UserRoleOperationDetails userRoleOperationDetails) {
        Optional<AcctUser> byEmail = userRepo.findByEmail(userRoleOperationDetails.getUser());
        if (byEmail.isEmpty()) {
            throw new AcctUserNotFoundException();
        }
        AcctUser acctUser = byEmail.get();
        if (!acctUser.getRoles().contains(userRoleOperationDetails.getRole()) && userRoleOperationDetails.getOperation().equals(Operation.REMOVE)) {
            throw new AcctUserDoesNotHaveSuchRoleException();
        }
        if (acctUser.getRoles().size() == 1 && userRoleOperationDetails.getOperation().equals(Operation.REMOVE)) {
            throw new AcctUserShouldHaveAtLeastOneRole();
        }
        if (userRoleOperationDetails.getOperation().equals(Operation.REMOVE) && userRoleOperationDetails.getRole().equals(ROLE.ROLE_ADMINISTRATOR)) {
            throw new UserAdminTryToRemoveHimselfException();
        }
        if (userRoleOperationDetails.getOperation().equals(Operation.GRANT) && acctUser.getRoles().contains(ROLE.ROLE_ADMINISTRATOR)) {
            throw new AcctServiceAdminCantHaveBusinessRolesException();
        }
            return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AcctUser> byUsername = userRepo.findByEmail(username.toLowerCase());
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found"));
        }
    }
}
