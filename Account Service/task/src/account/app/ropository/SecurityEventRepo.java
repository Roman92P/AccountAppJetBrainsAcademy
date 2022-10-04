package account.app.ropository;

import account.app.model.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityEventRepo extends JpaRepository<SecurityEvent, Long> {
}
