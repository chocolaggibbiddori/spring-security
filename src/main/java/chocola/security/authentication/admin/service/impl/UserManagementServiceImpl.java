package chocola.security.authentication.admin.service.impl;

import chocola.security.authentication.admin.repository.RoleRepository;
import chocola.security.authentication.admin.repository.UserManagementRepository;
import chocola.security.authentication.admin.service.UserManagementService;
import chocola.security.authentication.domain.dto.AccountDto;
import chocola.security.authentication.domain.entity.Account;
import chocola.security.authentication.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void modifyUser(AccountDto accountDto) {
        Account account = userManagementRepository.findById(accountDto.getId()).orElseThrow();

        Set<String> roleNames = accountDto.getRoles();
        if (roleNames != null) {
            Set<Role> roles = new HashSet<>(roleNames.stream()
                    .map(roleRepository::findByRoleName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList());
            account.setRoles(roles);
        }

        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setAge(accountDto.getAge());
        userManagementRepository.save(account);
    }

    public AccountDto getUser(Long id) {
        Account account = userManagementRepository.findById(id).orElse(new Account());
        return new AccountDto(account);
    }

    public List<Account> getUsers() {
        return userManagementRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userManagementRepository.deleteById(id);
    }
}
