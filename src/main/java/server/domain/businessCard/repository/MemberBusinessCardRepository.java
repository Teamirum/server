package server.domain.businessCard.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.domain.MemberBusinessCard;
import server.domain.businessCard.mapper.BusinessCardMapper;
import server.domain.businessCard.mapper.MemberBusinessCardMapper;

import java.io.ObjectStreamClass;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberBusinessCardRepository {

    private final MemberBusinessCardMapper memberBusinessCardMapper;
    private final BusinessCardMapper BusinessCardMapper;



    // 특정 명함 ID로 조회
    public Optional<MemberBusinessCard> findByMemberIdx(Long businessCardIdx) {
        return Optional.ofNullable(memberBusinessCardMapper.findByBusinessCardIdx(businessCardIdx));
    }

    // 특정 회원 ID와 명함 ID로 조회
    public Optional<MemberBusinessCard> findByMemberIdxAndBusinessCardIdx(Long memberIdx, Long businessCardIdx) {
        return Optional.ofNullable(memberBusinessCardMapper.findByMemberIdxAndBusinessCardIdx(memberIdx, businessCardIdx));
    }

    // 명함 등록
    public void save(MemberBusinessCard memberBusinessCard) {
        memberBusinessCardMapper.save(memberBusinessCard);
    }

    // 명함 삭제
    public void delete(Long idx) {
        memberBusinessCardMapper.delete(idx);
    }

    // 명함 상태 업데이트
    public void updateStatus(MemberBusinessCard memberBusinessCard) {
        memberBusinessCardMapper.updateStatus(memberBusinessCard);
    }

    public boolean existsByMemberIdxAndBusinessCardIdx(Long idx, Long memberIdx) {
        Map<String, Object> map = Map.of("idx", idx, "memberIdx", memberIdx);
        BusinessCard businessCard = BusinessCardMapper.findByIdxAndMemberIdx(map);
        return businessCard != null;
    }


}
