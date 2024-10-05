package server.domain.orderRoom.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

public class OrderRoomRequestDto {

    @Builder
    @Data
    public static class CreateOrderRoomRequestDto {
        private Long orderIdx;
        private int maxMemberCnt;
    }

    @Builder
    @Data
    public static class EnterOrderRoomRequestDto implements Serializable {
        private Long orderIdx;
        private String type;
    }

    @Builder
    @Data
    public static class StartSelectOrderMenuRequestDto implements Serializable {
        private Long orderIdx;
        private int memberCnt;
    }
}
