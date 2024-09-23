package server.global.oauth.infra.oauth.kakao.authcode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import server.global.config.KakaoOauthConfig;
import server.global.oauth.OauthServerType;
import server.global.oauth.domain.authcode.AuthCodeRequestUrlProvider;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthConfig.getClientId()) // Changed to getter convention
                .queryParam("client_secret", kakaoOauthConfig.getClientSecret()) // Changed to getter convention
                .queryParam("redirect_uri", kakaoOauthConfig.getRedirectUri()) // Changed to getter convention
                .queryParam("scope", String.join(",", kakaoOauthConfig.getScope())) // Changed to getter convention
                .toUriString();
    }
}
