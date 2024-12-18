package chocola.security.authentication.listener;

import chocola.security.authentication.admin.repository.RoleRepository;
import chocola.security.authentication.domain.entity.Account;
import chocola.security.authentication.domain.entity.Role;
import chocola.security.authentication.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        setupData();
        alreadySetup = true;
    }

    private void setupData() {
        createRoleIfNotFound("ROLE_USER", "회원");
        createRoleIfNotFound("ROLE_MANAGER", "매니저");
        createRoleIfNotFound("ROLE_DB", "DBA");
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        createUserIfNotFound("admin", "1234", 28, roles);
    }

    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);
        Role role = roleOpt.orElseGet(() -> Role.builder()
                .roleName(roleName)
                .roleDesc(roleDesc)
                .build());

        return roleRepository.save(role);
    }

    public void createUserIfNotFound(final String username, final String password, final int age, Set<Role> roleSet) {
        Optional<Account> accountOpt = userRepository.findByUsername(username);
        Account account = accountOpt.orElseGet(() -> Account.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .age(age)
                .roles(roleSet)
                .build());

        userRepository.save(account);
    }
}
