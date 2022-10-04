package account.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserRoleOperationDetails implements Serializable {

    @NotEmpty(message = "Provide user email!")
    private String user;
    @NotEmpty(message = "Provide role for this operation!")
    private String role;
    @NotEmpty(message = "Choose GRANT/REMOVE")
    @Enumerated(EnumType.STRING)
    private Operation operation;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role ="ROLE_"+role;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
