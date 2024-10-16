package server.domain.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.order.domain.Order;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    void save(Order order);

    Order findByOrderIdx(Long orderIdx);

    List<Order> findAllByMarketIdx(Long marketIdx);

    void updatePrice(Order order);

    Order findByNameAndMarketIdx(Map<String, Object> params);
}
