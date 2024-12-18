package chocola.security.authentication.security.details;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;

public class FormAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, FormAuthenticationDetails> {

    @Override
    public FormAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new FormAuthenticationDetails(request);
    }
}
