package server.domain.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.order.domain.TogetherOrder;
import server.domain.order.mapper.TogetherOrderMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TogetherOrderRepository {

    private final TogetherOrderMapper togetherOrderMapper;

    public void save(TogetherOrder togetherOrder) {
        togetherOrderMapper.save(togetherOrder);
    }

    public List<TogetherOrder> findByOrderIdx(Long orderIdx) {
        return togetherOrderMapper.findByOrderIdx(orderIdx);
    }

    public List<TogetherOrder> findByMemberIdx(Long memberIdx) {
        return togetherOrderMapper.findByMemberIdx(memberIdx);
    }

    public Optional<TogetherOrder> findByMemberIdxAndOrderIdx(Long memberIdx, Long orderIdx) {
        TogetherOrder togetherOrder = togetherOrderMapper.findByMemberIdxAndOrderIdx(memberIdx, orderIdx);
        if (togetherOrder != null) {
            return Optional.of(togetherOrder);
        }
        return Optional.empty();
    }

    public void deleteByOrderIdx(Long orderIdx) {
        togetherOrderMapper.deleteByOrderIdx(orderIdx);
    }

    public void deleteByMemberIdx(Long memberIdx) {
        togetherOrderMapper.deleteByMemberIdx(memberIdx);
    }

    public void deleteByMemberIdxAndOrderIdx(Long memberIdx, Long orderIdx) {
        togetherOrderMapper.deleteByMemberIdxAndOrderIdx(memberIdx, orderIdx);
    }
}
