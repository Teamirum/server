package server.domain.market.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.market.domain.Market;
import server.domain.market.mapper.MarketMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MarketRepository {

    private final MarketMapper marketMapper;

    public void save(Market market) {
        marketMapper.save(market);
    }

    public List<Market> findAll() {
        return marketMapper.findAll();
    }

    public Optional<Market> findByMarketIdx(Long marketIdx) {
        Market market = marketMapper.findByMarketIdx(marketIdx);
        if (market != null) {
            return Optional.of(market);
        }
        return Optional.empty();
    }

    public Optional<Market> findByMemberIdx(Long memberIdx) {
        Market market = marketMapper.findByMemberIdx(memberIdx);
        if (market != null) {
            return Optional.of(market);
        }
        return Optional.empty();
    }

    public boolean existsByMemberIdx(Long memberIdx) {
        return marketMapper.findByMemberIdx(memberIdx) != null;
    }

}
