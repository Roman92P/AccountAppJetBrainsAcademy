package account.app.businessFunctionality;

import account.app.model.AcctUserProjection;
import account.app.service.AcctUserService;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class BusinessFunctionalityController {

    private final AcctUserService userService;
    private final ProjectionFactory spelAwareProxyProjectionFactory;

    public BusinessFunctionalityController(AcctUserService userService, ProjectionFactory spelAwareProxyProjectionFactory) {
        this.userService = userService;
        this.spelAwareProxyProjectionFactory = spelAwareProxyProjectionFactory;
    }

    @GetMapping(path = "/api/empl/payment", produces = "application/json")
    @ResponseBody
    public ResponseEntity<AcctUserProjection> getCurrentLoggedUser(Principal principal) {
        String name = principal.getName();
        UserDetails foundUser = userService.getAcctUserByName(name);
        AcctUserProjection userProj = spelAwareProxyProjectionFactory.createProjection(AcctUserProjection.class, foundUser);
        return ResponseEntity.ok(userProj);
    }
}
