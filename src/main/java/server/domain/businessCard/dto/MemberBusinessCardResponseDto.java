package server.domain.businessCard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class MemberBusinessCardResponseDto {

    @Data
    @Builder
    public static class MemberBusinessCardTaskSuccessResponseDto {
        public boolean isSuccess;
        public Long idx;
        public String memo;
    }

    @Data
    @Builder
    public static class MemberBusinessCardListResponseDto {
        public int cnt;
        List<MemberBusinessCardInfoResponseDto> memberBusinessCardList;
        public boolean isSuccess;
    }

    @Data
    @Builder
    public static class MemberBusinessCardInfoResponseDto {
        public Long idx;
        public Long memberIdx;
        public Long businessCardIdx;
        public String status;
        public String memo;
    }
}
