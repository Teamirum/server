package server.global.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.member.domain.Member;
import server.global.security.mapper.UserDetailsMapper;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDetailsRepository {

    private final UserDetailsMapper userDetailsMapper;

    public Optional<Member> findByMemberId(String memberId) {
        Member member = userDetailsMapper.findByMemberId(memberId);
        if (member != null) {
            return Optional.of(member);
        }
        return Optional.empty();
    }

}
