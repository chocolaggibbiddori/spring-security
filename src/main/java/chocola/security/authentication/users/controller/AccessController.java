package chocola.security.authentication.users.controller;

import chocola.security.authentication.domain.dto.AccountDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccessController {

    @GetMapping("/denied")
    public String denied(@RequestParam("exception") String errorMessage,
                         @AuthenticationPrincipal AccountDto accountDto,
                         Model model) {
        model.addAttribute("exception", errorMessage);
        model.addAttribute("username", accountDto.getUsername());

        return "/denied";
    }
}
