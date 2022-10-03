package account.app.service;

import account.app.exception.NegativeSalaryException;
import account.app.exception.UserNotFoundException;
import account.app.model.AcctUser;
import account.app.model.Payment;
import account.app.model.PaymentDTO;
import account.app.model.PaymentSaveUpdateDTO;
import account.app.ropository.PaymentRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService{

    Logger logger = LogManager.getLogger(PaymentServiceImpl.class);


    private final ModelMapper mapper;

    private final PaymentRepo paymentRepo;
    private final AcctUserService acctUserService;

    public PaymentServiceImpl(ModelMapper mapper, PaymentRepo paymentRepo, AcctUserService acctUserService) {
        this.mapper = mapper;
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


        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MM-yyyy")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();
        LocalDate date = LocalDate.parse(finalPayment.getPeriod(), formatter);

        logger.warn("Final payment date: "+ date);

        paymentRepo.save(finalPayment);
    }

    @Override
    public void updateAcctUserPayment(PaymentSaveUpdateDTO payment, String requestPath) {
        if (Integer.parseInt(payment.getPeriod().substring(0,2))>12) {
            throw new DateTimeException("Incorrect date");
        }
        if (acctUserService.getUserByEmail(payment.getEmployee()).isEmpty()) {
            throw new UserNotFoundException(requestPath, payment.getEmployee());
        }
        if (payment.getSalary() <= 0) {
            throw  new NegativeSalaryException(requestPath);
        }
        Optional<Payment> userSalaryInParticularPeriod = paymentRepo.findUserSalaryInParticularPeriod(payment.getEmployee(), payment.getPeriod());
        Payment payment1 = userSalaryInParticularPeriod.get();
        payment1.setSalary(payment.getSalary());
        paymentRepo.save(payment1);
    }

    @Override
    public List<PaymentDTO> getAllAcctUserPayments(AcctUser acctUserByName) {
        List<Payment> allByEmployee = paymentRepo.findAllByEmployee(acctUserByName.getEmail());
        logger.warn("Payments from service: testing acctUser data: "+ acctUserByName);
        List<PaymentDTO> paymentDTOS = allByEmployee.stream()
                .map(p -> mapper.map(p, PaymentDTO.class))
                .collect(Collectors.toList())
                .stream()
                .map(paymentDTO -> {
                    paymentDTO.setName(paymentDTO.getAcctUser().getName());
                    paymentDTO.setLastname(paymentDTO.getAcctUser().getLastname());
                    return paymentDTO;
                }).collect(Collectors.toList());
        return paymentDTOS;
    }

    @Override
    @Transactional
    public void savePayments(List<PaymentSaveUpdateDTO> payments, String requestPath) {
        for (PaymentSaveUpdateDTO payment : payments) {
            Optional<AcctUser> userByEmail = acctUserService.getUserByEmail(payment.getEmployee());
            if (userByEmail.isEmpty()) {
                throw new UserNotFoundException(requestPath, payment.getEmployee());
            }
            if (payment.getSalary() <= 0) {
                throw  new NegativeSalaryException(requestPath);

            }
            Payment finalPayment = mapper.map(payment, Payment.class);
            finalPayment.setAcctUser(userByEmail.get());


            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("MM-yyyy")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();
            LocalDate date = LocalDate.parse(finalPayment.getPeriod(), formatter);

            logger.warn("Final payment date: "+ date);

            paymentRepo.save(finalPayment);
        }
    }
}
