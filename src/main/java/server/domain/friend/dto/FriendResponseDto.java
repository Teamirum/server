package server.domain.friend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class FriendResponseDto {

    @Data
    @Builder
    public static class FriendTaskSuccessResponseDto {
        public boolean isSuccess;
        public Long idx;
    }

    @Data
    @Builder
    public static class FriendListResponseDto {
        public int cnt;
        List<FriendInfoResponseDto> friendList;
        public boolean isSuccess;
    }

    @Data
    @Builder
    public static class FriendInfoResponseDto {
        public Long idx;
        public Long friendMemberId;
        public String createdAt;
        public boolean isFavorite;
    }
}
