package chocola.security.authentication.mapper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapBasedUrlRoleMapper implements UrlRoleMapper {

    private final LinkedHashMap<String, String> mappings = new LinkedHashMap<>();

    @Override
    public Map<String, String> getUrlRoleMappings() {
        mappings.put("/", "permitAll");
        mappings.put("/css/**", "permitAll");
        mappings.put("/js/**", "permitAll");
        mappings.put("/images/**", "permitAll");
        mappings.put("/favicon.*", "permitAll");
        mappings.put("/*/icon-*", "permitAll");
        mappings.put("/signup", "permitAll");
        mappings.put("/login", "permitAll");
        mappings.put("/logout", "permitAll");
        mappings.put("/denied", "authenticated");
        mappings.put("/user", "ROLE_USER");
        mappings.put("/admin/**", "ROLE_ADMIN");
        mappings.put("/manager", "ROLE_MANAGER");
        mappings.put("/db", "hasRole('DBA')");

        return Collections.unmodifiableMap(mappings);
    }
}
