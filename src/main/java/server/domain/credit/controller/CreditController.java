package server.domain.credit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.credit.dto.CreditRequestDto;
import server.domain.credit.service.CreditService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/credit")
@Slf4j
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @PostMapping
    public ApiResponse<?> uploadCredit(@RequestBody CreditRequestDto.UploadCreditRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 업로드 요청 : memberId = {}, credit = {}", loginMemberId, requestDto.getCreditName());
        return ApiResponse.onSuccess(creditService.upload(requestDto, loginMemberId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getCreditList() {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 리스트 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(creditService.getCreditList(loginMemberId));
    }

    @DeleteMapping
    public ApiResponse<?> deleteCredit(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(creditService.delete(idx, loginMemberId));
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}