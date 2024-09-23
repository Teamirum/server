package server.global.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.global.oauth.OauthServerType;
import server.global.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;

//OauthServerType에 따라 인증 코드 요청 URL을 반환하는 서비스
@Service
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;

    @Autowired
    public OauthService(AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite) {
        this.authCodeRequestUrlProviderComposite = authCodeRequestUrlProviderComposite;
    }

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }
}
