package account.app.service;

import account.app.exception.PasswordWasHackedException;
import account.app.exception.UserExistException;
import account.app.model.AcctUser;
import account.app.model.UserOperation;
import account.app.model.UserRoleOperationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface AcctUserService {

    Optional<AcctUser> getUserByEmail(String userEmail);

    AcctUser getAcctUserByName(String username);

    AcctUser saveNewUser(AcctUser user, String requestPath) throws UserExistException, PasswordWasHackedException;

    void updateUser(AcctUser acctUserByName, String contextPath) throws PasswordWasHackedException;

    List<AcctUser> getAllAcctUser();

    String removeAcctUser(AcctUser acctUser, HttpServletRequest request);

    AcctUser changeUserRoles(UserRoleOperationDetails userRoleOperationDetails, HttpServletRequest request);

    void lockUnlockAcctUser(String userEmail, UserOperation operation, HttpServletRequest request, String subject);

}
