package account.app.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class UserNewPassword implements Serializable {

    @NotEmpty
    @Length(min = 12, message = "Password length must be 12 chars minimum!")
    private String new_password;

    public String getNew_password() {
        return new_password;
    }

    public void setNewPassword(String new_password) {
        this.new_password = new_password;
    }

    public UserNewPassword() {

    }

}
