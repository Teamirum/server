package server.domain.order.dto;

import server.domain.order.domain.Order;

public class OrderDtoConverter {

    public static OrderResponseDto.OrderInfoResponseDto convertToOrderInfoResponseDto(Order order) {
        return OrderResponseDto.OrderInfoResponseDto.builder()
                .idx(order.getIdx())
                .marketIdx(order.getMarketIdx())
                .name(order.getName())
                .amount(order.getAmount())
                .taxFreeAmount(order.getTaxFreeAmount())
                .vatAmount(order.getVatAmount())
                .orderMemberIdx(order.getOrderMemberIdx())
                .createdAt(order.getCreatedAt().toString())
                .build();
    }

}
