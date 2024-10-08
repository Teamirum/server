package server.domain.account.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.account.domain.AccountHistory;
import server.domain.account.mapper.AccountHistoryMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountHistoryRepository {

    private final AccountHistoryMapper accountHistoryMapper;

    public void save(AccountHistory accountHistory) {
        accountHistoryMapper.save(accountHistory);
    }

    public void delete(Long idx) {
        accountHistoryMapper.delete(idx);
    }

    public List<AccountHistory> findAllAccountHistoryByAccountIdx(Long accountIdx) {
        return accountHistoryMapper.findAllByAccountIdx(accountIdx);
    }

    public Optional<AccountHistory> findByAccountHistoryIdx(Long accountIdx, Long historyIdx, Long memberIdx) {
        Map<String, Object> map = Map.of("accountIdx", accountIdx, "historyIdx", historyIdx, "memberIdx", memberIdx);
        AccountHistory accountHistory = accountHistoryMapper.findByAccountHistoryIdx(map);
        if (accountHistory != null) {
            return Optional.of(accountHistory);
        }
        return Optional.empty();
    }


}
