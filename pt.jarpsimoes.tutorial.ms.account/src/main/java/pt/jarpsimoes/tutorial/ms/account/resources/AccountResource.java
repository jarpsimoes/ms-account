package pt.jarpsimoes.tutorial.ms.account.resources;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.jarpsimoes.tutorial.ms.account.dtos.AccountDTO;
import pt.jarpsimoes.tutorial.ms.account.exceptions.AccountAlreadyExistsException;
import pt.jarpsimoes.tutorial.ms.account.services.AccountService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountResource {

    Logger logger = LoggerFactory.getLogger(AccountResource.class);
    @Inject AccountService accountService;

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response addNewAccount(AccountDTO accountDTO) {
        logger.debug("Add AccountDTO: {}", accountDTO);

        if(accountDTO == null) {
            logger.error("AccountDTO is null");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(accountDTO.getFirstName() == null || accountDTO.getFirstName().isEmpty()) {
            logger.error("AccountDTO first name is null");
            return Response.status(Response.Status.PRECONDITION_REQUIRED).build();
        }

        if(accountDTO.getLastName() == null || accountDTO.getLastName().isEmpty()) {
            logger.error("AccountDTO last name is null");
            return Response.status(Response.Status.PRECONDITION_REQUIRED).build();
        }

        if(accountDTO.getEmail() == null || accountDTO.getEmail().isEmpty()) {
            logger.error("AccountDTO email is null");
            return Response.status(Response.Status.PRECONDITION_REQUIRED).build();
        }

        if(accountDTO.getUsername() == null || accountDTO.getUsername().isEmpty()) {
            logger.error("AccountDTO username is null or empty");
            return Response.status(Response.Status.PRECONDITION_REQUIRED).build();
        }

        try {
            return Response.status(201).entity(accountService.addNewAccount(accountDTO)).build();
        } catch (AccountAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }

    }
}
