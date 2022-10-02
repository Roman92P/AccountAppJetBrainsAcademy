package account.app.service;

import account.app.model.Payment;
import account.app.model.PaymentSaveUpdateDTO;

public interface PaymentService {

    void saveAcctUserPayment(PaymentSaveUpdateDTO payment, String requestPath);

    void updateAcctUserPayment(PaymentSaveUpdateDTO payment, String requestPath);
}
