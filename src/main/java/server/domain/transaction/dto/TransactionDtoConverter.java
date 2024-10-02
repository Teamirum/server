package server.domain.transaction.dto;

import server.domain.transaction.domain.Transaction;  // Transaction 도메인 클래스
import java.util.List;
import java.util.stream.Collectors;

public class TransactionDtoConverter {

    // 거래 내역 리스트를 DTO로 변환하는 메서드
    public static TransactionResponseDto.TransactionListResponseDto convertToTransactionListResponseDto(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return TransactionResponseDto.TransactionListResponseDto.builder()
                    .count(0)
                    .isSuccess(false)
                    .transactionList(List.of())
                    .build();
        }

        return TransactionResponseDto.TransactionListResponseDto.builder()
                .count(transactions.size())
                .isSuccess(true)
                .transactionList(transactions.stream().map(TransactionDtoConverter::convertToTransactionInfoResponseDto).collect(Collectors.toList()))
                .build();
    }

    // 단일 거래를 DTO로 변환하는 메서드
    public static TransactionResponseDto.TransactionInfoResponseDto convertToTransactionInfoResponseDto(Transaction transaction) {
        return TransactionResponseDto.TransactionInfoResponseDto.builder()
                .id(transaction.getIdx())
                .creditIdx(transaction.getCreditIdx()) // 연관된 신용 카드 ID
                .time(transaction.getTime()) // 거래 시간
                .amount(transaction.getAmount())
                .memo(transaction.getMemo()) // 설명
                .payMethod(transaction.getPayMethod()) // 결제 방법
                .category(transaction.getCategory()) // 거래 카테고리
                .build();
    }
}