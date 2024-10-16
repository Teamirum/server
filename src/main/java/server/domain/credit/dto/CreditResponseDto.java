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
    public static class CreditHistoryTransactionResponseDto {
        private Boolean isSuccess;
        private String cardName;
        private int cnt;
        private List<CreditHistoryInfoResponseDto> creditHistoryList;

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
    }
}
