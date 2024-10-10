package server.domain.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.order.domain.TogetherOrder;

import java.util.List;
import java.util.Map;

@Mapper
public interface TogetherOrderMapper {

    void save(TogetherOrder togetherOrder);

    List<TogetherOrder> findByOrderIdx(Long orderIdx);

    List<TogetherOrder> findByMemberIdx(Long memberIdx);

    TogetherOrder findByMemberIdxAndOrderIdx(Map<String, Object> map);

    void deleteByOrderIdx(Long orderIdx);

    void deleteByMemberIdx(Long memberIdx);

    void deleteByMemberIdxAndOrderIdx(Long memberIdx, Long orderIdx);


    void updateStatusByIdx(Map<String, Object> map);
}
