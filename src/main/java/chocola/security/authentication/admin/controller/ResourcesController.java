package chocola.security.authentication.admin.controller;

import chocola.security.authentication.admin.repository.RoleRepository;
import chocola.security.authentication.admin.service.ResourcesService;
import chocola.security.authentication.admin.service.RoleService;
import chocola.security.authentication.domain.dto.ResourcesDto;
import chocola.security.authentication.domain.entity.Resources;
import chocola.security.authentication.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin/resources")
@RequiredArgsConstructor
public class ResourcesController {

    private final ResourcesService resourcesService;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    @GetMapping
    public String getResources(Model model) {
        List<Resources> resources = resourcesService.getResources();
        model.addAttribute("resources", resources);
        return "admin/resources";
    }

    @PostMapping
    public String createResources(ResourcesDto resourcesDto) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<Role> role = roleRepository.findByRoleName(resourcesDto.getRoleName());
        Resources resources = modelMapper.map(resourcesDto, Resources.class);
        resources.setRoleSet(role.map(Set::of).orElseGet(Set::of));

        resourcesService.createResources(resources);
        return "redirect:/admin/resources";
    }

    @GetMapping(value = "/register")
    public String resourcesRegister(Model model) {
        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        List<String> myRoles = new ArrayList<>();
        model.addAttribute("myRoles", myRoles);

        ResourcesDto resources = new ResourcesDto();
        resources.setRoleSet(Set.of(new Role()));
        model.addAttribute("resources", resources);

        return "admin/resourcesdetails";
    }

    @GetMapping(value = "/{id}")
    public String resourceDetails(@PathVariable("id") Long id, Model model) {
        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        Resources resources = resourcesService.getResources(id);
        List<String> myRoles = resources.getRoleSet()
                .stream()
                .map(Role::getRoleName)
                .toList();
        model.addAttribute("myRoles", myRoles);

        ModelMapper modelMapper = new ModelMapper();
        ResourcesDto resourcesDto = modelMapper.map(resources, ResourcesDto.class);
        model.addAttribute("resources", resourcesDto);

        return "admin/resourcesdetails";
    }

    @GetMapping(value = "/delete/{id}")
    public String removeResources(@PathVariable("id") Long id) {
        resourcesService.deleteResources(id);
        return "redirect:/admin/resources";
    }
}
