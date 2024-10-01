package server.domain.businessCard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class MemberBusinessCardRequestDto {

    @Data
    public static class UploadMemberBusinessCardRequestDto {
        public Long memberIdx;
        public Long businessCardIdx;
        public String status;
        public String memo;

        @JsonCreator
        public UploadMemberBusinessCardRequestDto(
                @JsonProperty("memberIdx") Long memberIdx,
                @JsonProperty("businessCardIdx") Long businessCardIdx,
                @JsonProperty("status") String status,
                @JsonProperty("memo") String memo
        ) {
            this.memberIdx = memberIdx;
            this.businessCardIdx = businessCardIdx;
            this.status = status;
            this.memo = memo;
        }
    }

    @Data
    public static class UpdateMemberBusinessCardRequestDto {
        public Long idx;
        public String status;
        public String memo;

        @JsonCreator
        public UpdateMemberBusinessCardRequestDto(
                @JsonProperty("idx") Long idx,
                @JsonProperty("status") String status,
                @JsonProperty("memo") String memo
        ) {
            this.idx = idx;
            this.status = status;
            this.memo = memo;
        }
    }
}
