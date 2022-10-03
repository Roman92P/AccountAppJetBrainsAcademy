package account.app.model;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@NoArgsConstructor
public class UserRoleOperationDetails implements Serializable {

    @NotEmpty(message = "Provide user email!")
    private String user;
    @NotEmpty(message = "Provide role for this operation!")
    private ROLE role;
    @NotEmpty(message = "Choose GRANT/REMOVE")
    private Operation operation;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
