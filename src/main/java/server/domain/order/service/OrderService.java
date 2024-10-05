package server.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.market.domain.Market;
import server.domain.market.repository.MarketRepository;
import server.domain.member.repository.MemberRepository;
import server.domain.menu.domain.Menu;
import server.domain.menu.repository.MenuRepository;
import server.domain.order.domain.Order;
import server.domain.order.domain.OrderMenu;
import server.domain.order.dto.OrderDtoConverter;
import server.domain.order.dto.OrderRequestDto;
import server.domain.order.dto.OrderResponseDto;
import server.domain.order.repository.OrderMenuRepository;
import server.domain.order.repository.OrderRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;

    // 주문 생성하는 메서드
    // POST /api/order
    public OrderResponseDto.OrderTaskSuccessResponseDto createOrder(OrderRequestDto.CreateOrderRequestDto requestDto, String memberId) {
        if (!marketRepository.existsByMarketIdx(requestDto.getMarketIdx())) {
            throw new ErrorHandler(ErrorStatus.MARKET_NOT_FOUND);
        }
        List<OrderRequestDto.OrderMenuDto> menuList = requestDto.getMenuList();
        int totalPrice = 0;

        LocalDateTime now = LocalDateTime.now();
        // 주문 이름 = 시각+테이블번호
        Order order = Order.builder()
                .marketIdx(requestDto.getMarketIdx())
                .name(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + requestDto.getTableNumber())
                .totalPrice(totalPrice)
                .taxFreePrice(totalPrice)
                .vatPrice(totalPrice)
                .tableNumber(requestDto.getTableNumber())
                .createdAt(now)
                .build();
        orderRepository.save(order);

        Order savedOrder = orderRepository.findByOrderIdx(order.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_SAVE_FAIL));

        for (OrderRequestDto.OrderMenuDto menuDto : menuList) {
            if (menuDto.getAmount() <= 0) {
                throw new ErrorHandler(ErrorStatus.ORDER_MENU_CNT_ERROR);
            }
            Menu menu = menuRepository.findByIdx(requestDto.getMarketIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.MENU_NOT_FOUND));
            totalPrice += menu.getPrice() * menuDto.getAmount();
            OrderMenu orderMenu = OrderMenu.builder()
                    .orderIdx(savedOrder.getIdx())
                    .menuIdx(menu.getIdx())
                    .price(menu.getPrice())
                    .amount(menuDto.getAmount())
                    .build();
            orderMenuRepository.save(orderMenu);

            OrderMenu savedOrderMenu = orderMenuRepository.findByOrderIdxAndMenuIdx(orderMenu.getIdx(), orderMenu.getMenuIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_MENU_SAVE_FAIL));

        }
        savedOrder.updatePrice(totalPrice);
        orderRepository.updatePrice(savedOrder);
        return OrderResponseDto.OrderTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedOrder.getIdx())
                .build();
    }

    public OrderResponseDto.OrderTaskSuccessResponseDto updateOrderMenu(OrderRequestDto.UpdateOrderRequestDto requestDto) {
        Order order = orderRepository.findByOrderIdx(requestDto.getOrderIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_NOT_FOUND));
        List<OrderRequestDto.OrderMenuDto> menuList = requestDto.getMenuList();
        int totalPrice = order.getTotalPrice();

        for (OrderRequestDto.OrderMenuDto menuDto : menuList) {
            if (menuDto.getAmount() <= 0) {
                throw new ErrorHandler(ErrorStatus.ORDER_MENU_CNT_ERROR);
            }
            Menu menu = menuRepository.findByIdx(menuDto.getMenuIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.MENU_NOT_FOUND));
            totalPrice += menu.getPrice() * menuDto.getAmount();
            OrderMenu orderMenu = OrderMenu.builder()
                    .orderIdx(order.getIdx())
                    .menuIdx(menu.getIdx())
                    .menuName(menu.getName())
                    .amount(menuDto.getAmount())
                    .build();
            orderMenuRepository.save(orderMenu);
        }
        order.updatePrice(totalPrice);
        orderRepository.updatePrice(order);

        return OrderResponseDto.OrderTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(order.getIdx())
                .build();
    }

    public OrderResponseDto.OrderInfoResponseDto getOrderDetail(Long orderIdx, Long marketIdx) {
        Order order = orderRepository.findByOrderIdx(orderIdx).orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_NOT_FOUND));
        // 주문이 해당 마켓에 속해있지 않을 경우
        // 보안적인 측면 추가로 고려
        if (!order.getMarketIdx().equals(marketIdx)) {
            throw new ErrorHandler(ErrorStatus.ORDER_NOT_FOUND);
        }
        List<OrderMenu> orderMenuList = orderMenuRepository.findAllByOrderIdx(order.getIdx());
        return OrderDtoConverter.convertToOrderInfoResponseDto(order, orderMenuList.stream()
                .map(OrderDtoConverter::convertToOrderMenuResponseDto)
                .toList());
    }



    // 마켓에서 주문 리스트 조회하는 메서드
    // GET /api/order
    public OrderResponseDto.OrderListResponseDto getMarketOrderList(String memberId) {
        Long memberIdx = getMemberIdx(memberId);
        Market market = marketRepository.findByMemberIdx(memberIdx).orElseThrow(() -> new ErrorHandler(ErrorStatus.MARKET_NOT_FOUND));
        List<Order> orderList = orderRepository.findAllByMarketIdx(market.getIdx());
        List <OrderResponseDto.OrderInfoResponseDto> orderInfoResponseDtoList = orderList.stream()
                .map(order -> {
                    List<OrderMenu> orderMenuList = orderMenuRepository.findAllByOrderIdx(order.getIdx());
                    return OrderDtoConverter.convertToOrderInfoResponseDto(order, orderMenuList.stream()
                            .map(OrderDtoConverter::convertToOrderMenuResponseDto)
                            .toList());
                })
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
