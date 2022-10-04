package account.app.businessFunctionality;

import account.app.auth.AuthenticationController;
import account.app.model.AcctUser;
import account.app.model.PaymentDTO;
import account.app.service.AcctUserService;
import account.app.service.PaymentService;
import account.app.utils.DateConvert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BusinessFunctionalityController {
    Logger logger = LogManager.getLogger(AuthenticationController.class);

    private final AcctUserService userService;
    private final ProjectionFactory spelAwareProxyProjectionFactory;
    private final PaymentService paymentService;

    public BusinessFunctionalityController(AcctUserService userService, ProjectionFactory spelAwareProxyProjectionFactory, PaymentService paymentService) {
        this.userService = userService;
        this.spelAwareProxyProjectionFactory = spelAwareProxyProjectionFactory;
        this.paymentService = paymentService;
    }

    @GetMapping(path = "/api/empl/payment", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> getCurrentLoggedUser(@RequestParam Optional<String> period, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.warn("Currently logged user from /api/empl/payment: "+ principal.getName());
        AcctUser acctUserByName = userService.getUserByEmail(principal.getName()).get();
        List<PaymentDTO> allAcctUserPayments = paymentService.getAllAcctUserPayments(acctUserByName);
        if (period.isPresent()) {
            String s = period.get();
            String periodToCheck = DateConvert.convertDigitMonthToString(s);
            PaymentDTO paymentDTO = allAcctUserPayments.stream().filter(p -> p.getPeriod().equals(periodToCheck))
                    .findAny().orElseThrow(EntityNotFoundException::new);
            return ResponseEntity.ok(paymentDTO);
        }
        List<PaymentDTO> sortedPaymentsByPeriod = allAcctUserPayments.stream().sorted().collect(Collectors.toList());
        return ResponseEntity.ok(sortedPaymentsByPeriod);
    }
}

