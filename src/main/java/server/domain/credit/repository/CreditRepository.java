package server.domain.credit.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.account.domain.Account;
import server.domain.credit.domain.Credit;
import server.domain.credit.mapper.CreditMapper;
import server.domain.transaction.domain.Transaction;
import server.domain.transaction.repository.TransactionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CreditRepository {

    private final CreditMapper creditMapper;

    public void save(Credit credit) {
        creditMapper.save(credit);
    }

    public void payPrice(Long creditIdx, int amountSum) {
        Map<String, Object> map = Map.of("creditIdx", creditIdx, "amountSum", amountSum);
        creditMapper.payPrice(map);
    }

    public Optional<Credit> findByCreditIdx(Long creditIdx) {
        Credit credit = creditMapper.findByCreditIdx(creditIdx);
        if (credit != null) {
            return Optional.of(credit);
        }
        return Optional.empty();
    }

    public Optional<Credit> findByCreditNumber(String creditNumber) {
        Credit credit = creditMapper.findByCreditNumber(creditNumber);
        if (credit != null) {
            return Optional.of(credit);
        }
        return Optional.empty();
    }

    public List<Credit> findAllCreditByMemberIdx(Long memberIdx) {
        return creditMapper.findAllByMemberIdx(memberIdx);
    }


    public boolean existsByCreditNumber(String creditNumber) {
        Credit credit = creditMapper.findByCreditNumber(creditNumber);
        return credit != null;
    }

    public boolean existsByCreditIdxAndMemberIdx(Long idx, Long memberIdx) {
        Map<String, Object> map = Map.of("idx", idx, "memberIdx", memberIdx);
        Credit credit = creditMapper.findByIdxAndMemberIdx(map);
        return credit != null;
    }



    public void delete(Long idx) {
        creditMapper.delete(idx);
    }

    public List<Credit> findAllByMemberIdx(Long memberIdx) {
        return creditMapper.findAllByMemberIdx(memberIdx);
    }


    public Optional<Credit> findMemberIdxAndCreditNumber(Long memberIdx, String creditNumber) {
        Map<String, Object> map = Map.of("memberIdx", memberIdx, "creditNumber", creditNumber);
        Credit credit = creditMapper.findMemberIdxAndCreditNumber(map);
        if (credit != null) {
            return Optional.of(credit);
        }
        return Optional.empty();
    }

    public void updateAmountSum(Long idx,Integer amountSum) {
        Map<String, Object> map = Map.of("idx", idx, "amountSum", amountSum);
        System.out.println("map = " + map);
        creditMapper.updateAmountSum(map);
    }
}
