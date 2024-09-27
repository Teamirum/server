package server.domain.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import server.domain.account.domain.Account;
import server.domain.account.dto.AccountRequestDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountMapper {

    List<Account> findAllByMemberIdx(Long memberIdx);

    Account findByAccountIdx(Long accountIdx);

    Account findByAccountNumber(String accountNumber);

    Account findByIdxAndMemberIdx(Map<String, Object> map);

    void save(Account account);

    void delete(Long idx);

    void updateAccountAmount(@Param("idx") Long idx, @Param("amount") Integer amount);


}
