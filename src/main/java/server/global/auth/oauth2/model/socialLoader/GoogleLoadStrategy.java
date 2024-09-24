package server.global.auth.oauth2.model.socialLoader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.auth.oauth2.model.SocialType;
import server.global.auth.oauth2.model.info.GoogleOAuth2UserInfo;
import server.global.auth.oauth2.model.info.OAuth2UserInfo;
import server.global.config.OAuthProperties;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class GoogleLoadStrategy extends SocialLoadStrategy{

    private String clientId;
    private String clientSecret;

    public GoogleLoadStrategy(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getAccessToken(String authCode) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SocialType.GOOGLE.getTokenUrl()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=" + "authorization_code" +
                                "&client_id=" + clientId +
                                "&client_secret=" + clientSecret +
                                "&code=" + authCode +
                                "&redirect_uri=" + SocialType.GOOGLE.getRedirectUrl()
                ))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        return jsonNode.get("access_token").asText();
    }

    protected OAuth2UserInfo sendRequestToSocialSite(HttpEntity request){
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(SocialType.GOOGLE.getUserInfoUrl(),
                    SocialType.GOOGLE.getMethod(),
                    request,
                    RESPONSE_TYPE);

            return new GoogleOAuth2UserInfo(response.getBody());

        } catch (Exception e) {
            log.error(ErrorStatus.KAKAO_SOCIAL_LOGIN_FAIL.getMessage() ,e.getMessage() );
            throw e;
        }
    }

    @Override
    public void unlink(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();

//            setHeaders(accessToken, headers);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("token", accessToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            restTemplate.exchange(SocialType.GOOGLE.getUnlinkUrl(),
                    SocialType.GOOGLE.getUnlinkMethod(),
                    request,
                    RESPONSE_TYPE);

        } catch (Exception e) {
            log.error(ErrorStatus.GOOGLE_SOCIAL_UNLINK_FAIL.getMessage(), e.getMessage());
            throw new ErrorHandler(ErrorStatus.GOOGLE_SOCIAL_UNLINK_FAIL);
        }
    }
}



