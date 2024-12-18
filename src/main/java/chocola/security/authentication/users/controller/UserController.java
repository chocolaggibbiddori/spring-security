package chocola.security.authentication.users.controller;

import chocola.security.authentication.domain.dto.SignupDto;
import chocola.security.authentication.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(SignupDto signupDto) {
        userService.createUser(signupDto);
        return "redirect:/";
    }
}
