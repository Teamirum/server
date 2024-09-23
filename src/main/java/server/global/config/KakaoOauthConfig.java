package server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOauthConfig {

    @Value("${oauth.kakao.redirect_uri}") // application.properties의 속성명에 맞춤
    private String redirectUri;

    @Value("${oauth.kakao.client_id}") // application.properties의 속성명에 맞춤
    private String clientId;

    @Value("${oauth.kakao.client_secret}") // application.properties의 속성명에 맞춤
    private String clientSecret;

    @Value("${oauth.kakao.scope}") // 쉼표로 구분된 스코프 문자열을 배열로 변환
    private String[] scope;

    // Getter 메서드
    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String[] getScope() {
        return scope;
    }
}
