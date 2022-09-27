package account.app.service;

import account.app.exception.UserExistException;
import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;

public interface AcctUserService {

    AcctUser getAcctUserByName (String username);

    AcctUser saveNewUser(AcctUser user) throws UserExistException;
}
