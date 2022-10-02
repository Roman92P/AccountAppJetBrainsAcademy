package account.app.service;

import account.app.exception.NegativeSalaryException;
import account.app.exception.UserNotFoundException;
import account.app.model.AcctUser;
import account.app.model.Payment;
import account.app.model.PaymentSaveUpdateDTO;
import account.app.ropository.PaymentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    ModelMapper mapper;

    private final PaymentRepo paymentRepo;
    private final AcctUserService acctUserService;

    public PaymentServiceImpl(PaymentRepo paymentRepo, AcctUserService acctUserService) {
        this.paymentRepo = paymentRepo;
        this.acctUserService = acctUserService;
    }

    @Override
    public void saveAcctUserPayment(PaymentSaveUpdateDTO payment, String requestPath) {
        Optional<AcctUser> userByEmail = acctUserService.getUserByEmail(payment.getEmployee());
        if (userByEmail.isEmpty()) {
            throw new UserNotFoundException(requestPath, payment.getEmployee());
        }
        if (payment.getSalary() <= 0) {
            throw  new NegativeSalaryException(requestPath);

        }
        Payment finalPayment = mapper.map(payment, Payment.class);
        finalPayment.setAcctUser(userByEmail.get());
        paymentRepo.save(finalPayment);
    }

    @Override
    public void updateAcctUserPayment(PaymentSaveUpdateDTO payment, String requestPath) {
        if (acctUserService.getUserByEmail(payment.getEmployee()).isEmpty()) {
            throw new UserNotFoundException(requestPath, payment.getEmployee());
        }
        if (payment.getSalary() <= 0) {
            throw  new NegativeSalaryException(requestPath);
        }
        Optional<Payment> userSalaryInParticularPeriod = paymentRepo.findUserSalaryInParticularPeriod(payment.getEmployee(), payment.getPeriod());
        if (userSalaryInParticularPeriod.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Payment payment1 = userSalaryInParticularPeriod.get();
        payment1.setSalary(payment.getSalary());
        paymentRepo.save(payment1);
    }
}
