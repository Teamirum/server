package server.domain.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.account.domain.Account;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface AccountMapper {

    List<Account> findAllByMemberIdx(Long memberIdx);

    Account findByAccountIdx(Long accountIdx);

    Account findByAccountNumber(String accountNumber);

    Account findByIdxAndMemberIdx(Map<String, Object> map);

    void save(Account account);

    void delete(Long idx);

    void updateAccountAmount(Long idx, Integer amount);

}
