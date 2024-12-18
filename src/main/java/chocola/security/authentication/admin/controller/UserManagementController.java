package chocola.security.authentication.admin.controller;

import chocola.security.authentication.admin.service.RoleService;
import chocola.security.authentication.admin.service.UserManagementService;
import chocola.security.authentication.domain.dto.AccountDto;
import chocola.security.authentication.domain.entity.Account;
import chocola.security.authentication.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final RoleService roleService;

    @GetMapping
    public String getUsers(Model model) {
        List<Account> users = userManagementService.getUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping
    public String modifyUser(AccountDto accountDto) {
        userManagementService.modifyUser(accountDto);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        AccountDto accountDto = userManagementService.getUser(id);
        List<Role> roleList = roleService.getRolesWithoutExpression();

        model.addAttribute("user", accountDto);
        model.addAttribute("roleList", roleList);
        return "admin/userdetails";
    }

    @GetMapping(value = "/delete/{id}")
    public String removeUser(@PathVariable("id") Long id) {
        userManagementService.deleteUser(id);
        return "redirect:admin/users";
    }
}
