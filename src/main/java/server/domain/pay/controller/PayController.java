package server.domain.pay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.pay.dto.PayRequestDto;
import server.domain.pay.service.PayService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
@Slf4j
public class PayController {

    private final PayService payService;

    @GetMapping("/payMethod/all")
    public ApiResponse<?> getAllPayMethod() {
        String memberId = getLoginMemberId();
        log.info("결제 수단 조회 : memberId = {}", memberId);
        return ApiResponse.onSuccess(payService.getAllPayMethod(memberId));
    }

    @PostMapping("/together")
    public ApiResponse<?> startPay(@RequestBody PayRequestDto.StartPayRequestDto requestDto) {
        String memberId = getLoginMemberId();
        log.info("결제 요청 : memberId = {}, orderIdx = {}", memberId, requestDto.getOrderIdx());
        if (requestDto.getPayType().equals("TOGETHER")) {
            return ApiResponse.onSuccess(payService.startTogetherPay(requestDto, memberId));
        }
        // 개인 결제는 아래
        return ApiResponse.onSuccess(payService.startTogetherPay(requestDto, memberId));
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
