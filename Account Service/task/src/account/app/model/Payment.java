package account.app.model;

import account.app.service.AcctUserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Table(name = "payment",
        uniqueConstraints = {@UniqueConstraint(columnNames =
                {"employee", "period"})})
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "acctUserId")
    private AcctUser acctUser;
    private String employee;
    private String period;
    private long salary;

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AcctUser getAcctUser() {
        return acctUser;
    }

    public void setAcctUser(AcctUser acctUser) {
        this.acctUser = acctUser;
    }
}
