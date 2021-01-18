package com.internship.walletapi.repositories;

import com.internship.walletapi.enums.TransactionStatus;
import com.internship.walletapi.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllTransactionsByStatus(TransactionStatus status, Pageable pageable);
}
