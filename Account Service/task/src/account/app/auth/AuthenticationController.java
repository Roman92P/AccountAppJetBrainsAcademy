package account.app.auth;

import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;
import account.app.service.AcctUserService;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private final SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory;

    private final AcctUserService userService;

    public AuthenticationController(SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory, AcctUserService userService) {
        this.spelAwareProxyProjectionFactory = spelAwareProxyProjectionFactory;
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<AcctUserProjection> userSignup(@Valid @RequestBody AcctUser acctUser) {
        AcctUserProjection user = spelAwareProxyProjectionFactory.createProjection(AcctUserProjection.class, acctUser);
        userService.saveNewUser(user);
        return ResponseEntity.ok(user);
    }
}
