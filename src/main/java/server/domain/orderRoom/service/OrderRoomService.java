package server.domain.orderRoom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import server.domain.member.repository.MemberRepository;
import server.domain.order.domain.Order;
import server.domain.order.domain.OrderMenu;
import server.domain.order.repository.OrderMenuRepository;
import server.domain.order.repository.OrderRepository;
import server.domain.orderRoom.domain.OrderRoom;
import server.domain.orderRoom.dto.OrderRoomResponseDto.*;
import server.domain.orderRoom.dto.OrderRoomRequestDto.*;
import server.domain.orderRoom.model.OrderRoomStatus;
import server.domain.orderRoom.repository.RedisRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderRoomService {

    private final RedisRepository redisRepository;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final MemberRepository memberRepository;
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RedisPublisher redisPublisher;


    public CreateOrderRoomResponseDto createOrderRoom(CreateOrderRoomRequestDto requestDto, String memberId) {
        Order order = orderRepository.findByOrderIdx(requestDto.getOrderIdx())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.ORDER_NOT_FOUND));
        if (redisRepository.existByOrderRoomIdx(requestDto.getOrderIdx())) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_ALREADY_EXIST);
        }

        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        OrderRoom orderRoom = OrderRoom.builder()
                .orderIdx(requestDto.getOrderIdx())
                .ownerMemberIdx(memberIdx)
                .maxMemberCnt(requestDto.getMaxMemberCnt())
                .memberCnt(0)
                .status(OrderRoomStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderMenu> orderMenuList = orderMenuRepository.findAllByOrderIdx(requestDto.getOrderIdx());

        HashMap<Long, Integer> menuAmount = new HashMap<>();
        HashMap<Long, Integer> currentMenuAmount = new HashMap<>();
        // 응답 dto 객체 생성
        List<OrderMenuResponseDto> orderMenuResponseDtoList = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenuList) {
            menuAmount.put(orderMenu.getMenuIdx(), orderMenu.getAmount());
            currentMenuAmount.put(orderMenu.getMenuIdx(), 0);
            OrderMenuResponseDto orderMenuResponseDto = OrderMenuResponseDto.builder()
                    .menuIdx(orderMenu.getMenuIdx())
                    .menuName(orderMenu.getMenuName())
                    .amount(orderMenu.getAmount())
                    .build();
            orderMenuResponseDtoList.add(orderMenuResponseDto);
        }
        if (orderMenuList.isEmpty()) {
            throw new ErrorHandler(ErrorStatus.ORDER_MENU_NOT_FOUND);
        }
        orderRoom.setMenuAmount(menuAmount);
        orderRoom.setCurrentMenuAmount(currentMenuAmount);

        ChannelTopic channelTopic = redisRepository.saveOrderRoom(orderRoom);
        redisMessageListener.addMessageListener(redisSubscriber, channelTopic);
        log.info("주문방 생성 : {} 번 방", requestDto.getOrderIdx());

        return CreateOrderRoomResponseDto.builder()
                .orderIdx(requestDto.getOrderIdx())
                .maxMemberCnt(requestDto.getMaxMemberCnt())
                .orderMenuList(orderMenuResponseDtoList)
                .totalPrice(order.getTotalPrice())
                .menuCnt(orderMenuList.size())
                .isSuccess(true)
                .build();

    }

    public void enterOrderRoom(EnterOrderRoomRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (!redisRepository.existByOrderRoomIdx(requestDto.getOrderIdx())) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_NOT_FOUND);
        }
        ChannelTopic channelTopic = redisRepository.enterOrderRoom(memberIdx, requestDto.getOrderIdx());
        redisMessageListener.addMessageListener(redisSubscriber, channelTopic);

        OrderRoom orderRoom = redisRepository.getOrderRoom(requestDto.getOrderIdx());
        boolean isFull = orderRoom.getMaxMemberCnt() == orderRoom.getMemberCnt();

        EnterOrderRoomResponseDto enterOrderRoomResponseDto = EnterOrderRoomResponseDto.builder()
                .orderIdx(requestDto.getOrderIdx())
                .ownerMemberIdx(orderRoom.getOwnerMemberIdx())
                .memberIdx(memberIdx)
                .maxMemberCnt(orderRoom.getMaxMemberCnt())
                .memberCnt(orderRoom.getMemberCnt())
                .isFull(isFull)
                .type("ENTER")
                .build();


        redisPublisher.publish(channelTopic, enterOrderRoomResponseDto);
        log.info("{} 님 주문방에 입장하였습니다. 주문방 ID : {}", memberId, requestDto.getOrderIdx());


    }

    public void sendOrderRoomMenu(OrderRoom orderRoom, ChannelTopic channelTopic) {
        OrderRoomMenuResponseDto orderRoomMenuResponseDto = OrderRoomMenuResponseDto.builder()
                .orderIdx(orderRoom.getOrderIdx())
                .menuAmount(orderRoom.getMenuAmount())
                .currentMenuAmount(orderRoom.getCurrentMenuAmount())
                .type("MENU")
                .build();
        redisPublisher.publish(channelTopic, orderRoomMenuResponseDto);
    }

}
