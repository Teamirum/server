package server.domain.market.dto;

import lombok.Data;

public class MarketRequestDto {

    @Data
    public static class MarketCreateRequestDto {
        private Long memberIdx;
        private String name;
        private String address;
        private String kakaoCid;
    }
}
