package server.domain.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class OrderResponseDto {

    @Data
    @Builder
    public static class OrderListResponseDto {
        public List<OrderInfoResponseDto> orderList;
        public int totalOrderCnt;
        public Boolean isSuccess;
    }

    @Data
    @Builder
    public static class OrderInfoResponseDto {
        public Long idx;
        public Long marketIdx;
        public String name;
        public int totalPrice;
        public int taxFreePrice;
        public int vatPrice;
        public int tableNumber;
        public int menuCnt;
        public List<OrderMenuResponseDto> orderMenuList;
        public String createdAt;
    }

    @Data
    @Builder
    public static class OrderMenuResponseDto {
        public Long idx;
        public Long menuIdx;
        public String menuName;
        public int price;
        public int amount;
    }

    @Data
    @Builder
    public static class OrderTaskSuccessResponseDto {
        public Long idx;
        public Boolean isSuccess;
    }
}
