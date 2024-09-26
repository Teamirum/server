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

    public Optional<Credit> findByCreditIdx(Long creditIdx) {
        Credit credit = creditMapper.findByCreditIdx(creditIdx);
        if (credit != null) {
            return Optional.of(credit);
        }
        return Optional.empty();
    }

    public void updateCreditImage(Long idx, String imgUrl) {
        Map<String, Object> map = Map.of("creditIdx", idx, "imgUrl", imgUrl);
        creditMapper.updateCreditImage(map);
    }

    public List<Credit> findAllByMemberIdx(Long memberIdx) {
        return creditMapper.findAllByMemberIdx(memberIdx);
    }

}
