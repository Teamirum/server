package server.domain.message.dto;

public class MessageDtoConverter {

    public static MessageResponseDto.CheckMessageSuccessResponseDto convertToCheckMessageSuccessResponseDto(boolean isSuccess, String authToken) {
        return MessageResponseDto.CheckMessageSuccessResponseDto.builder()
                .isSuccess(isSuccess)
                .authToken(authToken)
                .build();
    }

}
