package server.domain.account.dto;

import server.domain.account.domain.Account;
import server.domain.member.domain.Member;

import java.util.List;

public class AccountDtoConverter {

    public static AccountResponseDto.AccountInfoResponseDto converToAccountInfoResponseDto(Account account) {
        return AccountResponseDto.AccountInfoResponseDto.builder()
                .idx(account.getIdx())
                .accountHolderName(account.getAccountHolderName())
                .amount(account.getAmount())
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .createdAt(account.getCreatedAt().toString())
                .build();
    }

    public static AccountResponseDto.AccountListResponseDto convertToAccountListResponseDto(List<Account> account) {
        if (account == null) {
            return AccountResponseDto.AccountListResponseDto.builder()
                    .cnt(0)
                    .isSuccess(false)
                    .build();
        }
        return AccountResponseDto.AccountListResponseDto.builder()
                .cnt(account.size())
                .accountList(account.stream().map(AccountDtoConverter::converToAccountInfoResponseDto).toList())
                .isSuccess(true)
                .build();
    }




}
