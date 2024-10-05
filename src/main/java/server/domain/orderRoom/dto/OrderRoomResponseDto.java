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
        // ENTER 로 고정
        private String type;
    }

    @Data
    @Builder
    public static class OrderRoomMenuInfoListDto implements Serializable {
        private Long orderIdx;

        private Long ownerMemberIdx;

        private int maxMemberCnt;

        private int memberCnt;

        private int totalPrice;

        // MENU_INFO 로 고정
        private String type;

        private List<OrderMenuInfoDto> menuInfoList;

        @Data
        @Builder
        public static class OrderMenuInfoDto {
            private Long menuIdx;
            private String menuName;
            private int price;
            private int amount;
        }
    }

    @Data
    @Builder
    public static class OrderRoomMenuSelectionResponseDto implements Serializable {

        private Long orderIdx;

        private int currentPrice;

        private Long menuIdx;

        private Long memberIdx;

        private String menuName;

        private int amount;

        // MENU_SELECT, MENU_CANCEL 로 고정
        private String type;
    }

    @Data
    @Builder
    public static class OrderRoomReadyToPayResponseDto implements Serializable {
        private Long orderIdx;
        private Long memberIdx;
        private int totalPrice;
        private int currentPrice;
        private int readyMemberCnt;
        private int maxMemberCnt;
        private Boolean isReadyToPay;
        // READY_TO_PAY, CANCEL_READY_TO_PAY 로 고정
        private String type;
    }
}
