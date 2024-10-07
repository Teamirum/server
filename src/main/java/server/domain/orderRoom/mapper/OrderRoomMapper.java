package server.domain.orderRoom.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.orderRoom.domain.OrderRoom;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderRoomMapper {

    void save(OrderRoom orderRoom);

    OrderRoom findByOrderIdx(Long orderIdx);

    OrderRoom findByOwnerMemberIdxAndOrderIdx(Map<String, Object> map);

    int getMemberCntByOrderIdx(Long orderIdx);

    int getMaxMemberCntByOrderIdx(Long orderIdx);

    List<OrderRoom> findAllByOwnerMemberIdx(Long memberIdx);

    void updateStatus(Map<String, Object> map);

    void deleteByOrderIdx(Long orderIdx);

    void minusMemberCnt(Long orderIdx);

    void plusMemberCnt(Long orderIdx);
}
