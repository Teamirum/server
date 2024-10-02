package server.domain.credit.dto;

import server.domain.credit.domain.Credit;
import server.domain.transaction.domain.Transaction;

import java.util.List;
import java.util.stream.Collectors;

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

    // 카드 이름과 거래 내역을 반환하는 메소드
    public static CreditResponseDto.CreditTransactionResponseDto convertToCreditTransactionResponseDto(Credit credit, List<Transaction> transactions) {
        List<CreditResponseDto.TransactionDto> transactionDtos = transactions.stream().map(transaction -> {
            return CreditResponseDto.TransactionDto.builder()
                    .id(transaction.getIdx())
                    .transactionDate(transaction.getTime().toLocalDate().toString())
                    .amount(transaction.getAmount())
                    .description(transaction.getMemo()) // memo를 description으로 변환
                    .build();
        }).collect(Collectors.toList());

        return CreditResponseDto.CreditTransactionResponseDto.builder()
                .cardName(credit.getCreditName())
                .transactions(transactionDtos)
                .build();
    }

    public static CreditResponseDto.CreditTransactionResponseDto convertToCreditTransactionResponseDto(List<Transaction> transactions, String cardName) {
        List<CreditResponseDto.TransactionDto> transactionInfoList = transactions.stream()
                .map(transaction -> CreditResponseDto.TransactionDto.builder()
                        .id(transaction.getIdx())
                        .transactionDate(String.valueOf(transaction.getTime())) // Assuming transaction.getTime() is of String type
                        .amount(transaction.getAmount())
                        .description(transaction.getMemo()) // Assuming transaction.getMemo() is description
                        .build())
                .toList();

        return CreditResponseDto.CreditTransactionResponseDto.builder()
                .isSuccess(true)
                .cardName(cardName)
                .transactions(transactionInfoList)
                .build();
    }
}
