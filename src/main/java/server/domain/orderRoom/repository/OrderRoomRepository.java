package server.domain.orderRoom.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.order.domain.TogetherOrder;
import server.domain.orderRoom.domain.OrderRoom;
import server.domain.orderRoom.mapper.OrderRoomMapper;
import server.domain.orderRoom.model.OrderRoomStatus;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRoomRepository {

    private final OrderRoomMapper orderRoomMapper;

    public void save(OrderRoom orderRoom) {
        orderRoomMapper.save(orderRoom);
    }

    public Optional<OrderRoom> findByOrderIdx(Long orderIdx) {
        OrderRoom orderRoom = orderRoomMapper.findByOrderIdx(orderIdx);
        if (orderRoom != null) {
            return Optional.of(orderRoom);
        }
        return Optional.empty();
    }

    public Optional<OrderRoom> findByOwnerMemberIdxAndOrderIdx(Long ownerMemberIdx, Long orderIdx) {
        Map<String, Object> map = Map.of("ownerMemberIdx", ownerMemberIdx, "orderIdx", orderIdx);
        OrderRoom orderRoom = orderRoomMapper.findByOwnerMemberIdxAndOrderIdx(map);
        if (orderRoom != null) {
            return Optional.of(orderRoom);
        }
        return Optional.empty();
    }

    public List<OrderRoom> findAllByOwnerMemberIdx(Long memberIdx) {
        return orderRoomMapper.findAllByOwnerMemberIdx(memberIdx);
    }

    public int getMemberCnt(Long orderIdx) {
        return orderRoomMapper.getMemberCntByOrderIdx(orderIdx);
    }

    public int getMaxMemberCnt(Long orderIdx) {
        return orderRoomMapper.getMaxMemberCntByOrderIdx(orderIdx);
    }

    public void minusMemberCnt(Long orderIdx) {
        int memberCnt = getMemberCnt(orderIdx);
        if (memberCnt <= 0) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_MEMBER_CNT_CANNOT_BE_MINUS);
        }
        orderRoomMapper.minusMemberCnt(orderIdx);
    }

    public void plusMemberCnt(Long orderIdx) {
        int maxMemberCnt = getMaxMemberCnt(orderIdx);
        if (getMemberCnt(orderIdx) >= maxMemberCnt) {
            throw new ErrorHandler(ErrorStatus.ORDER_ROOM_MEMBER_CNT_CANNOT_EXCEED);
        }
        orderRoomMapper.plusMemberCnt(orderIdx);
    }

    public void updateStatus(Long orderIdx, OrderRoomStatus status) {
        Map<String, Object> map = Map.of("orderIdx", orderIdx, "status", status);
        orderRoomMapper.updateStatus(map);
    }

    public void deleteByOrderIdx(Long orderIdx) {
        orderRoomMapper.deleteByOrderIdx(orderIdx);
    }
}
