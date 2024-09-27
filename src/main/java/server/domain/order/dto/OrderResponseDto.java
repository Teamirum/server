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
        public int amount;
        public int taxFreeAmount;
        public int vatAmount;
        public Long orderMemberIdx;
        public String createdAt;
    }

    @Data
    @Builder
    public static class OrderTaskSuccessResponseDto {
        public Long idx;
        public Boolean isSuccess;
    }
}
