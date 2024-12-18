package chocola.security.authentication.domain.dto;

import chocola.security.authentication.domain.entity.Account;
import chocola.security.authentication.domain.entity.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AccountDto implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private int age;
    private List<GrantedAuthority> authorities;
    private Set<String> roles;

    public AccountDto(Account account) {
        this(account.getId(), account.getUsername(), account.getPassword(), account.getAge(), account.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
    }

    @JsonCreator
    public AccountDto(@JsonProperty("id") Long id,
                      @JsonProperty("username") String username,
                      @JsonProperty("password") String password,
                      @JsonProperty("age") int age,
                      @JsonProperty("roles") Set<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.authorities = new ArrayList<>(roles.stream().map(SimpleGrantedAuthority::new).toList());
        this.roles = roles;
    }
}
