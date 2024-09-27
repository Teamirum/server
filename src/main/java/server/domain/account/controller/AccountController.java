package server.domain.account.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.domain.account.dto.AccountRequestDto;
import server.domain.account.service.AccountService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;


@RestController
@RequestMapping("/api/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

//    @GetMapping
//    public AccountResponseDto.AccountListResponseDto getAccountList(@AuthenticationPrincipal String memberId) {
//        return accountService.getAccountList(memberId);
//    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @PostMapping
    public ApiResponse<?> uploadAccount(@RequestBody AccountRequestDto.UploadAccountRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 업로드 요청 : memberId = {}, accountNumber = {}", loginMemberId, requestDto.accountNumber);
        return ApiResponse.onSuccess(accountService.upload(requestDto, loginMemberId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getAccountList() {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 리스트 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(accountService.getAccountList(loginMemberId));
    }

    @DeleteMapping
    public ApiResponse<?> deleteAccount(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(accountService.delete(idx, loginMemberId));
    }

    //계좌 amount 수정
    @PutMapping
    public ApiResponse<?> updateAccountAmount(@RequestBody AccountRequestDto.UpdateAccountAmountRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 amount 수정 요청 : loginMemberId = {}, idx = {}, amount = {}", loginMemberId, requestDto.idx, requestDto.amount);
        return ApiResponse.onSuccess(accountService.updateAmount(requestDto, loginMemberId));
    }



}
