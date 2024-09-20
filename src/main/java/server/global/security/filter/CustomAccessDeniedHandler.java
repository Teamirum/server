package server.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.security.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final JwtService jwtService;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        if (response.isCommitted()) {
            log.warn("Response has already been committed.");
            return;
        }

        if (accessToken.isPresent()) {
            Optional<String> memberId = jwtService.extractMemberId(accessToken.get());
            log.info("Access denied: memberId = " + memberId.get());
        }
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(ErrorStatus.MEMBER_NOT_AUTHENTICATED.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.onFailure(ErrorStatus.MEMBER_NOT_AUTHENTICATED.getCode(),
                ErrorStatus.MEMBER_NOT_AUTHENTICATED.getMessage(), null)));
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}