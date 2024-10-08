package server.domain.credit.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.account.domain.AccountHistory;
import server.domain.credit.domain.CreditHistory;
import server.domain.credit.mapper.CreditHistoryMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<CreditHistory> findByCreditHistoryIdx(Long creditIdx, Long historyIdx, Long memberIdx) {
        Map<String, Object> map = Map.of("creditIdx", creditIdx, "historyIdx", historyIdx, "memberIdx", memberIdx);
        CreditHistory creditHistory = creditHistoryMapper.findByCreditHistoryIdx(map);
        if (creditHistory != null) {
            return Optional.of(creditHistory);
        }
        return Optional.empty();
    }



}

