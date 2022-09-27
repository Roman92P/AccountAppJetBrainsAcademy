package account.app.ropository;

import account.app.model.AcctUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccUserRepo  extends JpaRepository<AcctUser, Long> {
    Optional<AcctUser> findByName(String username);
}
