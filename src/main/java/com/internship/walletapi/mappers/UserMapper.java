package com.internship.walletapi.mappers;

import com.internship.walletapi.dtos.SignupRequestDto;
import com.internship.walletapi.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<SignupRequestDto, User> {
    @Autowired
    private ModelMapper mapper;

    @Override
    public User map(SignupRequestDto signupRequestDto) {
        return mapper.map(signupRequestDto, User.class);
    }
}
