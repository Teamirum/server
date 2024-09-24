package server.global.auth.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.member.domain.Member;
import server.global.auth.security.mapper.UserDetailsMapper;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDetailsRepository {

    private final UserDetailsMapper userDetailsMapper;

    public Optional<Member> findByMemberId(String username) {
        Member member = userDetailsMapper.findByMemberId(username);
        if (member != null) {
            return Optional.of(member);
        }
        return Optional.empty();
    }

}
