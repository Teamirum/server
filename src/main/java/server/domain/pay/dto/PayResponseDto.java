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

    @Data
    @Builder
    public static class PaySuccessResponseDto {
        Long idx;
        Long memberIdx;
        Long orderIdx;
        int price;
        String bankName;
        String accountNumber;
        String creditName;
        String creditNumber;
        String payType;
        String payStatus;
        String createdAt;
        Boolean isSuccess;
    }

    @Data
    @Builder
    public static class PayListResponseDto {
        List<TogetherPayInfoResponseDto> togetherPayList;
        List<TogetherPayInfoResponseDto> alonePayList;
        int togetherPayCnt;
        int alonePayCnt;
        int totalCnt;
        Boolean isSuccess;
    }

    @Data
    @Builder
    public static class TogetherPayInfoResponseDto {
        Long idx;
        Long orderIdx;
        Long marketIdx;
        int price;
        String bankName;
        String accountNumber;
        String creditName;
        String creditNumber;
        String orderName;
        String payMethod;
        String tid;
        String payStatus;
        String payType;
        String createdAt;
        Boolean isSuccess;
    }

}
