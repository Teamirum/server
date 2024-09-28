package server.domain.friend.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.friend.domain.Friend;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendMapper {

    List<Friend> findAllByMemberIdx(Long memberIdx);

    Friend findByFriendMemberId(Long friendMemberId);

    Friend findByIdxAndMemberIdx(Map<String, Object> map);

    void save(Friend friend);

    void delete(Long idx);

    void updateFavorite(Long idx, boolean isFavorite);
}
