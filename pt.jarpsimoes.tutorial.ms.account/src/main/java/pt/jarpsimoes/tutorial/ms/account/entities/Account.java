package pt.jarpsimoes.tutorial.ms.account.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotNull
    @NotBlank
    @Column(unique = true)
    private String username;
    @OneToOne(cascade = CascadeType.ALL)
    private AccountDetail detail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccountDetail getDetail() {
        return detail;
    }

    public void setDetail(AccountDetail detail) {
        this.detail = detail;
    }
    public void checkDuplicates() {

    }
}
