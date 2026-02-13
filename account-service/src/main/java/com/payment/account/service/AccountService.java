package com.payment.account.service;

import com.payment.account.dto.BalanceMoveResponse;
import com.payment.account.model.Account;
import com.payment.account.model.AsyncErrorLog;
import com.payment.account.repository.AccountRepository;
import com.payment.account.repository.AsyncErrorLogRepository;
import jakarta.persistence.PessimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AsyncErrorLogRepository asyncErrorLogRepository;

    public AccountService(AccountRepository accountRepository,
                          AsyncErrorLogRepository asyncErrorLogRepository) {
        this.accountRepository = accountRepository;
        this.asyncErrorLogRepository = asyncErrorLogRepository;
    }

    @Transactional
    public BalanceMoveResponse performBalanceMove(Integer sourceId, Integer destinationId, Double amount) {
        Optional<Account> sourceOpt;
        try {
            sourceOpt = accountRepository.findByAccIdForUpdate(sourceId);
        } catch (PessimisticLockException e) {
            return new BalanceMoveResponse(-1, "Account locked by another process");
        }

        if (sourceOpt.isEmpty()) {
            return new BalanceMoveResponse(-1, "Source account not found");
        }

        Account source = sourceOpt.get();

        if (source.getBalance() < amount) {
            return new BalanceMoveResponse(-2, "Insufficient funds");
        }

        Optional<Account> destOpt = accountRepository.findById(destinationId);
        if (destOpt.isEmpty()) {
            return new BalanceMoveResponse(-3, "Destination account not found");
        }

        Account destination = destOpt.get();

        source.setBalance(source.getBalance() - amount);
        accountRepository.save(source);

        destination.setBalance(destination.getBalance() + amount);
        accountRepository.save(destination);

        return new BalanceMoveResponse(0, "Balance move successful");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAsyncError(String reqId, Integer accId, String errorDesc) {
        AsyncErrorLog log = new AsyncErrorLog(reqId, accId, errorDesc, LocalDateTime.now());
        asyncErrorLogRepository.save(log);
    }
}
