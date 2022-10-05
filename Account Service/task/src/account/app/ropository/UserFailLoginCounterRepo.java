package account.app.ropository;

import account.app.model.UserFailLoginCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFailLoginCounterRepo extends JpaRepository<UserFailLoginCounter, Long> {


    Optional<UserFailLoginCounter> findByUserEmail(String userEmail);
}
