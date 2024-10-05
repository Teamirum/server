package server.domain.orderRoom.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

public class OrderRoomRequestDto {

    @Data
    public static class CreateOrderRoomRequestDto {
        private Long orderIdx;
        private int maxMemberCnt;
        // BY_MONEY, BY_MENU 둘 중 하나
        private String type;

        @JsonCreator
        public CreateOrderRoomRequestDto(
                @JsonProperty("orderIdx") Long orderIdx,
                @JsonProperty("maxMemberCnt") int maxMemberCnt,
                @JsonProperty("type") String type
        ) {
            this.orderIdx = orderIdx;
            this.maxMemberCnt = maxMemberCnt;
            this.type = type;
        }


    }

    @Data
    public static class SelectMenuRequestDto {
        private Long orderIdx;
        private Long menuIdx;
        private String menuName;
        private int menuPrice;
        private int amount;

        @JsonCreator
        public SelectMenuRequestDto(
                @JsonProperty("orderIdx") Long orderIdx,
                @JsonProperty("menuIdx") Long menuIdx,
                @JsonProperty("menuName") String menuName,
                @JsonProperty("menuPrice") int menuPrice,
                @JsonProperty("amount") int amount
        ) {
            this.orderIdx = orderIdx;
            this.menuIdx = menuIdx;
            this.menuName = menuName;
            this.menuPrice = menuPrice;
            this.amount = amount;
        }
    }



}
