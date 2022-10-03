package account.app.ropository;

import account.app.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

    @Query("SELECT p From Payment p WHERE p.employee = :userEmail AND p.period = :period")
    Optional<Payment> findUserSalaryInParticularPeriod(@Param(value = "userEmail") String userEmail, @Param(value = "period") String period);

    List<Payment> findAllByEmployee(String employeeEmail);
}
