package pt.jarpsimoes.tutorial.ms.account.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pt.jarpsimoes.tutorial.ms.account.entities.Account;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    public Account findByUsername(String username) {
        return find("username", username).firstResult();
    }
    public Account findByEmail(String email) {
        return find("email", email).firstResult();
    }
    public List<Account> findByUsernameOrEmail(String username, String email) {
        return list("username = ?1 or email = ?2", username, email);
    }
}
