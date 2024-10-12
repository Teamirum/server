package server.domain.transaction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.businessCard.dto.BusinessCardRequestDto;
import server.domain.transaction.dto.TransactionRequestDto;
import server.domain.transaction.dto.TransactionResponseDto;
import server.domain.transaction.service.TransactionService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @PostMapping
    public ApiResponse<?> uploadTransaction(@RequestBody TransactionRequestDto.UploadTransactionRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        return ApiResponse.onSuccess(transactionService.upload(requestDto, loginMemberId));
    }


    @GetMapping
    public ApiResponse<?> getTransaction(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("거래 조회 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(transactionService.getTransaction(idx, loginMemberId));
    }


    @GetMapping("/all")
    public ResponseEntity<TransactionResponseDto.TransactionListResponseDto> getAllTransactionsByMemberId() {
        String loginMemberId = getLoginMemberId();

        return ResponseEntity.ok(transactionService.getAllTransactionList(loginMemberId));
    }


    @DeleteMapping
    public ApiResponse<?> deleteTransaction(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(transactionService.delete(idx, loginMemberId));
    }

    @PatchMapping
    public ApiResponse<?> updateTransaction(
            @RequestParam(value = "idx") Long idx,
            @RequestBody TransactionRequestDto.UpdateTransactionRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        requestDto.setIdx(idx);
        log.info("거래 내역 수정 요청 : loginMemberId = {}, idx = {}, 수정 내용 = {}", loginMemberId, idx, requestDto);
        return ApiResponse.onSuccess(transactionService.update(requestDto, loginMemberId));
    }

    @GetMapping("/history/all")
    public ApiResponse<?> getTransactionHistory() {
        String loginMemberId = getLoginMemberId();
        log.info("거래 내역 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(transactionService.getTransactionHistory(loginMemberId));
    }


}
