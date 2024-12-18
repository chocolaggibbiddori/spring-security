package chocola.security.authentication.domain.dto;

import lombok.Data;

@Data
public class SignupDto {

    private Long id;
    private String username;
    private String password;
    private int age;
    private String roles;
}
