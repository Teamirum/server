package server.domain.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import server.domain.account.domain.AccountHistory;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountHistoryMapper {

    List<AccountHistory> findAllByAccountIdx(Long accountIdx);

    AccountHistory findByIdx(Long idx);

    void save(AccountHistory accountHistory);

    void delete(Long idx);

    void updateAccountHistory(AccountHistory accountHistory);

    AccountHistory findByAccountHistoryIdx(Map<String, Object> map);
}
