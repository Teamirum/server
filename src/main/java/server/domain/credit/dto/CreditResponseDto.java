package server.domain.credit.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class CreditResponseDto {

    @Data
    @Builder
    public static class CreditTaskSuccessResponseDto {
        public Boolean isSuccess;
        public Long idx;
    }

    @Data
    @Builder
    public static class CreditListResponseDto {
        public int cnt;
        List<CreditInfoResponseDto> creditList;
        public Boolean isSuccess;
    }

    @Data
    @Builder
    public static class CreditInfoResponseDto {
        public Long idx;
        public String creditNumber;
        public String creditName;
        public String companyName;
        public int amountSum;
        public String expirationDate;
        public String imgUrl;
        public String createdAt;
    }

    @Data
    @Builder
    public static class CreditTransactionResponseDto {
        private Boolean isSuccess;
        private String cardName; // 카드 이름 추가
        private List<TransactionDto> transactions; // 수정: transactions 필드 추가
    }

    @Data
    @Builder
    public static class TransactionDto {
        private Long id;
        private String transactionDate;
        private int amount;
        private String description;
    }
}
