package server.domain.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class MenuRequestDto {

    @Data
    public static class CreateMenuRequestDto {
        private String name;
        private int price;
        private String menuType;

        @JsonCreator
        public CreateMenuRequestDto(
                @JsonProperty("name") String name,
                @JsonProperty("price") int price,
                @JsonProperty("menuType") String menuType
        ) {
            this.name = name;
            this.price = price;
            this.menuType = menuType;
        }
    }
}
