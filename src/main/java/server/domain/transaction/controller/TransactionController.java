package server.domain.transaction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // 특정 거래 조회
    @GetMapping("/{transactionIdx}")
    public ResponseEntity<TransactionResponseDto.TransactionInfoResponseDto> getTransaction(
            @PathVariable Long transactionIdx) {

        return ResponseEntity.ok(transactionService.getTransaction(transactionIdx));
    }


    @GetMapping("/all")
    public ResponseEntity<TransactionResponseDto.TransactionListResponseDto> getAllTransactionsByMemberId() {
        String loginMemberId = getLoginMemberId();

        return ResponseEntity.ok(transactionService.getAllTransactionList(loginMemberId));
    }

    // 거래 삭제
    @DeleteMapping
    public ApiResponse<?> deleteTransaction(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(transactionService.delete(idx, loginMemberId));
    }

}
