package server.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.transaction.domain.Transaction;
import server.domain.transaction.dto.TransactionDtoConverter;
import server.domain.transaction.dto.TransactionRequestDto;
import server.domain.transaction.dto.TransactionResponseDto;
import server.domain.transaction.repository.TransactionRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;

    // 거래 추가
    public TransactionResponseDto.TransactionTaskSuccessResponseDto upload(TransactionRequestDto.UploadTransactionRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .memberIdx(memberIdx)
                .time(LocalDateTime.now())
                .payMethod(Transaction.PayMethod.valueOf(requestDto.getPayMethod()))
                .amount(requestDto.getAmount())
                .category(Transaction.Category.valueOf(requestDto.getCategory()))
                .build();

        // 트랜잭션을 저장합니다. 이 때, JPA가 ID를 자동으로 설정합니다.
        transactionRepository.save(transaction);

        // 저장된 트랜잭션 객체에서 ID를 가져옵니다.
        Long transactionIdx = transaction.getIdx(); // 이 때 ID는 null입니다.

        // ID를 가져오고, 이후 findByTransactionIdx로 확인합니다.
        Transaction savedTransaction = transactionRepository.findByTransactionIdx(transactionIdx)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.TRANSACTION_SAVE_FAIL));

        System.out.println(" @@@@@= " + savedTransaction.getIdx());

        return TransactionResponseDto.TransactionTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedTransaction.getIdx())
                .build();
    }


    public Transaction getTransaction(Long idx, String loginMemberId) {
       Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId)
               .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

       Transaction transaction = transactionRepository.findByTransactionIdx(idx)
               .orElseThrow(() -> new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND));

       if(!transaction.getMemberIdx().equals(memberIdx)) {
              throw new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND);
       }

       return transaction;
    }

    // 특정 회원의 모든 거래 내역 조회
    public TransactionResponseDto.TransactionListResponseDto getAllTransactionList(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return TransactionDtoConverter.convertToTransactionListResponseDto(transactionRepository.findAllTransactionByMemberIdx(memberIdx));
    }

    public TransactionResponseDto.TransactionTaskSuccessResponseDto delete(Long idx, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if(!transactionRepository.existsByTransactionIdxAndMemberIdx(idx, member.getIdx())) {
            throw new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND);
        }

        transactionRepository.delete(idx);

        return TransactionResponseDto.TransactionTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(idx)
                .build();
    }

    public TransactionResponseDto.TransactionTaskSuccessResponseDto update(TransactionRequestDto.UpdateTransactionRequestDto requestDto, String loginMemberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Transaction transaction = transactionRepository.findByTransactionIdx(requestDto.getIdx())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND));

        if (!transaction.getMemberIdx().equals(memberIdx)) {
            throw new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND);
        }

        transaction.setPayMethod(Transaction.PayMethod.valueOf(requestDto.getPayMethod()));
        transaction.setAmount(requestDto.getAmount());
        transaction.setMemo(requestDto.getMemo());
        transaction.setCategory(Transaction.Category.valueOf(requestDto.getCategory()));
        transaction.setTranId(requestDto.getTranId());

        transactionRepository.updateTransaction(transaction);


        return TransactionResponseDto.TransactionTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(transaction.getIdx())
                .build();
    }

    public TransactionResponseDto.TransactionListResponseDto getTransactionHistory(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Transaction> transactionList = transactionRepository.findAllTransactionByMemberIdx(memberIdx);

        return TransactionDtoConverter.convertToTransactionListResponseDto(transactionList);
    }
}
