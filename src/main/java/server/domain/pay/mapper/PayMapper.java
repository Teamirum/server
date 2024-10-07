package server.domain.pay.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.pay.domain.Pay;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayMapper {

    void save(Pay pay);

    List<Pay> findAllByOrderIdx(Long orderIdx);

    List<Pay> findAllByMemberIdx(Long memberIdx);

    Pay findByOrderIdxAndMemberIdx(Map<String, Long> map);
}
