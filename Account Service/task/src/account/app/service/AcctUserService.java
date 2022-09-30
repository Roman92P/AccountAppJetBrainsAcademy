package account.app.service;

import account.app.exception.PasswordWasHackedException;
import account.app.exception.UserExistException;
import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;

public interface AcctUserService {

    AcctUser getAcctUserByName (String username);

    AcctUser saveNewUser(AcctUser user, String requestPath) throws UserExistException, PasswordWasHackedException;

    void updateUser(AcctUser acctUserByName, String contextPath) throws PasswordWasHackedException;
}
