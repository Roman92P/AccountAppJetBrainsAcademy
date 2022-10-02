package account.app.businessFunctionality;

import account.app.model.Payment;
import account.app.model.PaymentSaveUpdateDTO;
import account.app.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@RestController
public class AcctPaymentController {

    @Autowired
    PaymentService paymentService;

    @Transactional
    @PostMapping(path = "/api/acct/payments", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> uploadsPayrolls(@RequestBody List<PaymentSaveUpdateDTO> payments, HttpServletRequest request) {
        for (PaymentSaveUpdateDTO paymentDto : payments) {
            paymentService.saveAcctUserPayment(paymentDto, request.getContextPath());
        }
        return ResponseEntity.ok("{\n" +
                "   \"status\": \"Added successfully!\"\n" +
                "}");
    }

    @PutMapping(path = "/api/acct/payments", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> changesTheSalaryOfASpecificUser(@RequestBody PaymentSaveUpdateDTO payment, HttpServletRequest request) {
        paymentService.updateAcctUserPayment(payment, request.getContextPath());
        return ResponseEntity.ok("{\n" +
                "   \"status\": \"Updated successfully!\"\n" +
                "}");
    }
}
