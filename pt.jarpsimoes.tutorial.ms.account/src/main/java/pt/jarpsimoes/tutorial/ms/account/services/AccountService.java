package pt.jarpsimoes.tutorial.ms.account.services;

import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDTO;
import pt.jarpsimoes.tutorial.ms.account.exceptions.AccountAlreadyExistsException;

public interface AccountService {

    AccountDTO addNewAccount(AccountDTO account)
            throws AccountAlreadyExistsException;

}
