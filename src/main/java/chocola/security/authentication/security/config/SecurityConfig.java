package chocola.security.authentication.security.config;

import chocola.security.authentication.admin.service.RoleHierarchyService;
import chocola.security.authentication.security.details.FormAuthenticationDetailsSource;
import chocola.security.authentication.security.dsl.RestApiDsl;
import chocola.security.authentication.security.entrypoint.RestAuthenticationEntryPoint;
import chocola.security.authentication.security.handler.*;
import chocola.security.authentication.security.provider.FormAuthenticationProvider;
import chocola.security.authentication.security.provider.RestAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> authorizationManager) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().access(authorizationManager))

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .authenticationDetailsSource(new FormAuthenticationDetailsSource())
                        .successHandler(new FormAuthenticationSuccessHandler())
                        .failureHandler(new FormAuthenticationFailureHandler()))

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new FormAccessDeniedHandler("/denied")))

                .authenticationProvider(new FormAuthenticationProvider(userDetailsService, passwordEncoder))

                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new RestAuthenticationProvider(userDetailsService, passwordEncoder));
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
                        .requestMatchers("/api", "/api/login").permitAll()
                        .requestMatchers("/api/user").hasRole("USER")
                        .requestMatchers("/api/manager").hasRole("MANAGER")
                        .requestMatchers("/api/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .authenticationManager(authenticationManager)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler()))

                .with(new RestApiDsl(), restApiDsl -> restApiDsl
                        .authenticationSuccessHandler(new RestAuthenticationSuccessHandler())
                        .authenticationFailureHandler(new RestAuthenticationFailureHandler())
                        .loginPage("/api/login")
                        .loginProcessingUrl("/api/login"))

                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy(RoleHierarchyService roleHierarchyService) {
        String allHierarchy = roleHierarchyService.findAllHierarchy();
        return RoleHierarchyImpl.fromHierarchy(allHierarchy);
    }
}
