package account.app.model;

import java.util.List;

public interface AcctUserProjection {

     Long getId();

     String getName();

     String getLastname();

     String getEmail();

     List<ROLE> getRoles();
}
