package server.global.oauth.infra.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import server.global.oauth.OauthServerType;
import server.global.oauth.service.OauthService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/oauth")
public class OauthController {

    private final OauthService oauthService;

    @Autowired
    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    //@PathVariable로 받은 OauthServerType에 따라 인증 코드 요청 URL을 반환
    @GetMapping("/{oauthServerType}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        try {
            String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
    }
}
