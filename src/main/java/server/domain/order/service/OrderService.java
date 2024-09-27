package server.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.market.domain.Market;
import server.domain.market.repository.MarketRepository;
import server.domain.member.repository.MemberRepository;
import server.domain.order.domain.Order;
import server.domain.order.dto.OrderDtoConverter;
import server.domain.order.dto.OrderRequestDto;
import server.domain.order.dto.OrderResponseDto;
import server.domain.order.repository.OrderRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;

    // 주문 생성하는 메서드
    // POST /api/order
    public OrderResponseDto.OrderTaskSuccessResponseDto createOrder(OrderRequestDto.CreateOrderRequestDto requestDto, String memberId) {
        Long memberIdx = getMemberIdx(memberId);
        if (!marketRepository.existsByMarketIdx(requestDto.getMarketIdx())) {
            throw new ErrorHandler(ErrorStatus.MARKET_NOT_FOUND);
        }
        int amount = requestDto.getAmount();
        // 90% 계산 (반올림 적용)
        int taxFreeAmount = Math.round(amount * 0.9f);

        // 10% 계산 (남은 부분)
        int vatAmount = amount - taxFreeAmount;

        Order order = Order.builder()
                .marketIdx(requestDto.getMarketIdx())
                .name(requestDto.getName())
                .amount(requestDto.getAmount())
                .memberCnt(requestDto.getMemberCnt())
                .taxFreeAmount(taxFreeAmount)
                .vatAmount(vatAmount)
                .orderMemberIdx(memberIdx)
                .createdAt(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        Order savedOrder = orderRepository.findByOrderIdx(order.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_SAVE_FAIL));

        return OrderResponseDto.OrderTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedOrder.getIdx())
                .build();
    }

    // 마켓에서 주문 리스트 조회하는 메서드
    // GET /api/order
    public OrderResponseDto.OrderListResponseDto getMarketOrderList(String memberId) {
        Long memberIdx = getMemberIdx(memberId);
        Market market = marketRepository.findByMemberIdx(memberIdx).orElseThrow(() -> new ErrorHandler(ErrorStatus.MARKET_NOT_FOUND));
        List<Order> orderList = orderRepository.findAllByMarketIdx(market.getIdx());
        List <OrderResponseDto.OrderInfoResponseDto> orderInfoResponseDtoList = orderList.stream()
                .map(OrderDtoConverter::convertToOrderInfoResponseDto)
                .toList();
        return OrderResponseDto.OrderListResponseDto.builder()
                .orderList(orderInfoResponseDtoList)
                .totalOrderCnt(orderList.size())
                .isSuccess(true)
                .build();
    }

    private Long getMemberIdx(String memberId) {
        return memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
