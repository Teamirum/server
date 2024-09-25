package server.domain.market.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.global.apiPayload.ApiResponse;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> registerMarket() {
        log.info("registerMarket");
        return ApiResponse.onSuccess("registerMarket");
    }

}
