package server.domain.member.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class MemberResponseDto {

    @Data
    @Builder
    public static class MemberTaskResultResponseDto {
        private Long idx;
        private String memberId;
        private Boolean isSuccess;
    }

    @Data
    @Builder
    public static class MemberTaskSuccessResponseDto {
        private Boolean isSuccess;
        private Boolean isConnected;

    }

}
