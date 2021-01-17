package com.internship.walletapi.repositories;

import com.internship.walletapi.models.Money;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoneyRepository extends JpaRepository<Money, Long> {
    @Query(value = "SELECT * FROM MONIES m WHERE m.user_id = :userId AND m.currency = :currency", nativeQuery = true)
    Optional<Money> getMoneyByUserIdAndCurrency (
            @Param("userId") Long userId,
            @Param("currency") String currency);

    @Query(value = "SELECT * FROM MONIES m WHERE m.user_id = :userId", nativeQuery = true)
    List<Money> getMoneyByUser (@Param("userId") Long userId);
}
