package chocola.security.authentication.manager;

import chocola.security.authentication.admin.repository.ResourcesRepository;
import chocola.security.authentication.mapper.PersistentUrlRoleMapper;
import chocola.security.authentication.service.DynamicAuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final AuthorizationDecision ACCESS = new AuthorizationDecision(true);

    private final HandlerMappingIntrospector handlerMappingIntrospector;
    private final DynamicAuthorizationService dynamicAuthorizationService;
    private final RoleHierarchy roleHierarchy;
    private final List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    @Autowired
    public DynamicAuthorizationManager(HandlerMappingIntrospector handlerMappingIntrospector, ResourcesRepository resourcesRepository, RoleHierarchy roleHierarchy) {
        this.handlerMappingIntrospector = handlerMappingIntrospector;
        this.dynamicAuthorizationService = new DynamicAuthorizationService(new PersistentUrlRoleMapper(resourcesRepository));
        this.roleHierarchy = roleHierarchy;
        this.mappings = new ArrayList<>(mapping());
    }

    public List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mapping() {
        return dynamicAuthorizationService
                .getUrlRoleMappings()
                .entrySet().stream()
                .map(this::createRequestMatcherEntry)
                .toList();
    }

    private RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> createRequestMatcherEntry(Map.Entry<String, String> entry) {
        String url = entry.getKey();
        String role = entry.getValue();
        Objects.requireNonNull(role, "Require role");

        AuthorizationManager<RequestAuthorizationContext> authorizationManager = role.startsWith("ROLE_") ?
                createAuthorityAuthorizationManager(role) :
                createWebExpressionAuthorizationManager(role);

        return new RequestMatcherEntry<>(new MvcRequestMatcher(handlerMappingIntrospector, url), authorizationManager);
    }

    private AuthorityAuthorizationManager<RequestAuthorizationContext> createAuthorityAuthorizationManager(String role) {
        AuthorityAuthorizationManager<RequestAuthorizationContext> authorityAuthorizationManager = AuthorityAuthorizationManager.hasAuthority(role);
        authorityAuthorizationManager.setRoleHierarchy(roleHierarchy);
        return authorityAuthorizationManager;
    }

    private WebExpressionAuthorizationManager createWebExpressionAuthorizationManager(String role) {
        WebExpressionAuthorizationManager webExpressionAuthorizationManager = new WebExpressionAuthorizationManager(role);
        DefaultHttpSecurityExpressionHandler defaultHttpSecurityExpressionHandler = new DefaultHttpSecurityExpressionHandler();
        defaultHttpSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        webExpressionAuthorizationManager.setExpressionHandler(defaultHttpSecurityExpressionHandler);
        return webExpressionAuthorizationManager;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : mappings) {
            RequestMatcher requestMatcher = mapping.getRequestMatcher();
            HttpServletRequest request = requestAuthorizationContext.getRequest();
            RequestMatcher.MatchResult matchResult = requestMatcher.matcher(request);

            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
                return manager.check(authentication, new RequestAuthorizationContext(request, matchResult.getVariables()));
            }
        }

        return ACCESS;
    }

    public synchronized void reload() {
        mappings.clear();
        mappings.addAll(mapping());
    }
}
