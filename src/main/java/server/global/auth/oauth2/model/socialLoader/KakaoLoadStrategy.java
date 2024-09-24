package server.global.auth.oauth2.model.socialLoader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.auth.oauth2.model.SocialType;
import server.global.auth.oauth2.model.info.KakaoOAuth2UserInfo;
import server.global.auth.oauth2.model.info.OAuth2UserInfo;

import java.util.Map;

@Slf4j
public class KakaoLoadStrategy extends SocialLoadStrategy{



    protected OAuth2UserInfo sendRequestToSocialSite(HttpEntity request){
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(SocialType.KAKAO.getUserInfoUrl(),// -> /v2/user/me
                    SocialType.KAKAO.getMethod(),
                    request,
                    RESPONSE_TYPE);

            log.info("Kakao response: {}", response.getBody());

            return new KakaoOAuth2UserInfo(response.getBody());

        } catch (Exception e) {
            log.error(ErrorStatus.KAKAO_SOCIAL_LOGIN_FAIL.getMessage(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void unlink(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();

            setHeaders(accessToken, headers);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            restTemplate.exchange(SocialType.KAKAO.getUnlinkUrl(),
                    SocialType.KAKAO.getUnlinkMethod(),
                    request,
                    RESPONSE_TYPE);

        } catch (Exception e) {
            log.error(ErrorStatus.KAKAO_SOCIAL_UNLINK_FAIL.getMessage(), e.getMessage());
            throw new ErrorHandler(ErrorStatus.KAKAO_SOCIAL_UNLINK_FAIL);
        }
    }
}

