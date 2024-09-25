package server.global.auth.security.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.member.domain.Member;

import java.util.Optional;

@Mapper
public interface UserDetailsMapper {
    Member findByMemberId(String memberId);
}
