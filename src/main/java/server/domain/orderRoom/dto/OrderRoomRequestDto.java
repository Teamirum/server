package server.domain.orderRoom.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
    public static class EnterOrderRoomRequestDto {
        private String memberId;
        private Long orderIdx;

        @JsonCreator
        public EnterOrderRoomRequestDto(
                @JsonProperty("memberIdx") String memberId,
                @JsonProperty("orderIdx") Long orderIdx
        ) {
            this.memberId = memberId;
            this.orderIdx = orderIdx;
        }
    }

    @Data
    public static class SelectMenuRequestDto {
        private Long orderIdx;
        private Long menuIdx;
        private String memberId;
        private String menuName;
        private int menuPrice;
        private int amount;

        @JsonCreator
        public SelectMenuRequestDto(
                @JsonProperty("orderIdx") Long orderIdx,
                @JsonProperty("menuIdx") Long menuIdx,
                @JsonProperty("memberId") String memberId,
                @JsonProperty("menuName") String menuName,
                @JsonProperty("menuPrice") int menuPrice,
                @JsonProperty("amount") int amount
        ) {
            this.orderIdx = orderIdx;
            this.menuIdx = menuIdx;
            this.memberId = memberId;
            this.menuName = menuName;
            this.menuPrice = menuPrice;
            this.amount = amount;
        }
    }

    @Data
    public static class SelectPriceRequestDto {
        private Long orderIdx;
        private String memberId;
        private int totalPrice;
        private int memberCnt;
        List<MemberPriceInfoDto> memberPriceInfoList;

        @JsonCreator
        public SelectPriceRequestDto(
                @JsonProperty("orderIdx") Long orderIdx,
                @JsonProperty("memberId") String memberId,
                @JsonProperty("totalPrice") int totalPrice,
                @JsonProperty("memberCnt") int memberCnt,
                @JsonProperty("memberPriceInfoList") List<MemberPriceInfoDto> memberPriceInfoList
        ) {
            this.orderIdx = orderIdx;
            this.memberId = memberId;
            this.totalPrice = totalPrice;
            this.memberCnt = memberCnt;
            this.memberPriceInfoList = memberPriceInfoList;
        }

    }

    @Data
    public static class ReadyOrderRoomRequestDto {
        private Long orderIdx;
        private String memberId;

        @JsonCreator
        public ReadyOrderRoomRequestDto(
                @JsonProperty("orderIdx") Long orderIdx,
                @JsonProperty("memberId") String memberId
        ) {
            this.orderIdx = orderIdx;
            this.memberId = memberId;
        }
    }

    @Data
    public static class MemberPriceInfoDto {
        private Long memberIdx;
        private int price;

        @JsonCreator
        public MemberPriceInfoDto(
                @JsonProperty("memberIdx") Long memberIdx,
                @JsonProperty("price") int price
        ) {
            this.memberIdx = memberIdx;
            this.price = price;
        }
    }



}
