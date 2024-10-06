package server.domain.account.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.account.domain.Account;
import server.domain.account.dto.AccountRequestDto;
import server.domain.account.mapper.AccountMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final AccountMapper accountMapper;

    public List<Account> findAllAccountByMemberIdx(Long memberIdx) {
        return accountMapper.findAllByMemberIdx(memberIdx);
    }


    public Optional<Account> findByAccountIdx(Long accountIdx) {
        Account account = accountMapper.findByAccountIdx(accountIdx);
        if (account != null) {
            return Optional.of(account);
        }
        return Optional.empty();
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        Account account = accountMapper.findByAccountNumber(accountNumber);
        if (account != null) {
            return Optional.of(account);
        }
        return Optional.empty();
    }

    public void save(Account account) {
        accountMapper.save(account);
    }

    public void delete(Long idx) {
        accountMapper.delete(idx);
    }

    public void updateAccountAmount(Long idx, Integer amount) {
        accountMapper.updateAccountAmount(idx, amount);
    }


    public boolean existsByAccountNumber(String accountNumber) {
        Account account = accountMapper.findByAccountNumber(accountNumber);
        return account != null;
    }

    public boolean existsByAccountIdxAndMemberIdx(Long idx, Long memberIdx) {
        Map<String, Object> map = Map.of("idx", idx, "memberIdx", memberIdx);
        Account account = accountMapper.findByIdxAndMemberIdx(map);
        return account != null;
    }

    public void payPrice(Long idx, int price) {
        Map<String, Object> map = Map.of("idx", idx, "price", price);
        accountMapper.payPrice(map);
    }
}
