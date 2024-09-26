package server.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.member.domain.Member;
import server.domain.member.mapper.MemberMapper;
import server.global.auth.oauth2.model.SocialType;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper memberMapper;

    public Optional<Member> findByMemberId(String memberId) {
        Member member = memberMapper.findByMemberId(memberId);
        if (member != null) {
            return Optional.of(member);
        }
        return Optional.empty();
    }

    public void save(Member member) {
        memberMapper.save(member);
    }

    public void updateRefreshToken(Member member) {
        memberMapper.updateRefreshToken(member);
    }

    public boolean existsByMemberId(String memberId) {
        return memberMapper.findByMemberId(memberId) != null;
    }

    public boolean existsByPhoneNum(String memberId) {
        return memberMapper.findByPhoneNum(memberId) != null;
    }

    public Long getIdxByMemberId(String memberId) {
        return memberMapper.getIdxByMemberId(memberId);
    }

    public Optional<Member> findBySocialTypeAndMemberId(SocialType socialType, String memberId) {
        Map<String, Object> map = Map.of("socialType", socialType.name(), "memberId", memberId);
        Member member = memberMapper.findBySocialTypeAndMemberId(map);
        if (member != null) {
            return Optional.of(member);
        }
        return Optional.empty();
    }
}
