package pt.jarpsimoes.tutorial.ms.account.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDTO;
import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDetailDTO;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Date;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountResourceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static AccountDTO accountDummy;

    private static AccountDTO response;
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
    @Order(1)
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

        response = given()
            .header("Content-Type", "application/json")
            .body(MAPPER.writeValueAsString(accountDummy))
            .when().put("/account")
            .then()
            .statusCode(Response.Status.CREATED.getStatusCode())
            .extract().body().as(AccountDTO.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());

        given()
            .header("Content-Type", "application/json")
            .body(MAPPER.writeValueAsString(accountDummy))
            .when().put("/account")
            .then()
            .statusCode(Response.Status.CONFLICT.getStatusCode());

    }

    @Test
    @Order(2)
    public void testGetAccountsByFirstName() throws JsonProcessingException {
        AccountDTO[] result = given()
                .when().get("/account/find/first-name?name=" + accountDummy.getFirstName())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(AccountDTO[].class);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.length > 0);
    }

    @Test
    @Order(3)
    public void testGetAccountByLastName() throws JsonProcessingException {

        AccountDTO[] results = given()
                .when().get("/account/find/last-name?name=" + accountDummy.getLastName())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(AccountDTO[].class);

        Assertions.assertNotNull(results);
        Assertions.assertTrue(results.length > 0);



    }

    @Test
    @Order(4)
    public void testDeleteAccount() throws JsonProcessingException {
        given()
                .body(MAPPER.writeValueAsString(accountDummy))
                .when().delete("/account/" + response.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        given()
                .body(MAPPER.writeValueAsString(accountDummy))
                .when().delete("/account/" + response.getId())
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
