package server.domain.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.order.domain.Order;
import server.domain.order.mapper.OrderMapper;

import java.util.List;
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

    List<Order> findAllByMarketIdx(Long marketIdx) {
        return orderMapper.findAllByMarketIdx(marketIdx);
    }

    List<Order> findAllByOrderMemberIdx(Long orderMemberIdx) {
        return orderMapper.findAllByOrderMemberIdx(orderMemberIdx);
    }
}
