package server.domain.transaction.dto;

import server.domain.transaction.domain.Transaction;  // Transaction 도메인 클래스
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TransactionDtoConverter {

    // 거래 내역 리스트를 DTO로 변환하는 메서드
    public static TransactionResponseDto.TransactionListResponseDto convertToTransactionListResponseDto(List<Transaction> transaction) {
        if (transaction == null || transaction.isEmpty()) {
            return TransactionResponseDto.TransactionListResponseDto.builder()
                    .count(0)
                    .isSuccess(false)
                    .transactionList(List.of())
                    .build();
        }

        return TransactionResponseDto.TransactionListResponseDto.builder()
                .count(transaction.size())
                .isSuccess(true)
                .transactionList(transaction.stream().map(TransactionDtoConverter::convertToTransactionInfoResponseDto).collect(toList()))
                .build();
    }

    // 단일 거래를 DTO로 변환하는 메서드
    public static TransactionResponseDto.TransactionInfoResponseDto convertToTransactionInfoResponseDto(Transaction transaction) {
        return TransactionResponseDto.TransactionInfoResponseDto.builder()
                .idx(transaction.getIdx())
                .creditIdx(transaction.getCreditIdx())
                .accountIdx(transaction.getAccountIdx())
                .time(transaction.getTime())
                .amount(transaction.getAmount())
                .memo(transaction.getMemo())
                .payMethod(transaction.getPayMethod())
                .category(transaction.getCategory())
                .build();
    }
}
