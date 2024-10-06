package server.domain.credit.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.credit.domain.Credit;
import server.domain.credit.mapper.CreditMapper;

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

    public void updateCreditImage(Long idx, String imgUrl) {
        Map<String, Object> map = Map.of("creditIdx", idx, "imgUrl", imgUrl);
        creditMapper.updateCreditImage(map);
    }

    public boolean existsByCreditNumber(String creditNumber) {
        Credit credit = creditMapper.findByCreditNumber(creditNumber);
        return credit != null;
    }

    public boolean existsByCreditIdxAndMemberIdx(Long creditIdx, Long memberIdx) {
        Map<String, Object> map = Map.of("creditIdx", creditIdx, "memberIdx", memberIdx);
        Credit credit = creditMapper.findByIdxAndMemberIdx(map);
        return credit != null;
    }

    public void deleteCredit(Long creditIdx) {
        creditMapper.deleteCredit(creditIdx);
    }

    public List<Credit> findAllByMemberIdx(Long memberIdx) {
        return creditMapper.findAllByMemberIdx(memberIdx);
    }

}
