package server.domain.menu.dto;


import lombok.Builder;
import lombok.Data;

public class MenuResponseDto {

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
