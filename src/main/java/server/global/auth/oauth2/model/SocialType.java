package server.global.auth.oauth2.model;

import org.springframework.http.HttpMethod;

public enum SocialType {


    KAKAO(
            "kakao",
            "https://kapi.kakao.com/v2/user/me",
            "https://kapi.kakao.com/v1/user/unlink",
            "https://kauth.kakao.com/oauth/token",
            "http://localhost:5173/login",
            HttpMethod.GET,
            HttpMethod.POST
    ),

    GOOGLE(
            "google",
            "https://www.googleapis.com/oauth2/v3/userinfo",
            "https://accounts.google.com/o/oauth2/revoke",
            "https://oauth2.googleapis.com/token",
            "http://localhost:5173/login",
            HttpMethod.GET,
            HttpMethod.POST
    ),

    NAVER(
            "naver",
            "https://openapi.naver.com/v1/nid/me",
            "https://nid.naver.com/oauth2.0/token",
            "https://nid.naver.com/oauth2.0/authorize",
            "http://localhost:5173/login",
            HttpMethod.GET,
            HttpMethod.POST
    ),
    LOCAL(
            "local",
            "",
            "",
            "",
            "",
            HttpMethod.GET,
            HttpMethod.POST
    )
    ;



    private final String socialName;
    private final String userInfoUrl;
    private final String unlinkUrl;
    private final String tokenUrl;
    private final String redirectUrl;
    private final HttpMethod method;
    private final HttpMethod unlinkMethod;

    SocialType(String socialName, String userInfoUrl, String unlinkUrl, String tokenUrl, String redirectUrl, HttpMethod method, HttpMethod unlinkMethod) {
        this.socialName = socialName;
        this.userInfoUrl = userInfoUrl;
        this.unlinkUrl = unlinkUrl;
        this.tokenUrl = tokenUrl;
        this.redirectUrl = redirectUrl;
        this.method = method;
        this.unlinkMethod = unlinkMethod;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpMethod getUnlinkMethod() {
        return unlinkMethod;
    }

    public String getSocialName() {
        return socialName;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getUnlinkUrl() {
        return unlinkUrl;
    }

    public static SocialType fromSocialName(String socialName) {
        for (SocialType socialType : SocialType.values()) {
            if (socialType.getSocialName().equalsIgnoreCase(socialName)) {
                return socialType;
            }
        }
        throw new IllegalArgumentException("Unknown social name: " + socialName);
    }
}