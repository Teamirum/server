package server.domain.market.dto;

import lombok.Builder;
import lombok.Data;

public class MarketResponseDto {

    @Data
    @Builder
    public static class MarketTaskSuccessResponseDto {
        public Boolean isSuccess;
        public Long idx;
    }

    @Data
    @Builder
    public static class MarketInfoResponseDto {
        public Long idx;
        public Long memberIdx;
        public String name;
        public String address;
        public String kakaoCid;
        public String createdAt;
        public String modifiedAt;
    }
}
