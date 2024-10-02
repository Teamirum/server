package server.domain.transaction.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

public class TransactionRequestDto {

    @Data
    @Builder
    public static class UploadTransactionRequestDto {
        private Long creditIdx;
        private Long accountIdx;
        private String payMethod;
        private Integer amount;
        private String memo;
        private String category;
        private String tranId;

        @JsonCreator
        public UploadTransactionRequestDto(
                @JsonProperty("creditIdx") Long creditIdx,
                @JsonProperty("accountIdx") Long accountIdx,
                @JsonProperty("payMethod") String payMethod,
                @JsonProperty("amount") Integer amount,
                @JsonProperty("memo") String memo,
                @JsonProperty("category") String category,
                @JsonProperty("tranId") String tranId
        ) {
            this.creditIdx = creditIdx;
            this.accountIdx = accountIdx;
            this.payMethod = payMethod;
            this.amount = amount;
            this.memo = memo;
            this.category = category;
            this.tranId = tranId;
        }
    }
}