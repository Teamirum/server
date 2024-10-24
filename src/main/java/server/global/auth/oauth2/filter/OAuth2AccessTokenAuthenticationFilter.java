package server.global.auth.oauth2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.auth.oauth2.domain.AccessTokenAuthenticationProvider;
import server.global.auth.oauth2.domain.AccessTokenSocialTypeToken;
import server.global.auth.oauth2.model.SocialType;
import server.global.auth.oauth2.model.socialLoader.GoogleLoadStrategy;
import server.global.auth.oauth2.model.socialLoader.KakaoLoadStrategy;
import server.global.auth.oauth2.model.socialLoader.NaverLoadStrategy;
import server.global.auth.oauth2.model.socialLoader.SocialLoadStrategy;
import server.global.config.OAuthProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@Slf4j
public class OAuth2AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/api/member/login/oauth2/";

    private static final String HTTP_METHOD = "POST";

//    private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

    private static final String ACCESS_TOKEN_PREFIX= "Bearer ";

    private final OAuthProperties oAuthProperties;




    private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX +"*", HTTP_METHOD);

    public OAuth2AccessTokenAuthenticationFilter(AccessTokenAuthenticationProvider accessTokenAuthenticationProvider,
                                                 AuthenticationSuccessHandler authenticationSuccessHandler,
                                                 AuthenticationFailureHandler authenticationFailureHandler, OAuthProperties oAuthProperties) {

        super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);
        this.oAuthProperties = oAuthProperties;

        this.setAuthenticationManager(new ProviderManager(accessTokenAuthenticationProvider));

        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);

    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SocialType socialType = extractSocialType(request, response);
        try {

            String authCode = request.getParameter("code");

            SocialLoadStrategy socialLoadStrategy = getSocialLoadStrategy(socialType);
            String accessToken = socialLoadStrategy.getAccessToken(authCode);
//          String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME).substring(ACCESS_TOKEN_PREFIX.length()); // Bearer이 두번 붙게되어, 최초 한번은 제외 (POSTMAN으로 테스트 시)

            return this.getAuthenticationManager().authenticate(new AccessTokenSocialTypeToken(ACCESS_TOKEN_PREFIX + accessToken, socialType));

        } catch (Exception e) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json");
            response.setStatus(ErrorStatus.SOCIAL_UNAUTHORIZED.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.onFailure(ErrorStatus.SOCIAL_UNAUTHORIZED.getCode(),
                    socialType.getSocialName() + " " + ErrorStatus.SOCIAL_UNAUTHORIZED.getMessage(), e.getMessage())));
            log.info("{} Authentication failed: " + e.getClass().toString() + " : " + e.getMessage(), socialType.getSocialName());
            return null;
        }
    }

    private SocialType extractSocialType(HttpServletRequest request, HttpServletResponse response) {
        log.info(request.getRequestURI().substring(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX.length()));
        return Arrays.stream(SocialType.values())
                .filter(socialType ->
                        socialType.getSocialName()
                                .equals(request.getRequestURI().substring(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX.length())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 URL 주소입니다"));
    }

    private SocialLoadStrategy getSocialLoadStrategy(SocialType socialType) {
        return switch (socialType){

            case KAKAO -> new KakaoLoadStrategy(oAuthProperties.getKakaoClientId(), oAuthProperties.getKakaoClientSecret());
            case GOOGLE ->  new GoogleLoadStrategy(oAuthProperties.getGoogleClientId(), oAuthProperties.getGoogleClientSecret());
            case NAVER ->  new NaverLoadStrategy(oAuthProperties.getNaverClientId(), oAuthProperties.getNaverClientSecret());
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 형식입니다");
        };
    }

}

