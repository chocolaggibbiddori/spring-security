package chocola.security.authentication.service;

import chocola.security.authentication.mapper.UrlRoleMapper;

import java.util.Map;

public class DynamicAuthorizationService {

    private final UrlRoleMapper mapper;

    public DynamicAuthorizationService(UrlRoleMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, String> getUrlRoleMappings() {
        return mapper.getUrlRoleMappings();
    }
}
