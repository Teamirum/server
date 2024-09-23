package server.global.oauth.domain.authcode;

import org.springframework.stereotype.Component;
import server.global.oauth.OauthServerType;

//Oauth2.0 인증을 제공하는 서버의 종류에 따라 인증코드 요청 URL을 제공하는 인터페이스
@Component
public interface AuthCodeRequestUrlProvider {

    //지원하는 Oauth2.0 인증 서버의 종류를 반환
    OauthServerType supportServer();

    //인증코드 요청 URL을 반환
    String provide();
}
