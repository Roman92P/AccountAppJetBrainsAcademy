package account.app.auth;

import account.app.exception.PasswordWasHackedException;
import account.app.exception.UserChangesPasswordSamePasswordException;
import account.app.exception.UserExistException;
import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;
import account.app.model.UserNewPassword;
import account.app.service.AcctUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);
    private final SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory;

    private final AcctUserService userService;

    private final BCryptPasswordEncoder encoder;

    public AuthenticationController(SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory, AcctUserService userService, BCryptPasswordEncoder encoder) {
        this.spelAwareProxyProjectionFactory = spelAwareProxyProjectionFactory;
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<AcctUserProjection> userSignup(@Valid @RequestBody AcctUser acctUser, HttpServletRequest request) throws UserExistException, PasswordWasHackedException {
        AcctUser savedUser = userService.saveNewUser(acctUser, request.getServletPath());
        AcctUserProjection user = spelAwareProxyProjectionFactory.createProjection(AcctUserProjection.class, savedUser);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/changepass", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> userChangePassword(@Valid @RequestBody UserNewPassword userNewPassword, Principal principal, HttpServletRequest request) throws UserExistException, PasswordWasHackedException, UserChangesPasswordSamePasswordException {
        String loggedUserName = principal.getName();
        AcctUser acctUserByName = userService.getUserByEmail(loggedUserName).get();
        logger.warn("Logged user changing pswd: " + acctUserByName);
        if (encoder.matches(userNewPassword.getNew_password(), acctUserByName.getPassword())) {
            throw new UserChangesPasswordSamePasswordException(request.getContextPath());
        }
        acctUserByName.setPassword(userNewPassword.getNew_password());
        userService.updateUser(acctUserByName, request.getServletPath());
        return ResponseEntity.ok(new ChangePasswordResponse(acctUserByName.getEmail()));
    }

    private static class ChangePasswordResponse {
        private String email;
        private String status = "The password has been updated successfully";

        public ChangePasswordResponse(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public String getStatus() {
            return status;
        }
    }


}
