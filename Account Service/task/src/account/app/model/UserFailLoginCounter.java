package account.app.model;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Entity
public class UserFailLoginCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int count;

    @Column(unique = true)
    private String userEmail;

    public UserFailLoginCounter() {
    }

    public UserFailLoginCounter(int count, String userEmail) {
        this.count = count;
        this.userEmail = userEmail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
