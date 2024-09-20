package server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberRequestDto {

    @Data
    public static class MemberSignupRequestDto {
        private String memberId;
        private String name;
        private String email;
        private String password;
        private String phoneNum;

        @JsonCreator
        public MemberSignupRequestDto(@JsonProperty("memberId") String memberId,
                                      @JsonProperty("email") String email,
                                      @JsonProperty("password") String password,
                                      @JsonProperty("phoneNum") String phoneNum,
                                      @JsonProperty("name") String name) {
            this.memberId = memberId;
            this.email = email;
            this.password = password;
            this.phoneNum = phoneNum;
            this.name = name;
        }

    }
}
