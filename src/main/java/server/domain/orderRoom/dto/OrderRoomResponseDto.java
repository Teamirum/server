package server.domain.orderRoom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.global.apiPayload.code.status.ErrorStatus;

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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnterOrderRoomResponseDto implements Serializable {
        private Long orderIdx;
        private Long ownerMemberIdx;
        private Long memberIdx;
        private int maxMemberCnt;
        private int memberCnt;
        private Boolean isFull;
        private String roomType;
        // ENTER 로 고정
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true) // 추가된 어노테이션
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
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class OrderMenuInfoDto {
            private Long menuIdx;
            private String menuName;
            private int price;
            private int amount;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
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
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StartPayResponseDto implements Serializable {
        private Long orderIdx;
        private int totalPrice;
        private int currentPrice;
        private int maxMemberCnt;
        private Boolean canStartToPay;
        // START_PAY 로 고정
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorResponseDto implements Serializable {
        private Long memberIdx;
        private Long orderIdx;
        private String status;
        private String code;
        private String message;
        private String type;
        private Boolean isSuccess = false;

        public ErrorResponseDto(Long memberIdx, Long orderIdx, ErrorStatus errorStatus) {
            this.memberIdx = memberIdx;
            this.orderIdx = orderIdx;
            this.status = errorStatus.getHttpStatus().toString();
            this.code = errorStatus.getCode();
            this.message = errorStatus.getMessage();
            this.type = "ERROR";
        }

        @Override
        public String toString() {
            return "{" +
                    "\"memberIdx\" : " + memberIdx +
                    "\"orderIdx\" : " + orderIdx +
                    "\"isSuccess\" : \"" + isSuccess + "\"" +
                    ", status: \"" + status + "\"" +
                    ", code: \"" + code + "\"" +
                    ", message: \"" + message + "\"" +
                    "}";
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderRoomPriceSelectionResponseDto implements Serializable {
        private Long orderIdx;
        List<OrderRoomMemberPriceSelectionInfoDto> memberPriceInfoList;
        // READY_TO_PAY, CANCEL_READY_TO_PAY 로 고정
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderRoomMemberPriceSelectionInfoDto implements Serializable {
        private Long memberIdx;
        private int price;
    }
}
