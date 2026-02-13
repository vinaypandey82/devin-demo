package com.payment.account.service;

import com.payment.account.entity.Account;
import com.payment.account.exception.AccountLockedException;
import com.payment.account.exception.AccountNotFoundException;
import com.payment.account.exception.InsufficientFundsException;
import com.payment.account.repository.AccountRepository;
import jakarta.persistence.PessimisticLockException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void performBalanceMove(int srcId, int destId, BigDecimal amount) {
        Account source;
        try {
            source = accountRepository.findByIdWithLock(srcId)
                    .orElseThrow(() -> new AccountLockedException("Source account not found: " + srcId));
        } catch (PessimisticLockException e) {
            throw new AccountLockedException("Source account locked: " + srcId);
        }

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + srcId);
        }

        Account dest;
        try {
            dest = accountRepository.findByIdWithLock(destId)
                    .orElseThrow(() -> new AccountNotFoundException("Destination account not found: " + destId));
        } catch (PessimisticLockException e) {
            throw new AccountLockedException("Destination account locked: " + destId);
        }

        source.setBalance(source.getBalance().subtract(amount));
        dest.setBalance(dest.getBalance().add(amount));

        List<Account> updated = Stream.of(source, dest).toList();
        accountRepository.saveAll(updated);
    }
}
