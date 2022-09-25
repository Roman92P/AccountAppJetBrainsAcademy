package account.app.auth;

import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;
import org.springframework.data.projection.ProjectionFactory;
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

    public AuthenticationController(SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory) {
        this.spelAwareProxyProjectionFactory = spelAwareProxyProjectionFactory;
    }

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<AcctUserProjection> userSignup(@Valid @RequestBody AcctUser acctUser) {
        AcctUserProjection user = spelAwareProxyProjectionFactory.createProjection(AcctUserProjection.class, acctUser);
        return ResponseEntity.ok(user);
    }
}
