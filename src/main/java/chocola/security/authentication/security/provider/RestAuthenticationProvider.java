package chocola.security.authentication.security.provider;

import chocola.security.authentication.token.RestAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String pw = (String) authentication.getCredentials();
        UserDetails userDetails = getUserDetails(id);

        checkPassword(pw, userDetails.getPassword());

        return new RestAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserDetails getUserDetails(String id) {
        return userDetailsService.loadUserByUsername(id);
    }

    private void checkPassword(String pw, String encodedPassword) {
        if (!passwordEncoder.matches(pw, encodedPassword)) {
            throw new BadCredentialsException("Invalid password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(RestAuthenticationToken.class);
    }
}
