package pt.jarpsimoes.tutorial.ms.account.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDTO;
import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDetailDTO;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Date;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AccountResourceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static AccountDTO accountDummy;

    @BeforeAll
    @AfterAll
    static void setup() {
        AccountDTO accountDTO = getAccountDTO();

        accountDummy = accountDTO;

        File file = new File("./test_db.mv.db");
        if(file.exists()) {
            file.delete();
        }
    }

    private static AccountDTO getAccountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setFirstName("John");
        accountDTO.setLastName("Doe");
        accountDTO.setEmail("test@email.pt");

        AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
        accountDetailDTO.setCountry("Portugal");
        accountDetailDTO.setBirthDate(new Date());
        accountDetailDTO.setState("Lisbon");

        accountDTO.setDetail(accountDetailDTO);
        return accountDTO;
    }

    @Test
    public void testAddNewAccount() throws JsonProcessingException {

        given()
                .header("Content-Type", "application/json")
                .when().put("/account")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());

        AccountDTO accountDummyWOFirstName = accountDummy;
        accountDummyWOFirstName.setFirstName(null);

        given()
                .header("Content-Type", "application/json")
                .body(MAPPER.writeValueAsString(accountDummyWOFirstName))
                .when().put("/account")
                .then()
                .statusCode(Response.Status.PRECONDITION_REQUIRED.getStatusCode());

        accountDummy.setFirstName("John");
        accountDummyWOFirstName.setLastName(null);

        given()
                .header("Content-Type", "application/json")
                .body(MAPPER.writeValueAsString(accountDummy))
                .when().put("/account")
                .then()
                .statusCode(Response.Status.PRECONDITION_REQUIRED.getStatusCode());

        accountDummy.setLastName("Doe");
        accountDummy.setEmail(null);

        given()
                .header("Content-Type", "application/json")
                .body(MAPPER.writeValueAsString(accountDummy))
                .when().put("/account")
                .then()
                .statusCode(Response.Status.PRECONDITION_REQUIRED.getStatusCode());

        accountDummy.setEmail("test@email.pt");
        accountDummy.setUsername(null);

        given()
                .header("Content-Type", "application/json")
                .body(MAPPER.writeValueAsString(accountDummy))
                .when().put("/account")
                .then()
                .statusCode(Response.Status.PRECONDITION_REQUIRED.getStatusCode());

        accountDummy.setUsername("test");

        given()
                .header("Content-Type", "application/json")
                .body(MAPPER.writeValueAsString(accountDummy))
                .when().put("/account")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        given()
                .header("Content-Type", "application/json")
                .body(MAPPER.writeValueAsString(accountDummy))
                .when().put("/account")
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }

}
