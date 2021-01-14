package com.internship.walletapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "monies")
@Table(name = "monies")
public class Money extends BaseModel{
    @ManyToOne
    private User user;

    @Column(nullable = false, name = "currency")
    private String currency;

    @Column(nullable = false, name = "amount")
    private double amount;
}
