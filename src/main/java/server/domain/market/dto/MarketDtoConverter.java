package server.domain.market.dto;

import server.domain.market.domain.Market;

public class MarketDtoConverter {

    public static MarketResponseDto.MarketInfoResponseDto convertToMarketInfoResponseDto(Market market) {
        return MarketResponseDto.MarketInfoResponseDto.builder()
                .idx(market.getIdx())
                .memberIdx(market.getMemberIdx())
                .name(market.getName())
                .address(market.getAddress())
                .maxTableCnt(market.getMaxTableCnt())
                .createdAt(market.getCreatedAt().toString())
                .modifiedAt(market.getModifiedAt().toString())
                .build();
    }
}
