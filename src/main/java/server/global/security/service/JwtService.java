package server.global.security.service;


import org.springframework.security.core.Authentication;
import server.global.security.domain.JwtToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface JwtService {

	JwtToken createJwtToken(String memberId, String password);
	JwtToken createJwtToken(Authentication authentication);
	String reIssueAccessToken(String memberId);
	String reIssueAndSaveRefreshToken(String memberId);

	void destroyRefreshToken(String email);

	void sendAccessToken(HttpServletResponse response, JwtToken jwtToken);
	void sendReIssuedAccessToken(HttpServletResponse response, String token);

	Optional<String> extractAccessToken(HttpServletRequest request);

	Optional<String> extractRefreshToken(HttpServletRequest request);

	Optional<String> extractMemberId(String accessToken);

	void setAccessTokenHeader(HttpServletResponse response, String token);

	boolean isTokenValid(String token);

	String getDomain();

	long getAccessTokenExpirationTime();

	String createEmailAuthToken(String email);

	boolean isValidEmailAuthToken(String accessToken, String email);

}
