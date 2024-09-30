package server.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

public class OrderRequestDto {

    @Data
    public static class CreateOrderRequestDto {
        private Long marketIdx;
        private int tableNumber;
        private List<OrderMenuDto> menuList;


        @JsonCreator
        public CreateOrderRequestDto(
                @JsonProperty("marketIdx") Long marketIdx,
                @JsonProperty("menuList") List<OrderMenuDto> menuList
        ) {
            this.marketIdx = marketIdx;
            this.menuList = menuList;
        }

    }

    @Data
    public static class UpdateOrderRequestDto {
        private Long orderIdx;
        private List<OrderMenuDto> menuList;

        @JsonCreator
        public UpdateOrderRequestDto(
                @JsonProperty("orderIdx") Long orderIdx,
                @JsonProperty("menuList") List<OrderMenuDto> menuList
        ) {
            this.orderIdx = orderIdx;
            this.menuList = menuList;
        }
    }

    @Data
    public static class OrderMenuDto {
        private Long menuIdx;
        private int amount;

        @JsonCreator
        public OrderMenuDto(
                @JsonProperty("menuIdx") Long menuIdx,
                @JsonProperty("amount") int amount
        ) {
            this.menuIdx = menuIdx;
            this.amount = amount;
        }
    }
}
