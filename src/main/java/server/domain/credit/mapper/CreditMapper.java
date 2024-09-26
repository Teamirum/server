package server.domain.credit.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.credit.domain.Credit;

import java.util.List;
import java.util.Map;

@Mapper
public interface CreditMapper {

    void save(Credit credit);

    Credit findByCreditIdx(Long creditIdx);

    List<Credit> findAllByMemberIdx(Long memberIdx);

    void updateCreditImage(Map<String, Object> map);
}
