package server.domain.credit.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.credit.domain.CreditHistory;
import server.domain.credit.mapper.CreditHistoryMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CreditHistoryRespository {

    private final CreditHistoryMapper creditHistoryMapper;

    public void save(CreditHistory creditHistory) {
        creditHistoryMapper.save(creditHistory);
    }

    public void delete(Long idx) {
        creditHistoryMapper.delete(idx);
    }

    public List<CreditHistory> findAllCreditHistoryByCreditIdx(Long creditIdx) {
        return creditHistoryMapper.findAllByCreditIdx(creditIdx);
    }
}
