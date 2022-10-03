package account.app.model;

import account.app.utils.SalaryLongToStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@NoArgsConstructor
public class PaymentDTO implements Serializable, Comparable<PaymentDTO> {
    private String name;
    private String lastname;
    private String period;
    private String salary;
    @JsonIgnore
    private AcctUser acctUser;
    @JsonIgnore
    private LocalDate pDate;

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MM-yyyy")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();
        LocalDate date = LocalDate.parse(period, formatter);
        this.pDate = date;
        String mthStr = date.getMonth().toString();
        String month = mthStr.charAt(0) + mthStr.toLowerCase().substring(1);
        this.period = month + "-" + date.getYear();
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = SalaryLongToStringConverter.convertSalary(salary);
    }

    public AcctUser getAcctUser() {
        return acctUser;
    }

    public void setAcctUser(AcctUser acctUser) {
        this.acctUser = acctUser;
    }

    @Override
    public int compareTo(PaymentDTO o) {
        if (o.pDate.isAfter(this.pDate)) {
            return 1;
        } else if (o.pDate.isBefore(this.pDate)) {
            return -1;
        } else
            return 0;
    }
}
