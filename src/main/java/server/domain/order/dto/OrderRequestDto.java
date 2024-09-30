package server.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class OrderRequestDto {

    @Data
    public static class CreateOrderRequestDto {
        public Long marketIdx;
        public String name;
        public int memberCnt;
        public int amount;

        @JsonCreator
        public CreateOrderRequestDto(
                @JsonProperty("marketIdx") Long marketIdx,
                @JsonProperty("name") String name,
                @JsonProperty("memberCnt") int memberCnt,
                @JsonProperty("amount") int amount
        ) {
            this.marketIdx = marketIdx;
            this.name = name;
            this.memberCnt = memberCnt;
            this.amount = amount;
        }

    }

    @Data
    public static class OrderMenuDto {
        public Long menuIdx;
        public int cnt;

        @JsonCreator
        public OrderMenuDto(
                @JsonProperty("menuIdx") Long menuIdx,
                @JsonProperty("cnt") int cnt
        ) {
            this.menuIdx = menuIdx;
            this.cnt = cnt;
        }
    }
}
