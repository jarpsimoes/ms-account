package pt.jarpsimoes.tutorial.ms.account.dtos;

public class AccountDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private AccountDetailDTO detail;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public AccountDetailDTO getDetail() {
        return detail;
    }

    public void setDetail(AccountDetailDTO detail) {
        this.detail = detail;
    }
}
