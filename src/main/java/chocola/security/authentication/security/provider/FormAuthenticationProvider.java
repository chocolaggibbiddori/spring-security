package chocola.security.authentication.security.provider;

import chocola.security.authentication.security.details.FormAuthenticationDetails;
import chocola.security.authentication.security.exception.SecretException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String pw = (String) authentication.getCredentials();
        UserDetails userDetails = getUserDetails(id);

        checkPassword(pw, userDetails.getPassword());
        checkSecretKey(authentication);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserDetails getUserDetails(String id) {
        return userDetailsService.loadUserByUsername(id);
    }

    private void checkPassword(String pw, String encodedPassword) {
        if (!passwordEncoder.matches(pw, encodedPassword)) {
            throw new BadCredentialsException("Invalid password");
        }
    }

    private void checkSecretKey(Authentication authentication) {
        FormAuthenticationDetails authenticationDetails = (FormAuthenticationDetails) authentication.getDetails();
        String secretKey = authenticationDetails.getSecretKey();
        if (Objects.isNull(secretKey) || !Objects.equals(secretKey, "secret")) {
            throw new SecretException("Invalid secret");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
