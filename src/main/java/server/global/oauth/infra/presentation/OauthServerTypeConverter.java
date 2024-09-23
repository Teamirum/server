package server.global.oauth.infra.presentation;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import server.global.oauth.OauthServerType;

@Component
public class OauthServerTypeConverter implements Converter<String, OauthServerType> {

    @Override
    public OauthServerType convert(String source) {
        return OauthServerType.fromName(source);
    }
}
