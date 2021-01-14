package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AuthResponseDto {
    public AuthResponseDto(String token) {
        this.token = token;
    }

    private final String token;
}
