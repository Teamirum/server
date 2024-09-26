package server.domain.account.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.account.domain.Account;
import server.domain.account.mapper.AccountMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final AccountMapper accountMapper;

    // 특정 회원의 계좌 조회
    public List<Account> findByMemberId(Long memberId) {
        return accountMapper.findByMemberId(memberId);
    }

    // 계좌 저장
//    public void save(Account account) {
//        accountMapper.save(account);
//    }

    // 계좌 삭제
//    public void deleteById(Long accountId) {
//        accountMapper.deleteById(accountId);
//    }

    // 계좌 조회
//    public Optional<Account> findById(Long accountId) {
//        return accountMapper.findById(accountId);
//    }
}
