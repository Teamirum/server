package server.domain.account.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.account.domain.Account;
import server.domain.account.domain.AccountHistory;
import server.domain.account.dto.*;
import server.domain.account.repository.AccountHistoryRepository;
import server.domain.account.repository.AccountRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.chrono.ChronoLocalDateTime.from;
import static server.domain.account.domain.AccountHistory.AccountHistoryType.GET;
import static server.domain.account.domain.AccountHistory.AccountHistoryType.SEND;

@Service
@RequiredArgsConstructor

public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AccountHistoryRepository accountHistoryRepository;


    public AccountResponseDto.AccountTaskSuccessResponseDto upload(AccountRequestDto.UploadAccountRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (accountRepository.existsByAccountNumber(requestDto.getAccountNumber())) {
            throw new ErrorHandler(ErrorStatus.ACCOUNT_DUPLICATE);
        }

        if (existsByAccountNumber(requestDto.getAccountNumber())) {
            throw new ErrorHandler(ErrorStatus.ACCOUNT_DUPLICATE);
        }

        Account account = Account.builder()
                .memberIdx(memberIdx)
                .accountHolderName(requestDto.getAccountHolderName())
                .amount(Integer.valueOf(requestDto.getAmount()))
                .bankName(requestDto.getBankName())
                .accountNumber(requestDto.getAccountNumber())
                .createdAt(now())
                .accountSecret(requestDto.getAccountSecret())
                .build();
        accountRepository.save(account);

        Account savedAccount = accountRepository.findByAccountNumber(account.getAccountNumber()).orElseThrow(() -> new ErrorHandler(ErrorStatus.ACCOUNT_SAVE_FAIL));
        return AccountResponseDto.AccountTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedAccount.getIdx())
                .build();
    }

    private boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
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
        accountRepository.updateAccountAmount(requestDto.getIdx(), Integer.parseInt(requestDto.getAmount()));
        return AccountResponseDto.AccountTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(requestDto.getIdx())
                .build();
    }

    @Transactional
    public void uploadAccountHistory(Account fromAccount, Account toAccount, Integer amount, String name) {

        Integer fromAccountAmount = fromAccount.getAmount() - amount;
        Integer toAccountAmount = toAccount.getAmount() + amount;

        accountHistoryRepository.save(AccountHistory.builder()
                .accountIdx(fromAccount.getIdx())
                .accountHistoryType(SEND)
                .createdAt(now())
                .amount(amount)
                .remainAmount(fromAccountAmount)
                .name(name)
                .build());
        accountHistoryRepository.save(AccountHistory.builder()
                .accountIdx(toAccount.getIdx())
                .accountHistoryType(GET)
                .createdAt(now())
                .amount(amount)
                .remainAmount(toAccountAmount)
                .name(name)
                .build());
    }


    public AccountHistoryResponseDto.AccountHistoryListResponseDto getAccountHistoryList(Long accountIdx, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (!accountRepository.existsByAccountIdxAndMemberIdx(accountIdx, member.getIdx())) {
            throw new ErrorHandler(ErrorStatus.ACCOUNT_NOT_FOUND);
        }
        List<AccountHistory> accountHistoryList = accountHistoryRepository.findAllAccountHistoryByAccountIdx(accountIdx);
        return AccountHistoryDtoConverter.convertToAccountHistoryListResponseDto(accountHistoryList);
    }

    public Account findByAccountIdx(Long idx) {
        return accountRepository.getIdxByAccountIdx(idx).orElseThrow(() -> new ErrorHandler(ErrorStatus.ACCOUNT_NOT_FOUND));
    }

    public Account findByMemberIdxAndAccountNumber(String loginMemberId, String fromAccountNumber) {
        Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return accountRepository.findByMemberIdxAndAccountNumber(memberIdx, fromAccountNumber).orElseThrow(() -> new ErrorHandler(ErrorStatus.ACCOUNT_NOT_FOUND));
    }

    public Account findByAccountNumber(String toAccountNumber) {
        return accountRepository.findByAccountNumber(toAccountNumber).orElseThrow(() -> new ErrorHandler(ErrorStatus.ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public AccountResponseDto.AccountTaskSuccessResponseDto updateAmount(Account fromAccount ,Account toAccount, Integer amount) {
        if(fromAccount.getAmount() < amount){
            throw new ErrorHandler(ErrorStatus.ACCOUNT_NOT_ENOUGH_AMOUNT);
        }

        int fromAccountAmount = fromAccount.getAmount() - amount;
        int toAccountAmount = toAccount.getAmount() + amount;

        // 계좌 송금
        accountRepository.updateAccountAmount(fromAccount.getIdx(), fromAccountAmount);
        // 계좌 입금
        accountRepository.updateAccountAmount(toAccount.getIdx(), toAccountAmount);

        // success
        return AccountResponseDto.AccountTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(fromAccount.getIdx())
                .build();
    }
}