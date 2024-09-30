package server.domain.menu.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.market.domain.Market;
import server.domain.menu.domain.Menu;
import server.domain.menu.mapper.MenuMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final MenuMapper menuMapper;

    public Optional<Menu> findByMarketIdxAndName(Long marketIdx, String name) {
        return Optional.ofNullable(menuMapper.findByMarketIdxAndName(marketIdx, name));
    }

    public List<Menu> findByMarketIdx(Long marketIdx) {
        return menuMapper.findByMarketIdx(marketIdx);
    }

    public Optional<Menu> findByIdx(Long idx) {
        return Optional.ofNullable(menuMapper.findByIdx(idx));
    }

    public boolean existsByMarketIdxAndName(Long marketIdx, String name) {
        return menuMapper.findByMarketIdxAndName(marketIdx, name) != null;
    }

    public void save(Menu menu) {
        menuMapper.save(menu);
    }
}
