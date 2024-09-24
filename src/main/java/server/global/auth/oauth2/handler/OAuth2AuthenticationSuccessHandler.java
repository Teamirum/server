package server.global.auth.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.auth.security.domain.JwtToken;
import server.global.auth.security.dto.LoginResponseDto;
import server.global.auth.security.service.JwtService;
import server.global.util.DeviceUtil;
import server.global.util.RedisUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final MemberRepository memberRepository;

    private final RedisUtil redisUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String REFRESH_TOKEN = "refreshToken";

    private static final String ACCESS_TOKEN_KEY = "accessToken";



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String memberId = extractMemberId(authentication);
        JwtToken jwtToken = jwtService.createJwtToken(authentication);

        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.updateRefreshToken(jwtToken.getRefreshToken());
        memberRepository.updateRefreshToken(member);

        String device = DeviceUtil.getDevice(request);

        // Redis에 accessToken을 추가하고 만료 시간 설정
        redisUtil.addMultiData(device+ACCESS_TOKEN_KEY+memberId, jwtToken.getAccessToken(), jwtService.getAccessTokenExpirationTime());

        jwtService.sendAccessToken(response, jwtToken);
        log.info( "로그인에 성공합니다. memberId: {}" , memberId);
        log.info( "AccessToken 을 발급합니다. AccessToken: {}" ,jwtToken.getAccessToken());
        log.info( "RefreshToken 을 발급합니다. RefreshToken: {}" ,jwtToken.getRefreshToken());

        SecurityContext context = SecurityContextHolder.createEmptyContext();//5
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        LoginResponseDto.LoginDto loginDto = LoginResponseDto.LoginDto.builder()
                .memberId(memberId)
                .accessToken(jwtToken.getAccessToken())
                .role(member.getRole().getTitle())
                .build();

//		setCookieForLocal(response, jwtToken); // 개발단계에서 사용

        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.onSuccess(loginDto)));



        if(request.getServerName().equals("localhost")){
            setCookieForLocal(response, jwtToken);
        }
        else{
            setCookieForProd(response, jwtToken);
        }

    }

    private void setCookieForLocal(HttpServletResponse response, JwtToken jwtToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, jwtToken.getRefreshToken());
        cookie.setHttpOnly(false);  //httponly 옵션 설정
        cookie.setSecure(false); //https 옵션 설정
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
//		cookie.setDomain(jwtService.getDomain());
        cookie.setMaxAge(60 * 60 * 24); //쿠키 만료시간 24시간 * 10일

        response.addCookie(cookie);
    }

    private void setCookieForProd(HttpServletResponse response, JwtToken jwtToken) {
//		Cookie cookie = new Cookie(REFRESH_TOKEN, jwtToken.getRefreshToken());
//		cookie.setHttpOnly(true);  //httponly 옵션 설정
//		cookie.setSecure(true); //https 옵션 설정
//		cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
//		cookie.setMaxAge(60 * 60 * 24); //쿠키 만료시간 24시간
//
//		response.addCookie(cookie);

        String cookieValue = "refreshToken="+ jwtToken.getRefreshToken() +"; "+"Path=/; "+"Domain="+jwtService.getDomain()+"; "+"Max-Age=604800; HttpOnly; SameSite=None; Secure";
        response.addHeader("Set-Cookie",cookieValue);
    }


    private String extractMemberId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
    private String extractPassword(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getPassword();
    }
}
