package server.domain.order.mapper;

import server.domain.order.domain.OrderMenu;

import java.util.List;

public interface OrderMenuMapper {

    void save(OrderMenu orderMenu);

    List<OrderMenu> findAllByOrderIdx(Long orderIdx);

    OrderMenu findByOrderIdxAndMenuIdx(Long orderIdx, Long menuIdx);


}
