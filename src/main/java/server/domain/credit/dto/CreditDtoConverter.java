package server.domain.credit.dto;

import server.domain.credit.domain.Credit;

import java.util.List;

public class CreditDtoConverter {

    public static CreditResponseDto.CreditListResponseDto convertToCreditListResponseDto(List<Credit> credit) {
        if (credit == null) {
            return CreditResponseDto.CreditListResponseDto.builder()
                    .cnt(0)
                    .isSuccess(false)
                    .build();
        }
        return CreditResponseDto.CreditListResponseDto.builder()
                .cnt(credit.size())
                .creditList(credit.stream().map(CreditDtoConverter::converToCreditInfoResponseDto).toList())
                .isSuccess(true)
                .build();
    }

    public static CreditResponseDto.CreditInfoResponseDto converToCreditInfoResponseDto(Credit credit) {
        return CreditResponseDto.CreditInfoResponseDto.builder()
                .idx(credit.getIdx())
                .creditNumber(credit.getCreditNumber())
                .creditName(credit.getCreditName())
                .companyName(credit.getCompanyName())
                .amountSum(credit.getAmountSum())
                .expirationDate(credit.getExpirationDate())
                .imgUrl(credit.getImgUrl())
                .createdAt(credit.getCreatedAt().toString())
                .build();
    }
}
