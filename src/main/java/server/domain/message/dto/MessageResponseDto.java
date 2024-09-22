package server.domain.message.dto;

import lombok.Builder;
import lombok.Data;

public class MessageResponseDto {

    @Builder
    @Data
    public static class SendMessageResponseDto {
        private Boolean isSuccess;
    }

    @Builder
    @Data
    public static class CheckMessageSuccessResponseDto {
        private Boolean isSuccess;
        private String authToken;
    }

}
