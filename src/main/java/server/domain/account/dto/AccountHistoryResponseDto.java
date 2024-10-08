package server.domain.account.dto;

import lombok.Builder;
import lombok.Data;
import server.domain.account.domain.AccountHistory;

import java.util.List;

public class AccountHistoryResponseDto {

    @Data
    @Builder
    public static class AccountHistoryListResponseDto {
        private boolean isSuccess;
        private int cnt;
        private List<AccountHistoryInfoResponseDto> accountHistoryList;
    }

    @Data
    @Builder
    public static class AccountHistoryInfoResponseDto {
        private Long idx;
        private Long accountIdx;
        private Long accountNumber;
        private AccountHistory.AccountHistoryType accountHistoryType;
        private Long amount;
        private Long remainAmount;
        private String createdAt;
        private String name;
    }

    @Data
    @Builder
    public static class AccountHistoryTransactionResponseDto {
        private String accountHolderName;
        private List<AccountHistoryInfoResponseDto> accountHistoryList;
    }
}
