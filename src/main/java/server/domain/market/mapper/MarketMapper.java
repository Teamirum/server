package server.domain.market.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.market.domain.Market;

import java.util.List;

@Mapper
public interface MarketMapper {

    void save(Market market);

    Market findByMarketIdx(Long marketIdx);

    Market findByMemberIdx(Long memberIdx);

    Market findByMemberId(String memberId);

    List<Market> findAll();

}
