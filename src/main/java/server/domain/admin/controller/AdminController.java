package server.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.market.dto.MarketRequestDto;
import server.domain.market.service.MarketService;
import server.domain.member.domain.Member;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final MarketService marketService;

    @PostMapping("/market/register")
    public ApiResponse<?> registerMarket(@RequestBody MarketRequestDto.MarketCreateRequestDto requestDto) {
        String adminMemberId = getLoginMemberId();
        log.info("market 등록 요청. adminMemberId : {}, memberIdx : {}", adminMemberId, requestDto.getMemberIdx());
        return ApiResponse.onSuccess(marketService.registerMarket(requestDto));
    }

    @GetMapping("/market/all")
    public ApiResponse<?> getAllMarket() {
        log.info("모든 마켓 조회 요청. adminMemberId : {}", getLoginMemberId());
        return ApiResponse.onSuccess(marketService.getAllMarket());
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
