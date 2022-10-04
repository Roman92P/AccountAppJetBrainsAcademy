package account.app.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class LockUnlockUserOperationModel implements Serializable {

    @NotEmpty
    private String user;
    @NotEmpty
    private UserOperation operation;

    public LockUnlockUserOperationModel() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UserOperation getOperation() {
        return operation;
    }

    public void setOperation(UserOperation operation) {
        this.operation = operation;
    }
}
