package server.domain.transaction.controller;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // 거래 추가
    @PostMapping
    public ApiResponse<?> uploadTransaction(@RequestBody TransactionRequestDto.UploadTransactionRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        return ApiResponse.onSuccess(transactionService.upload(requestDto, loginMemberId));
    }

    // 특정 거래 조회
    @GetMapping("/{transactionIdx}")
    public ResponseEntity<TransactionResponseDto.TransactionInfoResponseDto> getTransaction(
            @PathVariable Long transactionIdx) {
        TransactionResponseDto.TransactionInfoResponseDto responseDto = transactionService.getTransaction(transactionIdx);
        return ResponseEntity.ok(responseDto);
    }

    // 특정 회원의 모든 거래 내역 조회
    @GetMapping
    public ResponseEntity<TransactionResponseDto.TransactionListResponseDto> getAllTransactionsByMemberId(
            @RequestHeader("memberId") String memberId) {
        TransactionResponseDto.TransactionListResponseDto responseDto = transactionService.getAllTransactionsByMemberId(memberId);
        return ResponseEntity.ok(responseDto);
    }

    // 거래 삭제
    @DeleteMapping("/{transactionIdx}")
    public ResponseEntity<TransactionResponseDto.TransactionTaskSuccessResponseDto> delete(
            @PathVariable Long transactionIdx,
            @RequestHeader("memberId") String memberId) {
        TransactionResponseDto.TransactionTaskSuccessResponseDto responseDto = transactionService.delete(transactionIdx, memberId);
        return ResponseEntity.ok(responseDto);
    }
    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
