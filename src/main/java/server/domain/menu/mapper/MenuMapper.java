package server.domain.menu.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.market.domain.Market;
import server.domain.menu.domain.Menu;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuMapper {

    Menu findByMarketIdxAndName(Map<String, Object> map);

    List<Menu> findAllByMarketIdx(Long marketIdx);

    Menu findByIdx(Long idx);

    void save(Menu menu);

    void deleteByIdx(Long idx);

}
