package server.domain.friend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class FirendRequestDto {

    @Data
    public static class UploadFriendRequestDto {
        public Long friendMemberId;

        @JsonCreator
        public UploadFriendRequestDto(
                @JsonProperty("friendMemberId") Long friendMemberId
        ) {
            this.friendMemberId = friendMemberId;
        }
    }

    @Data
    public static class UpdateFriendFavoriteRequestDto {
        public Long idx;
        public boolean isFavorite;

        @JsonCreator
        public UpdateFriendFavoriteRequestDto(
                @JsonProperty("idx") Long idx,
                @JsonProperty("isFavorite") boolean isFavorite
        ) {
            this.idx = idx;
            this.isFavorite = isFavorite;
        }
    }
}
