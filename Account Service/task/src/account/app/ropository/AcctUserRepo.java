package account.app.ropository;

import account.app.model.AcctUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcctUserRepo extends JpaRepository<AcctUser, Long> {
    Optional<AcctUser> findByEmail(String username);

    Optional<AcctUser> findByName(String username);

    @Query("SELECT COUNT(u) > 0 FROM AcctUser u WHERE u.email = :email ")
    boolean existsAcctUserByEmail(String email);
}
