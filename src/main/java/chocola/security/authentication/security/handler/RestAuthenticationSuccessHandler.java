package chocola.security.authentication.security.handler;

import chocola.security.authentication.domain.dto.AccountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        AccountDto accountDto = (AccountDto) authentication.getPrincipal();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        accountDto = new AccountDto(accountDto.getId(), accountDto.getUsername(), null, accountDto.getAge(), accountDto.getRoles());

        om.writeValue(response.getWriter(), accountDto);
        clearAuthenticationAttributes(request);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) return;

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
