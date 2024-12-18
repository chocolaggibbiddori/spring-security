package chocola.security.authentication.admin.controller;

import chocola.security.authentication.admin.service.RoleService;
import chocola.security.authentication.domain.dto.RoleDto;
import chocola.security.authentication.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public String getRoles(Model model) {
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "admin/roles";
    }

    @GetMapping(value = "/register")
    public String rolesRegister(Model model) {
        RoleDto role = new RoleDto();
        model.addAttribute("roles", role);
        return "admin/rolesdetails";
    }

    @PostMapping
    public String createRole(RoleDto roleDto) {
        ModelMapper modelMapper = new ModelMapper();
        Role role = modelMapper.map(roleDto, Role.class);

        roleService.createRole(role);
        return "redirect:/admin/roles";
    }

    @GetMapping(value = "/{id}")
    public String getRole(@PathVariable("id") Long id, Model model) {
        Role role = roleService.getRole(id);

        ModelMapper modelMapper = new ModelMapper();
        RoleDto roleDto = modelMapper.map(role, RoleDto.class);
        model.addAttribute("roles", roleDto);

        return "admin/rolesdetails";
    }

    @GetMapping(value = "/delete/{id}")
    public String removeRoles(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return "redirect:/admin/roles";
    }
}
