package chocola.security.authentication.admin.service.impl;

import chocola.security.authentication.admin.repository.RoleHierarchyRepository;
import chocola.security.authentication.admin.service.RoleHierarchyService;
import chocola.security.authentication.domain.entity.RoleHierarchy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public String findAllHierarchy() {
        List<RoleHierarchy> rolesHierarchyList = roleHierarchyRepository.findAll();

        StringBuilder sb = new StringBuilder();
        for (RoleHierarchy roleHierarchy : rolesHierarchyList) {
            if (roleHierarchy.getParent() != null) {
                sb.append(roleHierarchy);
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
