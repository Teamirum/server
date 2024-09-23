package server.global.oauth.domain.authcode;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.global.oauth.OauthServerType;

@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<OauthServerType, AuthCodeRequestUrlProvider> mapping;

    @Autowired
    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
        mapping = providers.stream()
                .collect(Collectors.toMap(
                        AuthCodeRequestUrlProvider::supportServer,
                        Function.identity()
                ));
    }

    public String provide(OauthServerType oauthServerType) {
        return getProvider(oauthServerType).provide();
    }

    private AuthCodeRequestUrlProvider getProvider(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 로그인 타입입니다."));
    }
}
