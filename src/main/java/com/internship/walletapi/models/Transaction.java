package com.internship.walletapi.models;

import com.internship.walletapi.enums.TransactionStatus;
import com.internship.walletapi.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "transactions")
@ToString
public class Transaction extends BaseModel {
    @ManyToOne
    private User user;

    @Column(nullable = false, name = "currency")
    private String currency;

    @Column(nullable = false, name = "amount")
    private double amount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "transaction_type")
    private TransactionType type;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "transaction_status")
    private TransactionStatus status = TransactionStatus.PENDING;
}
