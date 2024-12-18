package chocola.security.authentication.users.service;

import chocola.security.authentication.domain.dto.AccountDto;
import chocola.security.authentication.domain.dto.SignupDto;
import chocola.security.authentication.domain.entity.Account;
import chocola.security.authentication.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(SignupDto signupDto) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(signupDto, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        userRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOpt = userRepository.findByUsername(username);
        if (accountOpt.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }

        Account account = accountOpt.get();
        return new AccountDto(account);
    }
}
