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


    public Optional<MemberBusinessCard> findByMemberIdxAndBusinessCardIdx(Long memberIdx, Long businessCardIdx) {
        Map<String, Object> map = Map.of("memberIdx", memberIdx, "businessCardIdx", businessCardIdx);
        System.out.println("map = " + map);
        MemberBusinessCard memberBusinessCard = memberBusinessCardMapper.findByMemberIdxAndBusinessCardIdx(map);
        if (memberBusinessCard != null) {
            return Optional.of(memberBusinessCard);
        }
        return Optional.empty();
    }

    // 명함 등록
    public void save(MemberBusinessCard memberBusinessCard) {
        memberBusinessCardMapper.save(memberBusinessCard);
    }

    // 명함 삭제
    public void delete(Long memberIdx, Long businessCardIdx) {
        Map<String, Object> map = Map.of("memberIdx", memberIdx, "businessCardIdx", businessCardIdx);
        memberBusinessCardMapper.delete(map);
    }

    // 명함 상태 업데이트
    public void update(MemberBusinessCard memberBusinessCard) {
        memberBusinessCardMapper.update(memberBusinessCard);
    }

    public boolean existsByMemberIdxAndBusinessCardIdx(Long idx, Long memberIdx) {
        Map<String, Object> map = Map.of("idx", idx, "memberIdx", memberIdx);
        BusinessCard businessCard = BusinessCardMapper.findByIdxAndMemberIdx(map);
        return businessCard != null;
    }

    public boolean existsFriendBusinessCardIdx(Long businessCardIdx, Long memberIdx) {
        Map<String, Object> map = Map.of("memberIdx", memberIdx, "businessCardIdx", businessCardIdx);
        MemberBusinessCard memberBusinessCard = memberBusinessCardMapper.findByFriendBusinessCardIdx(map);
        return memberBusinessCard != null;
    }

    public boolean existsByMemberIdxAndStatus(Long memberIdx) {
        MemberBusinessCard memberBusinessCard = memberBusinessCardMapper.findByMemberIdxAndStatus(memberIdx);
        return memberBusinessCard!=null;
    }

}
