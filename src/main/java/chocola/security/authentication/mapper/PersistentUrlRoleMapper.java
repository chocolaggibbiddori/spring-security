package chocola.security.authentication.mapper;

import chocola.security.authentication.admin.repository.ResourcesRepository;
import chocola.security.authentication.domain.entity.Resources;
import chocola.security.authentication.domain.entity.Role;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PersistentUrlRoleMapper implements UrlRoleMapper {

    private final LinkedHashMap<String, String> mappings = new LinkedHashMap<>();
    private final ResourcesRepository resourcesRepository;

    public PersistentUrlRoleMapper(ResourcesRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    @Override
    public Map<String, String> getUrlRoleMappings() {
        record MappingEntry(String resourceName, String roleName) {
        }

        mappings.clear();
        List<Resources> resourcesList = resourcesRepository.findAllResources();
        resourcesList.stream()
                .mapMulti((Resources resources, Consumer<MappingEntry> consumer) -> {
                    for (Role role : resources.getRoleSet()) {
                        MappingEntry entry = new MappingEntry(resources.getResourceName(), role.getRoleName());
                        consumer.accept(entry);
                    }})
                .forEach(entry -> mappings.put(entry.resourceName, entry.roleName));

        return mappings;
    }
}
