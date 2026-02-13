package com.payment.account.repository;

import com.payment.account.model.AsyncErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsyncErrorLogRepository extends JpaRepository<AsyncErrorLog, Long> {
}
