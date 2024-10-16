package server.domain.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.member.domain.Member;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    Member findByMemberId(String memberId);

    Member findByIdx(Long memberIdx);

    Member findByPhoneNum(String phoneNum);

    void save(Member member);

    void updateRefreshToken(Member member);

    Member findBySocialTypeAndMemberId(Map<String, Object> map);

    Long getIdxByMemberId(String memberId);

    void updateConnected(Member member);

}
