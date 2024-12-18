package chocola.security.authentication.admin.repository;

import chocola.security.authentication.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserManagementRepository extends JpaRepository<Account, Long> {
}
