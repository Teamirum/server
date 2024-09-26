package server.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.account.dto.AccountDTO;
import server.domain.account.service.AccountService;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")  // 요청 매핑을 /api/account로 변경
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    //내 계좌 조회
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccountsByMemberId() {
        String memberId = getLoginMemberId();
        List<AccountDTO> accountDTOs = accountService.getAccountsByMemberId(memberId);


        if (accountDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();  // 계좌가 없을 경우 204 No Content 응답
        }

        return ResponseEntity.ok(accountDTOs);  // 계좌 정보가 있을 경우 200 OK 응답
    }
//    public ResponseEntity<List<AccountDTO>> getAccountsByMemberId(@RequestParam Long memberId) {
//        return ResponseEntity.ok(accountService.getAccountsByMemberId(memberId));
//    }

    // 계좌 저장
//    @PostMapping
//    public ResponseEntity<Void> saveAccount(@RequestBody AccountDTO accountDTO) {
//        accountService.saveAccount(accountDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    // 계좌 삭제
//    @DeleteMapping
//    public ResponseEntity<Void> deleteAccount(@RequestParam Long idx) {
//        accountService.deleteAccount(idx);
//        return ResponseEntity.noContent().build();
//    }

    // 계좌 잔액 업데이트
//    @PutMapping("/{accountId}/update")
//    public ResponseEntity<Void> updateAccountBalance(@PathVariable Long accountId, @RequestBody Double newBalance) {
//        accountService.updateAccountBalance(accountId, newBalance);
//        return ResponseEntity.noContent().build();
//    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
