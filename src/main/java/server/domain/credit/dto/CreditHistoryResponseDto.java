package server.domain.credit.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class CreditHistoryResponseDto {

    @Data
    @Builder
    public static class CreditHistoryListResponseDto {
        private Boolean isSuccess;
        private int cnt;
        private List<CreditHistoryInfoResponseDto> creditHistoryList;
    }

    @Data
    @Builder
    public static class CreditHistoryInfoResponseDto {
        private Long idx;
        private Long creditIdx;
        private String name;
        private int amount;
        private int amountSum;
        private String createdAt;
    }

    @Data
    @Builder
    public static class CreditHistoryTransactionResponseDto {
        private String cardName;
        private List<CreditHistoryInfoResponseDto> creditHistoryList;
    }
}
