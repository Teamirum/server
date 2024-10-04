package server.domain.account.dto;

import server.domain.account.domain.AccountHistory;

import java.util.List;

public class AccountHistoryDtoConverter {

    public static AccountHistoryResponseDto.AccountHistoryInfoResponseDto convertToAccountHistoryInfoResponseDto(AccountHistory accountHistory) {
        return AccountHistoryResponseDto.AccountHistoryInfoResponseDto.builder()
                .idx(accountHistory.getIdx())
                .accountIdx(accountHistory.getAccountIdx())
                .accountHistoryType(AccountHistory.AccountHistoryType.valueOf(accountHistory.getAccountHistoryType().toString()))
                .amount(accountHistory.getAmount())
                .remainAmount(accountHistory.getRemainAmount())
                .createdAt(accountHistory.getCreatedAt().toString())
                .name(accountHistory.getName())
                .build();
    }

    public static AccountHistoryResponseDto.AccountHistoryListResponseDto convertToAccountHistoryListResponseDto(List<AccountHistory> accountHistories) {
        if (accountHistories == null) {
            return AccountHistoryResponseDto.AccountHistoryListResponseDto.builder()
                    .cnt(0)
                    .isSuccess(false)
                    .build();
        }
        return AccountHistoryResponseDto.AccountHistoryListResponseDto.builder()
                .cnt(accountHistories.size())
                .accountHistoryList(accountHistories.stream().map(AccountHistoryDtoConverter::convertToAccountHistoryInfoResponseDto).toList())
                .isSuccess(true)
                .build();
    }
}
