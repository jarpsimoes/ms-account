package pt.jarpsimoes.tutorial.ms.account.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDTO;
import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDetailDTO;
import pt.jarpsimoes.tutorial.ms.account.entities.Account;
import pt.jarpsimoes.tutorial.ms.account.entities.AccountDetail;
import pt.jarpsimoes.tutorial.ms.account.exceptions.AccountAlreadyExistsException;
import pt.jarpsimoes.tutorial.ms.account.repositories.AccountRepository;
import pt.jarpsimoes.tutorial.ms.account.services.AccountService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    @Inject AccountRepository accountRepository;
    static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Transactional
    @Override
    public AccountDTO addNewAccount(AccountDTO account)
            throws AccountAlreadyExistsException {


        List<Account> accountList = accountRepository.findByUsernameOrEmail(account.getUsername(), account.getEmail());
        if(accountList != null && accountList.size() == 1) {
            logger.error("Account with username {} or email {} already exists", account.getUsername(), account.getEmail());
            throw new AccountAlreadyExistsException();
        }

        Account accountEntity = convertToEntity(account);

        accountRepository.persist(accountEntity);

        accountRepository.flush();

        return convertToDTO(accountEntity);

    }

    @Override public List<AccountDTO> findAccountsByFirstName(final String firstName) {
        List<Account> accounts = accountRepository.findByFirstName(firstName);
        return convertToDTO(accounts);
    }

    @Override public List<AccountDTO> findAccountsByLastName(final String lastName) {
        List<Account> accounts = accountRepository.findByLastName(lastName);
        return convertToDTO(accounts);
    }

    @Transactional
    @Override
    public boolean deleteAccount(Long id) {
        return accountRepository.deleteAccountById(id);
    }



    private Account convertToEntity(AccountDTO dto) {
        Account account = new Account();
        account.setId(dto.getId());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmail(dto.getEmail());
        account.setUsername(dto.getUsername());
        account.setDetail(convertToEntity(dto.getDetail()));
        return account;
    }
    private AccountDetail convertToEntity(AccountDetailDTO dto) {
        AccountDetail detail = new AccountDetail();

        if(dto == null) {
            return detail;
        }
        detail.setId(dto.getId());
        detail.setCountry(dto.getCountry());
        detail.setState(dto.getState());
        detail.setBirthDate(dto.getBirthDate());
        return detail;
    }
    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setFirstName(account.getFirstName());
        dto.setLastName(account.getLastName());
        dto.setEmail(account.getEmail());
        dto.setUsername(account.getUsername());
        dto.setDetail(convertToDTO(account.getDetail()));
        return dto;
    }
    private AccountDetailDTO convertToDTO(AccountDetail detail) {
        AccountDetailDTO dto = new AccountDetailDTO();
        dto.setId(detail.getId());
        dto.setCountry(detail.getCountry());
        dto.setState(detail.getState());
        dto.setBirthDate(detail.getBirthDate());
        return dto;
    }
    private List<AccountDTO> convertToDTO(List<Account> accounts) {
        List<AccountDTO> result = new ArrayList<>();

        accounts.forEach(account -> result.add(convertToDTO(account)));

        return result;
    }
}
