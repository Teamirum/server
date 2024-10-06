package server.domain.pay.dto;

import lombok.Builder;
import lombok.Data;
import server.domain.account.dto.AccountResponseDto;
import server.domain.credit.dto.CreditResponseDto;

import java.util.List;

public class PayResponseDto {

    @Data
    @Builder
    public static class PayMethodResponseDto {
        CreditResponseDto.CreditListResponseDto credit;
        AccountResponseDto.AccountListResponseDto account;
    }
}
