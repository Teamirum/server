package server.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.member.domain.Member;
import server.domain.member.mapper.MemberMapper;

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

    public void updateRefreshToken(Member member) {
        memberMapper.updateRefreshToken(member);
    }
}
