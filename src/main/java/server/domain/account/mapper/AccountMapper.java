package server.domain.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.account.domain.Account;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {

    // 계좌 저장 (Insert 또는 Update)
//    void save(Account account);

   // 내 계좌 조회
   List<Account> findByMemberId(Long memberId);

    // 계좌 ID로 계좌 조회
//    Optional<Account> findById(Long accountId);

    // 계좌 삭제
//    void deleteById(Long accountId);
}
