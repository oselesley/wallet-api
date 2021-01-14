package com.internship.walletapi.mappers;

import com.internship.walletapi.dtos.SupportedCurrenciesRequestDto;
import com.internship.walletapi.dtos.SupportedCurrenciesResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesMapper implements Mapper<SupportedCurrenciesRequestDto, SupportedCurrenciesResponseDto> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SupportedCurrenciesResponseDto map(SupportedCurrenciesRequestDto supportedCurrenciesRequestDto) {
        return modelMapper.map(supportedCurrenciesRequestDto, SupportedCurrenciesResponseDto.class);
    }
}
