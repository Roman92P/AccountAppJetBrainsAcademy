package account.app.auth;

import account.app.exception.UserExistException;
import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;
import account.app.service.AcctUserService;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthenticationController {

    private final SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory;

    private final AcctUserService userService;

    public AuthenticationController(SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory, AcctUserService userService) {
        this.spelAwareProxyProjectionFactory = spelAwareProxyProjectionFactory;
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<AcctUserProjection> userSignup(@Valid @RequestBody AcctUser acctUser) throws UserExistException {
        AcctUser savedUser = userService.saveNewUser(acctUser);
        AcctUserProjection user = spelAwareProxyProjectionFactory.createProjection(AcctUserProjection.class, savedUser);



        return ResponseEntity.ok(user);
    }
}
