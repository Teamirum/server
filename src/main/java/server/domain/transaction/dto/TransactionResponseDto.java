package server.domain.transaction.dto;

import lombok.Builder;
import lombok.Data;
import server.domain.transaction.domain.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionResponseDto {

    @Data
    @Builder
    public static class TransactionTaskSuccessResponseDto {
        private Boolean isSuccess;
        private Long transactionId;
    }

    @Data
    @Builder
    public static class TransactionListResponseDto {
        private int count;
        private List<TransactionInfoResponseDto> transactionList;
        private Boolean isSuccess;
    }

    @Data
    @Builder
    public static class TransactionInfoResponseDto {
        private Long id;
        private Long creditIdx;  // 연관된 신용 카드의 ID
        private Long accountIdx; // 계좌 ID
        private LocalDateTime time; // 거래 시간
        private Transaction.PayMethod payMethod; // 결제 방식
        private int amount; // 거래 금액
        private String memo; // 메모
        private Transaction.Category category; // 거래 카테고리
        private String tranId; // 거래 ID
    }
}
