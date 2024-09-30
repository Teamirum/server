package server.domain.menu.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.market.domain.Market;
import server.domain.menu.domain.Menu;

import java.util.List;

@Mapper
public interface MenuMapper {

    Menu findByMarketIdxAndName(Long marketIdx, String name);

    List<Menu> findAllByMarketIdx(Long marketIdx);

    Menu findByIdx(Long idx);

    void save(Menu menu);

    void deleteByIdx(Long idx);

}
