package server.domain.account.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.account.domain.AccountHistory;
import server.domain.account.mapper.AccountHistoryMapper;

import java.util.List;
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

}
