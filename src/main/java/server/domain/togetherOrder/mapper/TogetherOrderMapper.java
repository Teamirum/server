package server.domain.togetherOrder.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.togetherOrder.domain.TogetherOrder;

import java.util.List;

@Mapper
public interface TogetherOrderMapper {

    void save(TogetherOrder togetherOrder);

    List<TogetherOrder> findByOrderIdx(Long orderIdx);

    List<TogetherOrder> findByMemberIdx(Long memberIdx);

    TogetherOrder findByMemberIdxAndOrderIdx(Long memberIdx, Long orderIdx);

    void deleteByOrderIdx(Long orderIdx);

    void deleteByMemberIdx(Long memberIdx);

    void deleteByMemberIdxAndOrderIdx(Long memberIdx, Long orderIdx);




}
