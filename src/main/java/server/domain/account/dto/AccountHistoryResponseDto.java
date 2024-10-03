package server.domain.account.dto;

import lombok.Builder;
import lombok.Data;
import server.domain.account.domain.AccountHistory;

import java.util.List;

public class AccountHistoryResponseDto {

    @Data
    @Builder
    public static class AccountHistoryListResponseDto {
        private boolean isSuccess;  // 요청 성공 여부
        private int cnt;            // 총 거래 내역 수
        private List<AccountHistoryInfoResponseDto> accountHistoryList; // 거래 내역 리스트
    }

    @Data
    @Builder
    public static class AccountHistoryInfoResponseDto {
        private Long idx;                       // 거래 내역 ID
        private Long accountIdx;                // 연결된 계좌 ID
        private AccountHistory.AccountHistoryType accountHistoryType;     // 거래 구분 (SEND 또는 GET)
        private Long amount;                    // 거래 금액
        private Long remainAmount;              // 거래 후 잔액
        private String createdAt;               // 거래 발생 시간
        private String name;                    // 거래 상대방 이름
    }

    @Data
    @Builder
    public static class AccountHistoryTransactionResponseDto {
        private String accountHolderName;                  // 계좌 소유자 이름
        private List<AccountHistoryInfoResponseDto> accountHistoryList; // 거래 내역 리스트
    }
}
