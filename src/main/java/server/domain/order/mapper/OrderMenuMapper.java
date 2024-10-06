package server.domain.order.mapper;

import server.domain.order.domain.OrderMenu;

import java.util.List;
import java.util.Map;

public interface OrderMenuMapper {

    void save(OrderMenu orderMenu);

    List<OrderMenu> findAllByOrderIdx(Long orderIdx);

    OrderMenu findByOrderIdxAndMenuIdx(Map<String, Object> params);


}
