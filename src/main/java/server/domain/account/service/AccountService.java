package server.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.account.domain.Account;
import server.domain.account.dto.AccountDTO;
import server.domain.account.repository.AccountRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    // 계좌 조회
    public List<AccountDTO> getAccountsByMemberId(String memberId) {
        Member member = getMemberByMemberId(memberId);
        List<Account> accounts = accountRepository.findByMemberId(member.getIdx());
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // 계좌 저장
//    @Transactional // 트랜잭션 관리 추가
//    public void saveAccount(AccountDTO accountDTO) {
//        validateAccountDTO(accountDTO); // 유효성 검사
//        Account account = convertToEntity(accountDTO);
//        accountRepository.save(account);
//    }

    // 계좌 삭제
//    @Transactional // 트랜잭션 관리 추가
//    public void deleteAccount(Long accountId) {
//        if (!accountRepository.findById(accountId).isPresent()) {
//            throw new IllegalArgumentException("Account not found with ID: " + accountId);
//        }
//        accountRepository.deleteById(accountId);
//    }

    private Member getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }


    // Entity -> DTO 변환
    private AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .idx(account.getIdx())
                .memberIdx(account.getMemberIdx())
                .accountHolderName(account.getAccountHolderName())
                .amount(account.getAmount())
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .createdAt(account.getCreatedAt())
                .build();
    }

    // DTO -> Entity 변환
    private Account convertToEntity(AccountDTO accountDTO) {
        return Account.builder()
                .idx(accountDTO.getIdx())
                .memberIdx(accountDTO.getMemberIdx())
                .accountHolderName(accountDTO.getAccountHolderName())
                .amount(accountDTO.getAmount())
                .bankName(accountDTO.getBankName())
                .accountNumber(accountDTO.getAccountNumber())
                .createdAt(accountDTO.getCreatedAt())
                .build();
    }

    // 유효성 검사 메서드 추가
    private void validateAccountDTO(AccountDTO accountDTO) {
        if (accountDTO.getAmount() < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}
