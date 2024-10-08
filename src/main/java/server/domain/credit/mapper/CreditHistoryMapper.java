package server.domain.credit.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.credit.domain.CreditHistory;

import java.util.List;
import java.util.Map;

@Mapper
public interface CreditHistoryMapper {

  List<CreditHistory> findAllByCreditIdx(Long creditIdx);

  CreditHistory findByIdx(Long idx);

    void save(CreditHistory creditHistory);

    void delete(Long idx);

    void updateCreditHistory(CreditHistory creditHistory);

    CreditHistory findByCreditHistoryIdx(Map<String, Object> map);
}
