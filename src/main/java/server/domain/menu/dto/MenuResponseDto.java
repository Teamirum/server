package server.domain.menu.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

public class MenuResponseDto {

    @Data
    @Builder
    public static class AllMenuInfoResponseDto {
        private int totalCnt;
        private List<MenuInfoResponseDto> menuList;
        private Boolean isSuccess;
    }

    @Data
    @Builder
    public static class MenuInfoResponseDto {
        private Long idx;
        private Long marketIdx;
        private String name;
        private int price;
        private String menuType;
    }

    @Data
    @Builder
    public static class MenuTaskSuccessResponseDto {
        private Long idx;
        private Boolean isSuccess;
    }
}
