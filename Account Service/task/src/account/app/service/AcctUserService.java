package account.app.service;

import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;

public interface AcctUserService {

    AcctUser getAcctUserByName (String username);

    AcctUser saveNewUser(AcctUserProjection user);
}
