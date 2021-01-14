package com.internship.walletapi.services;

import com.internship.walletapi.dtos.AuthResponseDto;
import com.internship.walletapi.dtos.SignupRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    void signup (SignupRequestDto dto);

     AuthResponseDto signin();
}
