package server.domain.orderRoom.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

public class OrderRoomDataDto {

    @Data
    public static class OrderMenuListDto implements Serializable {
        String marketIdx;
        String marketName;
        int totalPrice;
        int totalMenuCount;
        int memberCnt;
        int maxMemberCnt;
        List<OrderMenuInfoDto> orderMenuInfoList;
    }

    @Data
    public static class OrderMenuInfoDto implements Serializable {
        Long menuIdx;
        String menuName;
        int menuPrice;
        int menuCount;
    }

    @Data
    public static class
}
