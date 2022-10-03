package account.app.service;

import account.app.model.AcctUser;
import account.app.model.Payment;
import account.app.model.PaymentDTO;
import account.app.model.PaymentSaveUpdateDTO;

import java.util.List;

public interface PaymentService {

    void saveAcctUserPayment(PaymentSaveUpdateDTO payment, String requestPath);

    void updateAcctUserPayment(PaymentSaveUpdateDTO payment, String requestPath);

    List<PaymentDTO> getAllAcctUserPayments(AcctUser acctUserByName);

    void savePayments(List<PaymentSaveUpdateDTO> payments, String contextPath);
}
