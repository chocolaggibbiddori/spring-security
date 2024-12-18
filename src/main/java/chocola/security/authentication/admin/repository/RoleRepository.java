package chocola.security.authentication.admin.repository;

import chocola.security.authentication.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String name);

    @Query("SELECT r FROM Role r WHERE r.isExpression = 'N'")
    List<Role> findAllRolesWithoutExpression();
}
