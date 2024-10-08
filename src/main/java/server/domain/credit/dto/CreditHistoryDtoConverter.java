package server.domain.credit.dto;

import server.domain.credit.domain.CreditHistory;

import java.util.List;

public class CreditHistoryDtoConverter {

    public static CreditHistoryResponseDto.CreditHistoryInfoResponseDto convertToCreditHistoryInfoResponseDto(CreditHistory creditHistory) {
    return CreditHistoryResponseDto.CreditHistoryInfoResponseDto.builder()
            .idx(creditHistory.getIdx())
            .creditIdx(creditHistory.getCreditIdx())
            .creditNumber(creditHistory.getCreditNumber())
            .name(creditHistory.getName())
            .amount(Math.toIntExact(creditHistory.getAmount()))
            .amountSum(Math.toIntExact(creditHistory.getAmountSum()))
            .createdAt(creditHistory.getCreatedAt().toString())
            .build();
}

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
                        .toList())
                .isSuccess(true)
                .build();
    }


}
