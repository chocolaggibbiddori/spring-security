package chocola.security.authentication.admin.service;

import chocola.security.authentication.domain.dto.AccountDto;
import chocola.security.authentication.domain.entity.Account;

import java.util.List;

public interface UserManagementService {

    void modifyUser(AccountDto accountDto);

    List<Account> getUsers();

    AccountDto getUser(Long id);

    void deleteUser(Long id);
}
