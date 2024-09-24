package server.global.auth.oauth2.model.socialLoader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kotlinx.serialization.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.auth.oauth2.model.SocialType;
import server.global.auth.oauth2.model.info.NaverOAuth2UserInfo;
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
public class NaverLoadStrategy extends SocialLoadStrategy{

    private String clientId;
    private String clientSecret;

    public NaverLoadStrategy(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getAccessToken(String authCode) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SocialType.NAVER.getTokenUrl()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=" + "authorization_code" +
                                "&client_id=" + clientId +
                                "&client_secret=" + clientSecret +
                                "&code=" + authCode +
                                "&state=" + URLEncoder.encode("1234", StandardCharsets.UTF_8)
                ))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        return jsonNode.get("access_token").asText();
    }

    protected OAuth2UserInfo sendRequestToSocialSite(HttpEntity request){
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(SocialType.NAVER.getUserInfoUrl(),//
                    SocialType.NAVER.getMethod(),
                    request,
                    RESPONSE_TYPE);


            return new NaverOAuth2UserInfo(response.getBody());


        } catch (Exception e) {
            log.error(ErrorStatus.NAVER_SOCIAL_LOGIN_FAIL.getMessage() ,e.getMessage() );
            throw e;
        }
    }

    @Override
    public void unlink(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();

            setHeaders(accessToken, headers);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("access_token", accessToken);
            params.add("grant_type", "delete");


            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            restTemplate.exchange(SocialType.NAVER.getUnlinkUrl(),
                    SocialType.NAVER.getUnlinkMethod(),
                    request,
                    RESPONSE_TYPE);

        } catch (Exception e) {
            log.error(ErrorStatus.KAKAO_SOCIAL_UNLINK_FAIL.getMessage(), e.getMessage());
            throw new ErrorHandler(ErrorStatus.KAKAO_SOCIAL_UNLINK_FAIL);
        }
    }
}
