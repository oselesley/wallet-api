package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class Error {
    private int code;
    private String type;
    private String info;
}
