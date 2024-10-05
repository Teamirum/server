package server.domain.orderRoom.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

public class OrderRoomResponseDto {

    @Data
    @Builder
    public static class CreateOrderRoomResponseDto {
        private Long orderIdx;
        private int maxMemberCnt;
        private List<OrderMenuResponseDto> orderMenuList;
        private int totalPrice;
        private int menuCnt;
        private boolean isSuccess;
    }

    @Data
    @Builder
    public static class OrderMenuResponseDto {
        private Long menuIdx;
        private String menuName;
        private int price;
        private int amount;
    }

    @Data
    @Builder
    public static class EnterOrderRoomResponseDto implements Serializable {
        private Long orderIdx;
        private Long ownerMemberIdx;
        private Long memberIdx;
        private int maxMemberCnt;
        private int memberCnt;
        private Boolean isFull;
        private String type;
    }
}
