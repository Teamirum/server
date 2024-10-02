package server.domain.credit.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

public class CreditHistoryRequestDto {

    //신용카드 거래 내역 업로드
    @Data
    public static class UploadCreditHistoryRequestDto {
        private Long creditIdx;
        private Long amount;
        private String name;
        private Long amountSum;

        @JsonCreator
        public UploadCreditHistoryRequestDto(
                @JsonProperty("creditIdx") Long creditIdx,
                @JsonProperty("amount") Long amount,
                @JsonProperty("name") String name,
                @JsonProperty("amountSum") Long amountSum
        ) {
            this.creditIdx = creditIdx;
            this.amount = amount;
            this.name = name;
            this.amountSum = amountSum;
        }
    }

}
