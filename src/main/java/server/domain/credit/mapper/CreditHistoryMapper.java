package server.domain.credit.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.credit.domain.CreditHistory;

import java.util.List;
import java.util.Map;

@Mapper
public interface CreditHistoryMapper {

    void save(CreditHistory creditHistory);

    CreditHistory findByIdx(Long idx);

    List<CreditHistory> findAllByCreditIdx(Long creditIdx);

    List<CreditHistory> findAllByCreditIdxAndDateRange(Map<String, Object> map);

    void update(CreditHistory creditHistory);

    void delete(Long idx);

    void insertCreditHistory(CreditHistory creditHistory);
}
