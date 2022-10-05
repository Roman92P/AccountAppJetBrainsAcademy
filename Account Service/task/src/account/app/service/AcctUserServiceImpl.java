package account.app.service;

import account.app.exception.*;
import account.app.model.*;
import account.app.ropository.AcctUserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class AcctUserServiceImpl implements AcctUserService, UserDetailsService {

    Logger logger = LogManager.getLogger(AcctUserServiceImpl.class);

    @Autowired
    SecurityEventService securityEventService;

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
        logger.info("Checking if user exist: " + userRepo.existsAcctUserByEmail(user.getEmail()));
        if (userRepo.existsAcctUserByEmail(user.getEmail().toLowerCase())) {
            throw new UserExistException();
        }
        if (Arrays.stream(breachedPasswords).anyMatch(pswd -> pswd.equals(user.getPassword()))) {
            throw new PasswordWasHackedException(requestPath);
        }
        if (userRepo.count() == 0) {
            user.setRoles(EnumSet.of(ROLE.ROLE_ADMINISTRATOR));
        } else if (userRepo.count() > 0) {
            user.setRoles(EnumSet.of(ROLE.ROLE_USER));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AcctUser savedUser = userRepo.save(user);
        Optional<AcctUser> byEmail = userRepo.findByEmail(user.getEmail().toLowerCase());
        securityEventService.createSecurityEvent(new SecurityEvent(EventName.CREATE_USER, "Anonymous", byEmail.get().getEmail(), requestPath));
        return savedUser;
    }

    @Override
    public void updateUser(AcctUser user, String contextPath) throws PasswordWasHackedException {
        if (Arrays.stream(breachedPasswords).anyMatch(pswd -> pswd.equals(user.getPassword()))) {
            throw new PasswordWasHackedException(contextPath);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        securityEventService.createSecurityEvent(new SecurityEvent(EventName.CHANGE_PASSWORD, user.getEmail(), user.getEmail(), contextPath));
    }

    @Override
    public List<AcctUser> getAllAcctUser() {
        return userRepo.findAll();
    }

    @Override
    public String removeAcctUser(AcctUser acctUser, HttpServletRequest request) {
        if (acctUser.getRoles().contains(ROLE.ROLE_ADMINISTRATOR)) {
            throw new UserAdminTryToRemoveHimselfException();
        }
        userRepo.delete(acctUser);
        securityEventService.createSecurityEvent(new SecurityEvent(EventName.DELETE_USER, request.getRemoteUser(), acctUser.getEmail(), request.getContextPath()));
        return acctUser.getEmail();
    }

    @Override
    public AcctUser changeUserRoles(UserRoleOperationDetails userRoleOperationDetails, HttpServletRequest request) {
        Optional<AcctUser> byEmail = userRepo.findByEmail(userRoleOperationDetails.getUser().toLowerCase());
        ROLE roleFromRequest;
        try {
            roleFromRequest = ROLE.valueOf(userRoleOperationDetails.getRole());
        } catch (IllegalArgumentException e) {
            throw new EnumNotFoundException();
        }

        if (byEmail.isEmpty()) {
            throw new AcctUserNotFoundException();
        }
        AcctUser acctUser = byEmail.get();
        if (!acctUser.getRoles().contains(roleFromRequest)
                && userRoleOperationDetails.getOperation().equals(Operation.REMOVE)) {
            throw new AcctUserDoesNotHaveSuchRoleException();
        }
        if (userRoleOperationDetails.getOperation().equals(Operation.REMOVE)
                && roleFromRequest.equals(ROLE.ROLE_ADMINISTRATOR)) {
            throw new UserAdminTryToRemoveHimselfException();
        }
        if (acctUser.getRoles().size() == 1 && userRoleOperationDetails.getOperation().equals(Operation.REMOVE)) {
            throw new AcctUserShouldHaveAtLeastOneRole();
        }
        if (userRoleOperationDetails.getOperation().equals(Operation.GRANT)
                && acctUser.getRoles().contains(ROLE.ROLE_ADMINISTRATOR)) {
            throw new AcctServiceAdminCantHaveBusinessRolesException();
        }
        if ( roleFromRequest.equals(ROLE.ROLE_ADMINISTRATOR)) {
            throw new TryingToAddAdminRoleException();
        }

        EnumSet<ROLE> roles = acctUser.getRoles();
        String roleToBeAddedOrRemoved = userRoleOperationDetails.getRole();
        switch (userRoleOperationDetails.getOperation()) {
            case REMOVE:
                roles.remove(ROLE.valueOf(userRoleOperationDetails.getRole()));
                securityEventService.createSecurityEvent(
                        new SecurityEvent(EventName.REMOVE_ROLE, request.getRemoteUser(), String.format("Remove role %s from %s",
                                userRoleOperationDetails.getRole().substring("ROLE_".length()).trim(),userRoleOperationDetails.getUser()), request.getServletPath()));
                break;
            case GRANT:
                roles.add(ROLE.valueOf(userRoleOperationDetails.getRole()));
                securityEventService.createSecurityEvent(
                        new SecurityEvent(EventName.GRANT_ROLE, request.getRemoteUser(), String.format("Grant role %s to %s",
                                userRoleOperationDetails.getRole().substring("ROLE_".length()).trim(),userRoleOperationDetails.getUser()), request.getServletPath()));
                break;
        }
        acctUser.setRoles(roles);
        acctUser.setLocked(false);
        AcctUser userWithUpdatedRoles = userRepo.save(acctUser);
        return userWithUpdatedRoles;
    }

    @Override
    public void lockUnlockAcctUser(String userEmail, UserOperation operation, HttpServletRequest request, String subject) {
        Optional<AcctUser> byEmail = userRepo.findByEmail(userEmail);
        if (byEmail.isEmpty()) {
            throw new AcctUserNotFoundException();
        }
        AcctUser acctUser = byEmail.get();
        if (acctUser.getRoles().contains(ROLE.ROLE_ADMINISTRATOR)) {
            throw new CantLockAdminException();
        }
        boolean lockUnlockOption = operation.equals(UserOperation.LOCK);
        acctUser.setLocked(lockUnlockOption);
        userRepo.save(acctUser);
        if (lockUnlockOption) {
            securityEventService.createSecurityEvent(
                    new SecurityEvent(EventName.LOCK_USER, subject, String.format("Lock user %s",
                            userEmail), request.getServletPath()));
        } else {
            securityEventService.createSecurityEvent(
                    new SecurityEvent(EventName.UNLOCK_USER, subject, String.format("Unlock user %s",
                            userEmail), request.getServletPath()));
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AcctUser> byUsername = userRepo.findByEmail(username.toLowerCase());
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found",username));
        }
    }
}
