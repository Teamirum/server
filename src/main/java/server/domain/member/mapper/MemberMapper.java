package server.domain.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import server.domain.member.domain.Member;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    Member findByMemberId(String memberId);

    void save(Member member);

    void updateRefreshToken(Member member);
}
