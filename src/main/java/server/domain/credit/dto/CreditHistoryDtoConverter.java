package server.domain.credit.dto;

import server.domain.credit.domain.CreditHistory;
import server.domain.credit.domain.Credit;

import java.util.List;
import java.util.stream.Collectors;

public class CreditHistoryDtoConverter {

    public static CreditHistoryResponseDto.CreditHistoryListResponseDto convertToCreditHistoryListResponseDto(List<CreditHistory> creditHistories) {
        if (creditHistories == null) {
            return CreditHistoryResponseDto.CreditHistoryListResponseDto.builder()
                    .cnt(0)
                    .isSuccess(false)
                    .build();
        }
        return CreditHistoryResponseDto.CreditHistoryListResponseDto.builder()
                .cnt(creditHistories.size())
                .creditHistoryList(creditHistories.stream()
                        .map(CreditHistoryDtoConverter::convertToCreditHistoryInfoResponseDto)
                        .collect(Collectors.toList()))
                .isSuccess(true)
                .build();
    }

    public static CreditHistoryResponseDto.CreditHistoryInfoResponseDto convertToCreditHistoryInfoResponseDto(CreditHistory creditHistory) {
        return CreditHistoryResponseDto.CreditHistoryInfoResponseDto.builder()
                .idx(creditHistory.getIdx())
                .creditIdx(creditHistory.getCreditIdx())
                .name(creditHistory.getName())
                .amount(Math.toIntExact(creditHistory.getAmount()))
                .amountSum(Math.toIntExact(creditHistory.getAmountSum()))
                .createdAt(creditHistory.getCreatedAt().toString()) // createdAt을 문자열로 변환
                .build();
    }
}
