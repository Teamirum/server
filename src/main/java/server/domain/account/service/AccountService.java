package server.domain.account.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.account.domain.Account;
import server.domain.account.dto.AccountDtoConverter;
import server.domain.account.dto.AccountRequestDto;
import server.domain.account.dto.AccountResponseDto;
import server.domain.account.repository.AccountRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    public AccountResponseDto.AccountTaskSuccessResponseDto upload(AccountRequestDto.UploadAccountRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (accountRepository.existsByAccountNumber(requestDto.getAccountNumber())) {
            throw new ErrorHandler(ErrorStatus.ACCOUNT_DUPLICATE);
        }

        Account account = Account.builder()
                .memberIdx(memberIdx)
                .accountHolderName(requestDto.getAccountHolderName())
                .amount(Integer.valueOf(requestDto.getAmount()))
                .bankName(requestDto.getBankName())
                .accountNumber(requestDto.getAccountNumber())
                .createdAt(LocalDateTime.now())
                .accountSecret(requestDto.getAccountSecret())
                .build();
        accountRepository.save(account);

        Account savedAccount = accountRepository.findByAccountNumber(account.getAccountNumber()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ACCOUNT_SAVE_FAIL));
        return AccountResponseDto.AccountTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedAccount.getIdx())
                .build();
    }

    public AccountResponseDto.AccountListResponseDto getAccountList(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<Account> accountList = accountRepository.findAllAccountByMemberIdx(memberIdx);
        return AccountDtoConverter.convertToAccountListResponseDto(accountList);
    }

    public AccountResponseDto.AccountTaskSuccessResponseDto delete(Long idx, String memberId) {
       Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (!accountRepository.existsByAccountIdxAndMemberIdx(idx, member.getIdx())) {
            throw new ErrorHandler(ErrorStatus.ACCOUNT_NOT_FOUND);
        }
        accountRepository.delete(idx);
        return AccountResponseDto.AccountTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .build();
    }


    public AccountResponseDto.AccountTaskSuccessResponseDto updateAmount(AccountRequestDto.UpdateAccountAmountRequestDto requestDto) {
        // 주어진 idx로 계좌 금액 업데이트
        accountRepository.updateAccountAmount(requestDto.getIdx(), Integer.parseInt(requestDto.getAmount()));

        // 성공 응답 생성
        return AccountResponseDto.AccountTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(requestDto.getIdx())
                .build();
    }

}
