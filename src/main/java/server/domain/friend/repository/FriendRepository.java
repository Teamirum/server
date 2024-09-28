package server.domain.friend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.account.domain.Account;
import server.domain.friend.domain.Friend;
import server.domain.friend.mapper.FriendMapper;
import server.domain.member.domain.Member;
import server.domain.member.mapper.MemberMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    private final FriendMapper friendMapper;

    public Optional<Friend> findByFriendMemberId(Long friendMemberId) {
        Friend friend = friendMapper.findByFriendMemberId(friendMemberId);
        if (friend != null) {
            return Optional.of(friend);
        }
        return Optional.empty();
    }


    // 친구 memberIdx로 Member에서 전화번호 가져와서 확인
    public boolean existsByFriendIdxAndMemberIdx(Long idx, Long memberIdx) {
        Map<String, Object> map = Map.of("idx", idx, "memberIdx", memberIdx);
        Friend friend = friendMapper.findByIdxAndMemberIdx(map);
        return friend != null;
    }



    public void save(Friend friend) {
        friendMapper.save(friend);
    }

    public void delete(Long idx) {
        friendMapper.delete(idx);
    }

    public void updateFavorite(Long idx, boolean isFavorite) {
        friendMapper.updateFavorite(idx, isFavorite);
    }


}
