package server.domain.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.market.domain.Market;
import server.domain.market.dto.MarketDtoConverter;
import server.domain.market.dto.MarketRequestDto;
import server.domain.market.dto.MarketResponseDto;
import server.domain.market.repository.MarketRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;

    public MarketResponseDto.MarketTaskSuccessResponseDto registerMarket(MarketRequestDto.MarketCreateRequestDto requestDto) {
        if (marketRepository.existsByMemberIdx(requestDto.getMemberIdx())) {
            throw new ErrorHandler(ErrorStatus.MARKET_ALREADY_EXIST);
        }
        Market market = Market.builder()
                .memberIdx(requestDto.getMemberIdx())
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .kakaoCid(requestDto.getKakaoCid())
                .build();
        marketRepository.save(market);

        Market savedMarket = marketRepository.findByMemberIdx(requestDto.getMemberIdx()).orElseThrow();
        return MarketResponseDto.MarketTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedMarket.getIdx())
                .build();
    }

    public List<MarketResponseDto.MarketInfoResponseDto> getAllMarket() {
        List<Market> marketList = marketRepository.findAll();
        List<MarketResponseDto.MarketInfoResponseDto> marketInfoResponseDtoList = new ArrayList<>();
        for (Market market : marketList) {
            marketInfoResponseDtoList.add(MarketDtoConverter.convertToMarketInfoResponseDto(market));
        }
        return marketInfoResponseDtoList;
    }

    public MarketResponseDto.MarketInfoResponseDto getMarketInfo(Long marketIdx) {
        Market market = marketRepository.findByMarketIdx(marketIdx).orElseThrow(() -> new ErrorHandler(ErrorStatus.MARKET_NOT_FOUND));
        return MarketDtoConverter.convertToMarketInfoResponseDto(market);
    }

    public MarketResponseDto.MarketInfoResponseDto getMarketInfoByMemberIdx(Long memberIdx) {
        Market market = marketRepository.findByMemberIdx(memberIdx).orElseThrow(() -> new ErrorHandler(ErrorStatus.MARKET_NOT_FOUND));
        return MarketDtoConverter.convertToMarketInfoResponseDto(market);
    }
}
