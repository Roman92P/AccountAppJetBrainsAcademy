package account.app.admin;

import account.app.exception.AcctUserNotFoundException;
import account.app.model.AcctUser;
import account.app.model.AcctUserProjection;
import account.app.model.LockUnlockUserOperationModel;
import account.app.model.UserRoleOperationDetails;
import account.app.service.AcctUserService;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
public class AdministratorController {

    private final AcctUserService acctUserService;
    private final SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory;

    public AdministratorController(AcctUserService acctUserService, SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory) {
        this.acctUserService = acctUserService;
        this.spelAwareProxyProjectionFactory = spelAwareProxyProjectionFactory;
    }


    @PutMapping(path = "/role", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> setsTheRoles(@RequestBody UserRoleOperationDetails userRoleOperationDetails) {
        AcctUser acctUser = acctUserService.changeUserRoles(userRoleOperationDetails);
        return ResponseEntity.ok(spelAwareProxyProjectionFactory.createProjection(AcctUserProjection.class, acctUser));
    }

    @DeleteMapping(path = "/{user email}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> deletesUsers(@PathVariable(name = "user email") String email, HttpServletRequest request) {
        Optional<AcctUser> userByEmail = acctUserService.getUserByEmail(email);
        if (userByEmail.isEmpty()) {
            throw new AcctUserNotFoundException();
        }
        String removedUserEmail = acctUserService.removeAcctUser(userByEmail.get());
        return ResponseEntity.ok(String.format("{\n" +
                "   \"user\": \"%s\",\n" +
                "   \"status\": \"Deleted successfully!\"\n" +
                "}", removedUserEmail));
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> obtainUsersInfo() {
        List<AcctUser> allServiceUsers = acctUserService.getAllAcctUser();
        List<AcctUserProjection> allUsersProj = allServiceUsers.stream()
                .map(acctUser -> spelAwareProxyProjectionFactory
                        .createProjection(AcctUserProjection.class, acctUser))
                .collect(Collectors.toList());
        return ResponseEntity.ok(allUsersProj);
    }

    @PutMapping(path = "/access", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> lockUnlockUsers(@RequestBody LockUnlockUserOperationModel lockUnlockUserOperationModel) {
        switch (lockUnlockUserOperationModel.getOperation()) {
            case UNLOCK:
                acctUserService.unlockAcctUser(lockUnlockUserOperationModel.getUser());
                break;
            case LOCK:
                acctUserService.lockAcctUser(lockUnlockUserOperationModel.getUser());
                break;
        }
        return null;
    }
}
