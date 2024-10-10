package server.domain.pay.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

public class PayRequestDto {

    @Data
    public static class StartPayRequestDto {
        Long orderIdx;
        Long accountIdx;
        Long creditIdx;
        String payMethod;

        @JsonCreator
        public StartPayRequestDto(
                @JsonProperty("orderIdx") Long orderIdx,
                @JsonProperty("accountIdx") Long accountIdx,
                @JsonProperty("creditIdx") Long creditIdx,
                @JsonProperty("payMethod") String payMethod
        ) {
            this.orderIdx = orderIdx;
            this.accountIdx = accountIdx;
            this.creditIdx = creditIdx;
            this.payMethod = payMethod;
        }
    }
}
