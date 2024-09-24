package server.global.auth.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import server.global.auth.oauth2.domain.AccessTokenSocialTypeToken;
import server.global.auth.oauth2.model.SocialType;
import server.global.auth.oauth2.model.info.OAuth2UserInfo;
import server.global.auth.oauth2.model.socialLoader.GoogleLoadStrategy;
import server.global.auth.oauth2.model.socialLoader.KakaoLoadStrategy;
import server.global.auth.oauth2.model.socialLoader.NaverLoadStrategy;
import server.global.auth.oauth2.model.socialLoader.SocialLoadStrategy;
import server.global.auth.security.domain.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class LoadMemberService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PasswordEncoder passwordEncoder;

    private static final String OAUTH2_USER_PASSWORD = "TEAM_OAUTH2_PW";


    public CustomUserDetails getOAuth2UserDetails(AccessTokenSocialTypeToken authentication)  {

        SocialType socialType = authentication.getSocialType();

        SocialLoadStrategy socialLoadStrategy = getSocialLoadStrategy(socialType);//SocialLoadStrategy 설정

        OAuth2UserInfo userInfo = socialLoadStrategy.getUserInfo(authentication.getAccessToken());//PK 가져오기

        return CustomUserDetails.builder() //PK와 SocialType을 통해 회원 생성
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .password(passwordEncoder.encode(OAUTH2_USER_PASSWORD))
                .name(userInfo.getName())
                .socialType(socialType)
                .build();
    }

    private SocialLoadStrategy getSocialLoadStrategy(SocialType socialType) {
        return switch (socialType){

            case KAKAO -> new KakaoLoadStrategy();
            case GOOGLE ->  new GoogleLoadStrategy();
            case NAVER ->  new NaverLoadStrategy();
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 형식입니다");
        };
    }


}

