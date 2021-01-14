package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequestDto {
    @Email(message = "Must be a valid email!")
    private String  email;
    @NotBlank
    @Size(min = 10)
    private String password;
    @NotBlank
    private String userName;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String role;
    @NotBlank
    private String mainCurrency;
}
