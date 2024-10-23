package server.domain.orderRoom.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import server.domain.orderRoom.domain.OrderRoom;
import server.domain.orderRoom.dto.OrderRoomResponseDto;
import server.domain.orderRoom.service.RedisPublisher;
import server.global.aop.annotation.RedisLock;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisRepository {

    private final RedisPublisher redisPublisher;

    // Redis
    private static final String ORDER_ROOMS = "ORDER_ROOM";
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
    public static final String MEMBER_INFO = "MEMBER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, OrderRoom> opsHashOrderRoom;
    // 채팅방 입장 정보 <ENTER_INFO, memberIdx, orderIdx>
    private HashOperations<String, String, Long> opsHashEnterInfo;
    // 채팅방 세션 멤버 정보 <MEMBER_INFO, SessionId, memberId>
    private HashOperations<String, String, String> opsHashMemberSessionInfo;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic 정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashOrderRoom = redisTemplate.opsForHash();
        opsHashEnterInfo = redisTemplate.opsForHash();
        opsHashMemberSessionInfo = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    /**
     * 주문 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    @RedisLock(lockName = "lock:orderRoom:#{#orderIdx}")
    public ChannelTopic enterOrderRoom(Long memberIdx, Long orderIdx) {
        OrderRoom orderRoom = getOrderRoom(orderIdx);
        String orderIdxStr = orderRoom.getOrderIdx() + "";
        ChannelTopic topic = getTopic(orderIdxStr);
        if (topic == null) {
            log.info("기존에 등록된 topic이 없습니다. 새로운 topic 생성 : orderIdx = {}", orderIdxStr);
            topic = new ChannelTopic(orderIdxStr);
        }
        log.info("topic 생성 : orderIdx = {}, topic = {}", orderIdxStr, topic);

        topics.put(orderIdxStr, topic);
//        if (existMyInfo(memberIdx)) {
//            throw new ErrorHandler(ErrorStatus.ORDER_MEMBER_ALREADY_IN_OTHER_ROOM);
//        }
        if (getMemberEnteredOrderRoomIdx(memberIdx) != null) {
            redisPublisher.publish(topics.get(orderIdx + ""), new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_MEMBER_ALREADY_IN_ROOM));
            throw new RuntimeException(ErrorStatus.ORDER_MEMBER_ALREADY_IN_ROOM.getMessage());
        }
        if (orderRoom.getMemberIdxList().contains(memberIdx)) {
            redisPublisher.publish(topics.get(orderIdx + ""), new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_MEMBER_ALREADY_IN_ROOM));
            throw new RuntimeException(ErrorStatus.ORDER_MEMBER_ALREADY_IN_ROOM.getMessage());
        }

        if (!orderRoom.enterMember(memberIdx)) {
            redisPublisher.publish(topic, new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_ROOM_MEMBER_CNT_CANNOT_EXCEED));
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_MEMBER_CNT_CANNOT_EXCEED);
        }
        opsHashOrderRoom.put(ORDER_ROOMS, orderIdxStr, orderRoom);
        opsHashEnterInfo.put(ENTER_INFO, memberIdx + "", orderIdx);
        return topic;
    }

    public ChannelTopic saveOrderRoom(OrderRoom orderRoom) {
        String orderIdxStr = orderRoom.getOrderIdx() + "";
        if (topics.containsKey(orderIdxStr)) {
            ChannelTopic topic = new ChannelTopic(orderIdxStr);
            topics.put(orderIdxStr, topic);
        }
        opsHashOrderRoom.put(ORDER_ROOMS, orderIdxStr, orderRoom);
        return topics.get(orderIdxStr);
    }



    public OrderRoom getOrderRoom(Long orderIdx) {
        if (!existByOrderRoomIdx(orderIdx)) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_NOT_FOUND);
        }
        return opsHashOrderRoom.get(ORDER_ROOMS, orderIdx + "");
    }

    @RedisLock(lockName = "lock:orderRoom:#{#orderIdx}")
    public OrderRoom readyToPay(Long orderIdx, Long memberIdx, ChannelTopic topic) {
        OrderRoom orderRoom = getOrderRoom(orderIdx);
        String orderIdxStr = orderRoom.getOrderIdx() + "";
        if (!orderRoom.readyToPay()) {
            redisPublisher.publish(topic, new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_ROOM_MEMBER_CNT_NOT_MATCH));
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_MEMBER_CNT_NOT_MATCH);
        }
        opsHashOrderRoom.put(ORDER_ROOMS, orderIdxStr, orderRoom);
        return orderRoom;
    }

    @RedisLock(lockName = "lock:orderRoom:#{#orderIdx}")
    public OrderRoom cancelReadyToPay(Long orderIdx, Long memberIdx, ChannelTopic topic) {
        OrderRoom orderRoom = getOrderRoom(orderIdx);
        if (orderRoom.getReadyCnt() == orderRoom.getMaxMemberCnt()) {
            redisPublisher.publish(topic, new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_ROOM_PAY_ALREADY_STARTED));
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_PAY_ALREADY_STARTED);
        }
        if (!orderRoom.cancelReadyToPay()) {
            redisPublisher.publish(topic, new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_ROOM_MEMBER_CNT_NOT_MATCH));
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_MEMBER_CNT_NOT_MATCH);
        }
        opsHashOrderRoom.put(ORDER_ROOMS, orderIdx + "", orderRoom);
        return orderRoom;
    }

    public boolean existByOrderRoomIdx(Long orderIdx) {
        return opsHashOrderRoom.hasKey(ORDER_ROOMS, orderIdx + "");
    }

    public ChannelTopic getTopic(String orderIdx) {
        log.info("topic 을 불러옵니다 : orderIdx = {}, topic = {}", orderIdx, topics.get(orderIdx));
        return topics.get(orderIdx);
    }

    @RedisLock(lockName = "lock:orderRoom:#{#orderIdx}")
    public OrderRoom selectMenu(Long orderIdx, Long menuIdx, Long memberIdx, int price, ChannelTopic topic) {
        OrderRoom orderRoom = getOrderRoom(orderIdx);
        HashMap<Long, List<Long>> menuSelect = orderRoom.getMenuSelect();
        HashMap<Long, Integer> menuAmount = orderRoom.getMenuAmount();
        List<OrderRoomResponseDto.SelectedMenuInfoDto> selectedMenuInfoList = new ArrayList<>();
        for (Long idx : orderRoom.getMenuSelect().keySet()) {
            List<Long> memberIdxList = orderRoom.getMenuSelect().get(idx);
            OrderRoomResponseDto.SelectedMenuInfoDto selectedMenuInfo = OrderRoomResponseDto.SelectedMenuInfoDto.builder()
                    .menuIdx(idx)
                    .currentAmount(memberIdxList.size())
                    .memberIdxList(memberIdxList)
                    .build();
            selectedMenuInfoList.add(selectedMenuInfo);
        }
        if (!menuSelect.containsKey(menuIdx)) {
            redisPublisher.publish(topic, new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_MENU_NOT_FOUND, selectedMenuInfoList));
            throw new ErrorHandler(ErrorStatus.ORDER_MENU_NOT_FOUND);
        }
        if (menuSelect.get(menuIdx).size() >= menuAmount.get(menuIdx)) {
            redisPublisher.publish(topic, new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_MENU_MEMBER_CNT_ERROR, selectedMenuInfoList));
            throw new ErrorHandler(ErrorStatus.ORDER_MENU_MEMBER_CNT_ERROR);
        }
        menuSelect.get(menuIdx).add(memberIdx);
        orderRoom.updateCurrentPrice(price);

        opsHashOrderRoom.put(ORDER_ROOMS, orderIdx + "", orderRoom);
        return orderRoom;
    }

    @RedisLock(lockName = "lock:orderRoom:#{#orderIdx}")
    public OrderRoom cancelMenu(Long orderIdx, Long menuIdx, Long memberIdx, int price) {
        OrderRoom orderRoom = getOrderRoom(orderIdx);
        HashMap<Long, List<Long>> menuSelect = orderRoom.getMenuSelect();
        List<OrderRoomResponseDto.SelectedMenuInfoDto> selectedMenuInfoList = new ArrayList<>();
        for (Long idx : orderRoom.getMenuSelect().keySet()) {
            List<Long> memberIdxList = orderRoom.getMenuSelect().get(idx);
            OrderRoomResponseDto.SelectedMenuInfoDto selectedMenuInfo = OrderRoomResponseDto.SelectedMenuInfoDto.builder()
                    .menuIdx(idx)
                    .currentAmount(memberIdxList.size())
                    .memberIdxList(memberIdxList)
                    .build();
            selectedMenuInfoList.add(selectedMenuInfo);
        }
        if (!menuSelect.containsKey(menuIdx)) {
            redisPublisher.publish(topics.get(orderIdx + ""), new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_MENU_NOT_FOUND, selectedMenuInfoList));
            throw new ErrorHandler(ErrorStatus.ORDER_MENU_NOT_FOUND);
        }
        if (menuSelect.get(menuIdx).size() <= 0) {
            redisPublisher.publish(topics.get(orderIdx + ""), new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_MENU_MEMBER_CNT_ERROR, selectedMenuInfoList));
            throw new ErrorHandler(ErrorStatus.ORDER_MENU_MEMBER_CNT_ERROR);
        }
        if (!menuSelect.get(menuIdx).contains(memberIdx)) {
            redisPublisher.publish(topics.get(orderIdx + ""), new OrderRoomResponseDto.ErrorResponseDto(memberIdx, orderIdx, ErrorStatus.ORDER_NOT_ORDERED_MENU_CANNOT_CANCELED, selectedMenuInfoList));
            throw new ErrorHandler(ErrorStatus.ORDER_NOT_ORDERED_MENU_CANNOT_CANCELED);
        }
        menuSelect.get(menuIdx).remove(memberIdx);
        orderRoom.updateCurrentPrice(price);

        opsHashOrderRoom.put(ORDER_ROOMS, orderIdx + "", orderRoom);
        return orderRoom;
    }

//    public void plusUserCnt(Long orderRoomIdx) {
//        orderRoomRepository.plusMemberCnt(orderRoomIdx);
//    }
//
//    public void minusUserCnt(Long orderRoomIdx) {
//        orderRoomRepository.minusMemberCnt(orderRoomIdx);
//    }

    @RedisLock(lockName = "lock:orderRoom:#{#orderIdx}")
    public void minusMemberCnt(Long orderIdx, Long memberIdx) {
        OrderRoom orderRoom = getOrderRoom(orderIdx);
        if (orderRoom.exitMember(memberIdx)) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_MEMBER_CNT_CANNOT_BE_MINUS);
        }
        opsHashOrderRoom.put(ORDER_ROOMS, orderIdx + "", orderRoom);
    }

    public void deleteOrderRoom(Long orderIdx) {
        opsHashOrderRoom.delete(ORDER_ROOMS, orderIdx + "");
    }

    // 유저가 입장해 있는 주문방 ID 조회
    public Long getMemberEnteredOrderRoomIdx(Long memberIdx) {
        if (opsHashEnterInfo.get(ENTER_INFO, memberIdx + "") == null) {
            return null;
        }
        return Long.parseLong(opsHashEnterInfo.get(ENTER_INFO, memberIdx + "") + "");
    }

    // 사용자가 특정 주문방에 입장해 있는지 확인
    public boolean existMemberInOrderRoom(Long orderIdx, Long memberIdx) {
        return getMemberEnteredOrderRoomIdx(memberIdx).equals(orderIdx);
    }

    // 사용자 퇴장
    public void exitMemberEnterOrderRoom(Long memberIdx) {
        opsHashEnterInfo.delete(ENTER_INFO, memberIdx + "");
    }

    // 이미 참여중인지
    public boolean existMyInfo(Long memberIdx) {
        return opsHashEnterInfo.hasKey(ENTER_INFO, memberIdx + "");
    }

    public void saveMySessionInfo(String sessionId, String memberId) {
        opsHashMemberSessionInfo.put(MEMBER_INFO, sessionId, memberId);
    }

    public boolean existMySessionInfo(String sessionId) {
        return opsHashMemberSessionInfo.hasKey(MEMBER_INFO, sessionId);
    }

    public String getMySessionInfo(String sessionId) {
        return opsHashMemberSessionInfo.get(MEMBER_INFO, sessionId);
    }

    public void deleteMySessionInfo(String sessionId) {
        opsHashMemberSessionInfo.delete(MEMBER_INFO, sessionId);
    }


}
