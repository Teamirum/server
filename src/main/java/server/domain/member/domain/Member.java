package server.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.member.model.Role;
import server.global.auth.oauth2.model.SocialType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    private Long idx;

    private String memberId;

    private String name;

    private String email;

    private String password;

    private String phoneNum;

    private Role role;

    private String refreshToken;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private SocialType socialType;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
