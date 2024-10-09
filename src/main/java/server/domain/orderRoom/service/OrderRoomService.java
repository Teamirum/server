package server.domain.orderRoom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.domain.menu.domain.Menu;
import server.domain.menu.repository.MenuRepository;
import server.domain.order.domain.Order;
import server.domain.order.domain.OrderMenu;
import server.domain.order.domain.TogetherOrder;
import server.domain.order.repository.OrderMenuRepository;
import server.domain.order.repository.OrderRepository;
import server.domain.order.repository.TogetherOrderRepository;
import server.domain.orderRoom.domain.OrderRoom;
import server.domain.orderRoom.dto.OrderRoomDataDto;
import server.domain.orderRoom.dto.OrderRoomResponseDto.*;
import server.domain.orderRoom.dto.OrderRoomRequestDto.*;
import server.domain.orderRoom.model.OrderRoomStatus;
import server.domain.orderRoom.model.OrderRoomType;
import server.domain.orderRoom.repository.OrderRoomRepository;
import server.domain.orderRoom.repository.RedisRepository;
import server.global.aop.annotation.RedisLock;
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
    private final TogetherOrderRepository togetherOrderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RedisPublisher redisPublisher;
    private final OrderRoomRepository orderRoomRepository;


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
                .readyCnt(0)
                .totalPrice(order.getTotalPrice())
                .currentPrice(0)
                .memberIdxList(new ArrayList<>())
                .type(OrderRoomType.fromName(requestDto.getType()))
                .status(OrderRoomStatus.ACTIVE)
                .createdAt(LocalDateTime.now().toString())
                .build();



        List<OrderMenu> orderMenuList = orderMenuRepository.findAllByOrderIdx(requestDto.getOrderIdx());

        HashMap<Long, Integer> menuAmount = new HashMap<>();
        HashMap<Long, List<Long>> currentMenuSelect = new HashMap<>();
        // 응답 dto 객체 생성
        List<OrderMenuResponseDto> orderMenuResponseDtoList = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenuList) {
            menuAmount.put(orderMenu.getMenuIdx(), orderMenu.getAmount());
            currentMenuSelect.put(orderMenu.getMenuIdx(), new ArrayList<>());
            OrderMenuResponseDto orderMenuResponseDto = OrderMenuResponseDto.builder()
                    .menuIdx(orderMenu.getMenuIdx())
                    .menuName(orderMenu.getMenuName())
                    .price(orderMenu.getPrice())
                    .amount(orderMenu.getAmount())
                    .build();
            orderMenuResponseDtoList.add(orderMenuResponseDto);
        }
        if (orderMenuList.isEmpty()) {
            throw new ErrorHandler(ErrorStatus.ORDER_MENU_NOT_FOUND);
        }
        orderRoom.setMenuAmount(menuAmount);
        orderRoom.setMenuSelect(currentMenuSelect);
        redisRepository.saveOrderRoom(orderRoom);

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

    public void enterOrderRoom(Long orderIdx, String memberId) {
        Member member = getMemberById(memberId);
        ChannelTopic channelTopic = redisRepository.enterOrderRoom(member.getIdx(), orderIdx);
        redisMessageListener.addMessageListener(redisSubscriber, channelTopic);

        if (!redisRepository.existByOrderRoomIdx(orderIdx)) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), orderIdx, ErrorStatus.ORDER_ROOM_NOT_FOUND));
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_NOT_FOUND);
        }

        OrderRoom orderRoom = redisRepository.getOrderRoom(orderIdx);
        boolean isFull = orderRoom.getMaxMemberCnt() == orderRoom.getMemberCnt();

        EnterOrderRoomResponseDto enterOrderRoomResponseDto = EnterOrderRoomResponseDto.builder()
                .orderIdx(orderIdx)
                .ownerMemberIdx(orderRoom.getOwnerMemberIdx())
                .memberIdx(member.getIdx())
                .maxMemberCnt(orderRoom.getMaxMemberCnt())
                .memberCnt(orderRoom.getMemberCnt())
                .isFull(isFull)
                .roomType(orderRoom.getType().name())
                .type("ENTER")
                .build();


        redisPublisher.publish(channelTopic, enterOrderRoomResponseDto);
        log.info("{} 님 주문방에 입장하였습니다. 주문방 ID : {}", memberId, orderIdx);

        if (isFull && orderRoom.getType().equals(OrderRoomType.BY_MENU)) {
            sendOrderRoomMenu(orderRoom, channelTopic);
        }

    }

    private void sendOrderRoomMenu(OrderRoom orderRoom, ChannelTopic channelTopic) {
        List<OrderMenu> orderMenuList = orderMenuRepository.findAllByOrderIdx(orderRoom.getOrderIdx());
        List<OrderRoomMenuInfoListDto.OrderMenuInfoDto> orderMenuInfoDtoList = new ArrayList<>();
        for (OrderMenu orderMenu : orderMenuList) {
            OrderRoomMenuInfoListDto.OrderMenuInfoDto orderMenuInfoDto = OrderRoomMenuInfoListDto.OrderMenuInfoDto.builder()
                    .menuIdx(orderMenu.getMenuIdx())
                    .menuName(orderMenu.getMenuName())
                    .price(orderMenu.getPrice())
                    .amount(orderMenu.getAmount())
                    .build();
            orderMenuInfoDtoList.add(orderMenuInfoDto);
        }
        OrderRoomMenuInfoListDto orderRoomMenuInfoListDto = OrderRoomMenuInfoListDto.builder()
                .orderIdx(orderRoom.getOrderIdx())
                .ownerMemberIdx(orderRoom.getOwnerMemberIdx())
                .maxMemberCnt(orderRoom.getMaxMemberCnt())
                .memberCnt(orderRoom.getMemberCnt())
                .totalPrice(orderRoom.getTotalPrice())
                .type("MENU_INFO")
                .menuInfoList(orderMenuInfoDtoList)
                .build();
        redisPublisher.publish(channelTopic, orderRoomMenuInfoListDto);

    }

    public void selectOrderMenu(SelectMenuRequestDto requestDto, String memberId) {
        Member member = getMemberById(memberId);
        ChannelTopic channelTopic = redisRepository.getTopic(requestDto.getOrderIdx().toString());
        if (channelTopic == null) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_CHANNEL_TOPIC_NOT_FOUND);
        }

        if (!redisRepository.existByOrderRoomIdx(requestDto.getOrderIdx())) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_NOT_FOUND));
            return;
        }
        OrderRoom orderRoom = redisRepository.selectMenu(requestDto.getOrderIdx(), requestDto.getMenuIdx(), member.getIdx(), requestDto.getMenuPrice() * requestDto.getAmount(), channelTopic);

        log.info("{} 님이 주문방에 메뉴를 선택하였습니다. 주문방 ID : {}, 메뉴 이름 : {}", memberId, requestDto.getOrderIdx(), requestDto.getMenuName());
        OrderRoomMenuSelectionResponseDto menuSelect = OrderRoomMenuSelectionResponseDto.builder()
                .orderIdx(requestDto.getOrderIdx())
                .memberIdx(member.getIdx())
                .menuIdx(requestDto.getMenuIdx())
                .menuName(requestDto.getMenuName())
                .currentPrice(orderRoom.getCurrentPrice())
                .amount(requestDto.getAmount())
                .type("MENU_SELECT")
                .build();
        redisPublisher.publish(channelTopic, menuSelect);
    }

    @Transactional
    public void selectPrice(SelectPriceRequestDto requestDto, String memberId) {
        Member member = getMemberById(memberId);
        ChannelTopic channelTopic = redisRepository.getTopic(requestDto.getOrderIdx().toString());
        if (channelTopic == null) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_CHANNEL_TOPIC_NOT_FOUND);
        }

        if (!redisRepository.existByOrderRoomIdx(requestDto.getOrderIdx())) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_NOT_FOUND));
            return;
        }
        if (!redisRepository.existByOrderRoomIdx(requestDto.getOrderIdx())) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_NOT_FOUND));
            return;
        }
        Order order = orderRepository.findByOrderIdx(requestDto.getOrderIdx()).orElse(null);
        if (order == null) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(null, requestDto.getOrderIdx(), ErrorStatus.ORDER_NOT_FOUND));
            return;
        }
        if (order.getTotalPrice() != requestDto.getTotalPrice()) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_PRICE_NOT_MATCH));
            return;
        }

        OrderRoom orderRoom = redisRepository.getOrderRoom(requestDto.getOrderIdx());
        if (requestDto.getMemberCnt() != orderRoom.getMaxMemberCnt()) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_MEMBER_CNT_NOT_MATCH));
            return;
        }
        List<MemberPriceInfoDto> memberPriceInfoList = requestDto.getMemberPriceInfoList();
        // 요청온 멤버와 참여중인 멤버 정보 확인
        for (MemberPriceInfoDto memberPriceInfo : memberPriceInfoList) {
            if (!orderRoom.getMemberIdxList().contains(memberPriceInfo.getMemberIdx())) {
                redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_MEMBER_NOT_FOUND));
                return;
            }
        }
        // 응답 Dto
        List<OrderRoomMemberPriceSelectionInfoDto> orderRoomMemberPriceSelectionInfoDtos = new ArrayList<>();
        for (MemberPriceInfoDto memberPriceInfo : memberPriceInfoList) {
            if (memberPriceInfo.getPrice() < 0) {
                redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_PRICE_NOT_VALID));
                return;
            }
            log.info("{} 님. 함께 결제 주문정보 저장 주문방 ID : {}, 금액 : {}", memberId, requestDto.getOrderIdx(), memberPriceInfo.getPrice());
            TogetherOrder togetherOrder = TogetherOrder.builder()
                    .orderIdx(order.getIdx())
                    .memberIdx(memberPriceInfo.getMemberIdx())
                    .price(memberPriceInfo.getPrice())
                    .createdAt(LocalDateTime.now())
                    .build();

            togetherOrderRepository.save(togetherOrder);

            orderRoom.updateCurrentPrice(memberPriceInfo.getPrice());
            orderRoomMemberPriceSelectionInfoDtos.add(OrderRoomMemberPriceSelectionInfoDto.builder()
                    .memberIdx(memberPriceInfo.getMemberIdx())
                    .price(memberPriceInfo.getPrice())
                    .build());
        }
        redisRepository.saveOrderRoom(orderRoom);
        orderRoomRepository.save(orderRoom);

        OrderRoomPriceSelectionResponseDto priceSelect = OrderRoomPriceSelectionResponseDto.builder()
                .orderIdx(requestDto.getOrderIdx())
                .memberPriceInfoList(orderRoomMemberPriceSelectionInfoDtos)
                .type("PRICE_SELECT")
                .build();
        redisPublisher.publish(channelTopic, priceSelect);
    }

    public void cancelOrderMenu(SelectMenuRequestDto requestDto, String memberId) {
        Member member = getMemberById(memberId);
        ChannelTopic channelTopic = redisRepository.getTopic(requestDto.getOrderIdx().toString());
        if (channelTopic == null) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_CHANNEL_TOPIC_NOT_FOUND);
        }

        if (!redisRepository.existByOrderRoomIdx(requestDto.getOrderIdx())) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), requestDto.getOrderIdx(), ErrorStatus.ORDER_ROOM_NOT_FOUND));
            return;
        }
        OrderRoom orderRoom = redisRepository.cancelMenu(requestDto.getOrderIdx(), requestDto.getMenuIdx(), member.getIdx(), requestDto.getMenuPrice() * -1);

        log.info("{} 님이 주문방에 메뉴를 취소하였습니다. 주문방 ID : {}, 메뉴 이름 : {}", memberId, requestDto.getOrderIdx(), requestDto.getMenuName());
        OrderRoomMenuSelectionResponseDto menuCancel = OrderRoomMenuSelectionResponseDto.builder()
                .orderIdx(requestDto.getOrderIdx())
                .memberIdx(member.getIdx())
                .menuIdx(requestDto.getMenuIdx())
                .menuName(requestDto.getMenuName())
                .currentPrice(orderRoom.getCurrentPrice())
                .amount(requestDto.getAmount())
                .type("MENU_CANCEL")
                .build();

        redisPublisher.publish(channelTopic, menuCancel);
    }

    @Transactional
    public void readyToPay(Long orderIdx, String memberId) {
        Member member = getMemberById(memberId);
        ChannelTopic channelTopic = redisRepository.getTopic(orderIdx + "");
        if (channelTopic == null) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_CHANNEL_TOPIC_NOT_FOUND);
        }
        if (!redisRepository.existByOrderRoomIdx(orderIdx)) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), orderIdx, ErrorStatus.ORDER_ROOM_NOT_FOUND));
            return;
        }
        Order order = orderRepository.findByOrderIdx(orderIdx).orElse(null);
        if (order == null) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(null, orderIdx, ErrorStatus.ORDER_NOT_FOUND));
            return;
        }
        OrderRoom orderRoom = redisRepository.readyToPay(orderIdx, member.getIdx(), channelTopic);
        log.info("{} 님이 주문방에 결제 준비를 하였습니다. 주문방 ID : {}", memberId, orderIdx);
        OrderRoomReadyToPayResponseDto readyToPay = OrderRoomReadyToPayResponseDto.builder()
                .orderIdx(orderIdx)
                .memberIdx(member.getIdx())
                .totalPrice(orderRoom.getTotalPrice())
                .currentPrice(orderRoom.getCurrentPrice())
                .readyMemberCnt(orderRoom.getReadyCnt())
                .maxMemberCnt(orderRoom.getMaxMemberCnt())
                .isReadyToPay(orderRoom.getReadyCnt() == orderRoom.getMaxMemberCnt())
                .type("READY_TO_PAY")
                .build();
        redisPublisher.publish(channelTopic, readyToPay);

        if (orderRoom.getReadyCnt() == orderRoom.getMaxMemberCnt() && orderRoom.getType().equals(OrderRoomType.BY_MENU)) {
            if (orderRoom.getTotalPrice() == orderRoom.getCurrentPrice()) {
                saveOrderRoomAndTogetherOrder(orderIdx);
            } else {
                redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), orderIdx, ErrorStatus.ORDER_ROOM_PRICE_NOT_MATCH));
            }
        }

    }

    @Transactional
    public void cancelReadyToPay(Long orderIdx, String memberId) {
        Member member = getMemberById(memberId);
        ChannelTopic channelTopic = redisRepository.getTopic(orderIdx.toString());
        if (channelTopic == null) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_CHANNEL_TOPIC_NOT_FOUND);
        }
        if (!redisRepository.existByOrderRoomIdx(orderIdx)) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(member.getIdx(), orderIdx, ErrorStatus.ORDER_ROOM_NOT_FOUND));
            return;
        }
        Order order = orderRepository.findByOrderIdx(orderIdx).orElse(null);
        if (order == null) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(null, orderIdx, ErrorStatus.ORDER_NOT_FOUND));
            return;
        }
        OrderRoom orderRoom = redisRepository.cancelReadyToPay(orderIdx, member.getIdx(), channelTopic);
        log.info("{} 님이 주문방에 결제 준비를 취소하였습니다. 주문방 ID : {}", memberId, orderIdx);
        OrderRoomReadyToPayResponseDto cancelReadyToPay = OrderRoomReadyToPayResponseDto.builder()
                .orderIdx(orderIdx)
                .memberIdx(member.getIdx())
                .totalPrice(orderRoom.getTotalPrice())
                .currentPrice(orderRoom.getCurrentPrice())
                .readyMemberCnt(orderRoom.getReadyCnt())
                .maxMemberCnt(orderRoom.getMaxMemberCnt())
                .isReadyToPay(orderRoom.getReadyCnt() == orderRoom.getMaxMemberCnt())
                .type("CANCEL_READY_TO_PAY")
                .build();
        redisPublisher.publish(channelTopic, cancelReadyToPay);
    }

    public void saveOrderRoomAndTogetherOrder(Long orderIdx) {
        ChannelTopic channelTopic = redisRepository.getTopic(orderIdx.toString());
        if (channelTopic == null) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_CHANNEL_TOPIC_NOT_FOUND);
        }
        Order order = orderRepository.findByOrderIdx(orderIdx).orElse(null);
        if (order == null) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(null, orderIdx, ErrorStatus.ORDER_NOT_FOUND));
            return;
        }

        OrderRoom orderRoom = redisRepository.getOrderRoom(order.getIdx());
        List<OrderMenu> orderMenuList = orderMenuRepository.findAllByOrderIdx(orderRoom.getOrderIdx());
        if (orderMenuList.isEmpty()) {
            redisPublisher.publish(channelTopic, new ErrorResponseDto(null, orderIdx, ErrorStatus.ORDER_MENU_NOT_FOUND));
            return;
        }
        HashMap<Long, List<Long>> menuSelect = orderRoom.getMenuSelect();
        for (Long memberIdx : orderRoom.getMemberIdxList()) {
            Member member = memberRepository.findByIdx(memberIdx).orElse(null);
            if (member == null) {
                redisPublisher.publish(channelTopic, new ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.MEMBER_NOT_FOUND));
                return;
            }
            int totalPrice = 0;
            for (Long menuIdx : menuSelect.keySet()) {
                List<Long> memberMenuSelect = menuSelect.get(menuIdx);
                if (memberMenuSelect.contains(memberIdx)) {
                    Menu menu = menuRepository.findByIdx(menuIdx).orElse(null);
                    if (menu == null) {
                        redisPublisher.publish(channelTopic, new ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.MENU_NOT_FOUND));
                        return;
                    }
                    totalPrice += menu.getPrice();
                }
            }
            TogetherOrder togetherOrder = TogetherOrder.builder()
                    .orderIdx(order.getIdx())
                    .memberIdx(member.getIdx())
                    .price(totalPrice)
                    .createdAt(LocalDateTime.now())
                    .build();
            togetherOrderRepository.save(togetherOrder);
        }
        orderRoomRepository.save(orderRoom);

        StartPayResponseDto startPayResponseDto = StartPayResponseDto.builder()
                .orderIdx(orderIdx)
                .totalPrice(orderRoom.getTotalPrice())
                .currentPrice(orderRoom.getCurrentPrice())
                .maxMemberCnt(orderRoom.getMaxMemberCnt())
                .canStartToPay(orderRoom.getTotalPrice() == orderRoom.getCurrentPrice())
                .type("START_PAY")
                .build();

        redisPublisher.publish(channelTopic, startPayResponseDto);


    }


    public Member getMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
