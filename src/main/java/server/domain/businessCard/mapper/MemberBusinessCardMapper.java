package server.domain.businessCard.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.businessCard.domain.BusinessCard;
import server.domain.businessCard.domain.MemberBusinessCard;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberBusinessCardMapper {

    // 명함 ID로 조회
    MemberBusinessCard findByBusinessCardIdx(Long businessCardIdx);

    // 회원 ID와 명함 ID로 조회
    MemberBusinessCard findByMemberIdxAndBusinessCardIdx(Long memberIdx, Long businessCardIdx);

    MemberBusinessCard findByMemberIdx(Long memberIdx);

    // 명함 저장
    void save(MemberBusinessCard memberBusinessCard);

    // 명함 삭제
    void delete(Map<String, Object> map);

    // 명함 상태 업데이트
    void updateStatus(MemberBusinessCard memberBusinessCard);

    MemberBusinessCard findByMemberIdxAndBusinessCardIdx(Map<String, Object> map);

    MemberBusinessCard findByFriendBusinessCardIdx(Map<String, Object> map);

}
