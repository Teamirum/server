package server.domain.message.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

public class MessageRequestDto {

    @Data
    public static class CheckMessageRequestDto {
        private String phoneNum;
        private String authNum;

        @JsonCreator
        public CheckMessageRequestDto(@JsonProperty("phoneNum") String phoneNum,
                                      @JsonProperty("authNum") String authNum) {
            this.phoneNum = phoneNum;
            this.authNum = authNum;
        }
    }
}
