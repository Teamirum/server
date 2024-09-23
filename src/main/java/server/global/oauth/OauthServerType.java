package server.global.oauth;

import org.springframework.util.StringUtils;

import java.util.Locale;


//Oauth2.0 인증을 제공하는 서버의 종류를 명시하는 enum
public enum OauthServerType {

    KAKAO;

    public static OauthServerType fromName(String type) {
        if (StringUtils.hasText(type)) {
            return OauthServerType.valueOf(type.toUpperCase(Locale.ENGLISH));
        }
        throw new IllegalArgumentException("Type must not be empty");
    }
}