package server.domain.order.dto;

import server.domain.order.domain.Order;
import server.domain.order.domain.OrderMenu;

import java.util.List;

public class OrderDtoConverter {

    public static OrderResponseDto.OrderInfoResponseDto convertToOrderInfoResponseDto(Order order, List<OrderResponseDto.OrderMenuResponseDto> menuResponseDtoList) {
        return OrderResponseDto.OrderInfoResponseDto.builder()
                .idx(order.getIdx())
                .marketIdx(order.getMarketIdx())
                .name(order.getName())
                .totalPrice(order.getTotalPrice())
                .taxFreePrice(order.getTaxFreePrice())
                .vatPrice(order.getVatPrice())
                .tableNumber(order.getTableNumber())
                .menuCnt(menuResponseDtoList.size())
                .orderMenuList(menuResponseDtoList)
                .createdAt(order.getCreatedAt().toString())
                .build();
    }

    public static OrderResponseDto.OrderMenuResponseDto convertToOrderMenuResponseDto(OrderMenu orderMenu) {
        return OrderResponseDto.OrderMenuResponseDto.builder()
                .idx(orderMenu.getIdx())
                .menuIdx(orderMenu.getMenuIdx())
                .menuName(orderMenu.getMenuName())
                .price(orderMenu.getPrice())
                .amount(orderMenu.getAmount())
                .build();
    }



}
