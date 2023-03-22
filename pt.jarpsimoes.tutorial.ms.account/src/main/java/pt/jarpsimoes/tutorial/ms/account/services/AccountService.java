package pt.jarpsimoes.tutorial.ms.account.services;

import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDTO;
import pt.jarpsimoes.tutorial.ms.account.exceptions.AccountAlreadyExistsException;

import java.util.List;

public interface AccountService {

    AccountDTO addNewAccount(AccountDTO account)
            throws AccountAlreadyExistsException;

    List<AccountDTO> findAccountsByFirstName(String firstName);
    List<AccountDTO> findAccountsByLastName(String lastName);
    boolean deleteAccount(Long id);
}
