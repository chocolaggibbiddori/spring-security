package chocola.security.authentication.admin.service.impl;

import chocola.security.authentication.admin.repository.RoleRepository;
import chocola.security.authentication.admin.service.RoleService;
import chocola.security.authentication.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(long id) {
        return roleRepository.findById(id).orElse(new Role());
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public List<Role> getRolesWithoutExpression() {
        return roleRepository.findAllRolesWithoutExpression();
    }

    @Transactional
    public void createRole(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }
}
