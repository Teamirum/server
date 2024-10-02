package server.domain.credit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.credit.dto.CreditHistoryRequestDto;
import server.domain.credit.service.CreditHistoryService;
import server.domain.credit.service.CreditService;
import server.domain.member.service.MemberService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/credit/history")
@Slf4j
@RequiredArgsConstructor
public class CreditHistoryController {

    private final CreditHistoryService creditHistoryService;
    private final CreditService creditService;
    private  final MemberService memberService;

    // 카드 결제 내역 삽입
    @PostMapping
    public ApiResponse<?> insertCreditHistory(@RequestBody CreditHistoryRequestDto.UploadCreditHistoryRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        Long loginMemberIdx = memberService.findByMemberId(loginMemberId);
        Long creditIdx = requestDto.getCreditIdx();

        log.info("신용카드 결제 내역 삽입 요청 : memberId = {}, creditIdx = {}, amount = {}, name = {}, amountSum = {}",
                loginMemberId, creditIdx, requestDto.getAmount(), requestDto.getName(), requestDto.getAmountSum());

        Long creditMemberIdx = creditService.findCreditMemberIdxByCreditIdx(creditIdx);
        if (!creditMemberIdx.equals(loginMemberIdx)) {
            log.error("권한 오류: 로그인된 사용자와 결제 카드 소유자가 일치하지 않습니다. 로그인된 사용자 idx: {}, 카드 소유자 idx: {}", loginMemberIdx, creditMemberIdx);
            throw new ErrorHandler(ErrorStatus.UNAUTHORIZED);
        }

        return ApiResponse.onSuccess(creditHistoryService.insertCreditHistory(requestDto, String.valueOf(loginMemberIdx)));
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
