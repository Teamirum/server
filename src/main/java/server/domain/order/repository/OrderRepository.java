package server.domain.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.order.domain.Order;
import server.domain.order.mapper.OrderMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final OrderMapper orderMapper;

    public void save(Order order) {
        orderMapper.save(order);
    }

    public Optional<Order> findByOrderIdx(Long orderIdx) {
        Order order = orderMapper.findByOrderIdx(orderIdx);
        if (order != null) {
            return Optional.of(order);
        }
        return Optional.empty();
    }

    public Optional<Order> findByNameAndMarketIdx(String name, Long marketIdx) {
        Map<String, Object> params = Map.of("name", name, "marketIdx", marketIdx);
        Order order = orderMapper.findByNameAndMarketIdx(params);
        if (order != null) {
            return Optional.of(order);
        }
        return Optional.empty();
    }

    public List<Order> findAllByMarketIdx(Long marketIdx) {
        return orderMapper.findAllByMarketIdx(marketIdx);
    }

    public void updatePrice(Order order) {
        orderMapper.updatePrice(order);
    }
}
