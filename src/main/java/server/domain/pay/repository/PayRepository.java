package server.domain.pay.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.pay.domain.Pay;
import server.domain.pay.mapper.PayMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PayRepository {

    private final PayMapper payMapper;

    public void save(Pay pay) {
        payMapper.save(pay);
    }

    public List<Pay> findAllByOrderIdx(Long orderIdx) {
        return payMapper.findAllByOrderIdx(orderIdx);
    }

    public List<Pay> findAllByMemberIdx(Long memberIdx) {
        return payMapper.findAllByMemberIdx(memberIdx);
    }

    public Optional<Pay> findByOrderIdxAndMemberIdx(Long orderIdx, Long memberIdx) {
        Map<String, Long> map = Map.of("orderIdx", orderIdx, "memberIdx", memberIdx);
        Pay byOrderIdxAndMemberIdx = payMapper.findByOrderIdxAndMemberIdx(map);
        if (byOrderIdxAndMemberIdx != null) {
            return Optional.of(byOrderIdxAndMemberIdx);
        }
        return Optional.empty();
    }
}
