package account.app.admin;

import account.app.model.SecurityEvent;
import account.app.service.SecurityEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/security/events")
public class SecurityEventsController {

    @Autowired
    SecurityEventService securityEventService;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> getSecurityEvents() {
        List<SecurityEvent> allSecurityEvents = securityEventService.getAllSecurityEvents();
        return ResponseEntity.ok(allSecurityEvents);
    }
}
