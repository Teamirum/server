package server.domain.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.order.domain.OrderMenu;
import server.domain.order.mapper.OrderMenuMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderMenuRepository {

    private final OrderMenuMapper orderMenuMapper;

    public void save(OrderMenu orderMenu) {
        orderMenuMapper.save(orderMenu);
    }

    public Optional<OrderMenu> findByOrderIdxAndMenuIdx(Long orderIdx, Long menuIdx) {
        Map<String, Object> params = Map.of("orderIdx", orderIdx, "menuIdx", menuIdx);
        OrderMenu orderMenu = orderMenuMapper.findByOrderIdxAndMenuIdx(params);
        if (orderMenu != null) {
            return Optional.of(orderMenu);
        }
        return Optional.empty();
    }

    public List<OrderMenu> findAllByOrderIdx(Long orderIdx) {
        return orderMenuMapper.findAllByOrderIdx(orderIdx);
    }
}
