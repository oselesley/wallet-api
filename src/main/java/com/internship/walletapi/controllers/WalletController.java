package com.internship.walletapi.controllers;

import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.models.Role;
import com.internship.walletapi.models.User;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.services.WalletService;
import com.internship.walletapi.utils.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.internship.walletapi.enums.UserRole.*;
import static com.internship.walletapi.utils.ApiResponseBuilder.*;
import static org.springframework.http.HttpStatus.*;


// "%s %s has been successfully deposited", trd.getAmount(), trd.getCurrency()
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @ResponseStatus(CREATED)
    @PostMapping("/deposit")
    private ResponseEntity<ApiResponse<String>> deposit (@RequestBody TransactionRequestDto trd) {
        walletService.deposit(trd, generateNewUser());
        return buildResponseEntity(new ApiResponse<>("deposit transaction initiated successfully!", CREATED));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/withdraw")
    private ResponseEntity<ApiResponse<String>> withdraw (@RequestBody TransactionRequestDto trd)  {
        walletService.withDraw(1L, trd);
        return buildResponseEntity(new ApiResponse<>("withdraw transaction completed!", CREATED));
    }


    private User generateNewUser() {
        Role role = new Role();
        role.setRole(ELITE);
        role.setAuthorities(ELITE.getGrantedAuthorities());
        User user = new User();
        user.setId(1L);
        user.setLastName("Onibokun");
        user.setFirstName("Adeyemi");
        user.setMainCurrency("NGN");
        user.setEmail("adeyemionibokun@gmail.com");
        user.setUsername("adehimself");
        user.setUserRole(role);

        return user;
    }

    private User generateNewAdmin() {
        Role role = new Role();
        role.setRole(ADMIN);
        role.setAuthorities(ADMIN.getGrantedAuthorities());
        User user = new User();
        user.setId(1L);
        user.setLastName("osereme");
        user.setFirstName("okoduwa");
        user.setMainCurrency("NGN");
        user.setEmail("leslieokoduwa@gmail.com");
        user.setUsername("lesliej");
        user.setUserRole(role);
        return user;
    }
}
