package server.domain.credit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.credit.domain.Credit;
import server.domain.credit.domain.CreditHistory;
import server.domain.credit.dto.*;
import server.domain.credit.mapper.CreditHistoryMapper;
import server.domain.credit.mapper.CreditMapper;
import server.domain.credit.repository.CreditHistoryRespository;
import server.domain.credit.repository.CreditRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.domain.transaction.domain.Transaction;
import server.domain.transaction.repository.TransactionRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static server.domain.transaction.domain.Transaction.Category.ENTERTAINMENT;
import static server.domain.transaction.domain.Transaction.PayMethod.ACCOUNT;
import static server.domain.transaction.domain.Transaction.PayMethod.CARD;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditService {

    private final CreditRepository creditRepository;
    private final MemberRepository memberRepository;
    private final CreditHistoryRespository creditHistoryRespository;
    private final TransactionRepository transactionRepository;

    // 신용카드 등록
    // POST /api/credit
    public CreditResponseDto.CreditTaskSuccessResponseDto upload(CreditRequestDto.UploadCreditRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (creditRepository.existsByCreditNumber(requestDto.getCreditNumber())) {
            throw new ErrorHandler(ErrorStatus.CREDIT_CARD_DUPLICATE);
        }
        Credit credit = Credit.builder()
                .memberIdx(memberIdx)
                .creditNumber(requestDto.getCreditNumber())
                .creditName(requestDto.getCreditName())
                .companyName(requestDto.getCompanyName())
                .creditSecret(requestDto.getCreditSecret())
                .amountSum(0)
                .expirationDate(requestDto.getExpirationDate())
                .imgUrl(requestDto.getImgUrl())
                .createdAt(LocalDateTime.now())
                .build();
        creditRepository.save(credit);

        Credit savedCredit = creditRepository.findByCreditNumber(credit.getCreditNumber()).orElseThrow(() -> new ErrorHandler(ErrorStatus.CREDIT_SAVE_FAIL));
        return CreditResponseDto.CreditTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedCredit.getIdx())
                .build();
    }

    // 신용카드 조회
    // GET /api/credit
    public CreditResponseDto.CreditListResponseDto getCreditList(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<Credit> creditList = creditRepository.findAllCreditByMemberIdx(memberIdx);
        return CreditDtoConverter.convertToCreditListResponseDto(creditList);
    }


    public CreditResponseDto.CreditTaskSuccessResponseDto delete(Long idx, String memberId) {
        Member member =  memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (!creditRepository.existsByCreditIdxAndMemberIdx(idx, member.getIdx())) {
            throw new ErrorHandler(ErrorStatus.CREDIT_CARD_NOT_FOUND);
        }

        creditRepository.delete(idx);
        return CreditResponseDto.CreditTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(idx)
                .build();
    }

   public Credit findMemberIdxAndCreditNumber(String memberId, String creditNumber) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return creditRepository.findMemberIdxAndCreditNumber(memberIdx, creditNumber).orElseThrow(() -> new ErrorHandler(ErrorStatus.CREDIT_NOT_FOUND));
    }


    @Transactional
    public CreditResponseDto.CreditTaskSuccessResponseDto updateAmountSum(Credit credit, Integer amountSum) {
        creditRepository.updateAmountSum(credit.getIdx(), credit.getAmountSum() + amountSum);

        return CreditResponseDto.CreditTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(credit.getIdx())
                .build();
    }


    @Transactional
    public void uploadCreditHistory(Credit credit, int amount, String name) {


        int creditAmountSum = credit.getAmountSum() + amount;
        // 카드 결제내역
        creditHistoryRespository.save(CreditHistory.builder()
                .creditIdx(credit.getIdx())
                .creditNumber(credit.getCreditNumber())
                .amount(amount)
                .amountSum(creditAmountSum)
                .name(name)
                .createdAt(LocalDateTime.now())
                .build());

        transactionRepository.save(Transaction.builder()
                .memberIdx(credit.getMemberIdx())
                .creditIdx(credit.getIdx())
                .creditNumber(credit.getCreditNumber())
                .time(now())
                .payMethod(CARD)
                .amount(amount)
                .memo(name)
                .category(ENTERTAINMENT)
                .build());
    }

    @Transactional
    public void payWithCredit(Credit credit, int price, String name) {
        creditRepository.payPrice(credit.getIdx(), price + credit.getAmountSum());
        uploadCreditHistory(credit, price, name);
    }

    public boolean isAbleToUseCredit(Credit credit) {
//        LocalDateTime expirationDate;
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 문자열 형식에 맞게 조정
//            expirationDate = LocalDateTime.parse(credit.getExpirationDate(), formatter);
//        } catch (DateTimeParseException e) {
//            throw new ErrorHandler(ErrorStatus.CREDIT_EXPIRATION_DATE_FORMAT_ERROR);
//        }
//
//        if (expirationDate.isBefore(LocalDateTime.now())) {
//            throw new ErrorHandler(ErrorStatus.CREDIT_CARD_EXPIRED);
//        }
        return true;
    }

    public Credit getCreditByIdx(Long creditIdx) {
        return creditRepository.findByCreditIdx(creditIdx).orElseThrow(() -> new ErrorHandler(ErrorStatus.CREDIT_CARD_NOT_FOUND));
    }

    public CreditHistoryResponseDto.CreditHistoryListResponseDto getCreditHistoryList(Long creditIdx, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if(!creditRepository.existsByCreditIdxAndMemberIdx(creditIdx, member.getIdx())) {
            throw new ErrorHandler(ErrorStatus.CREDIT_CARD_NOT_FOUND);
        }
        List<CreditHistory> creditHistoryList = creditHistoryRespository.findAllCreditHistoryByCreditIdx(creditIdx);
        return CreditHistoryDtoConverter.convertToCreditHistoryListResponseDto(creditHistoryList);
    }

    public CreditHistoryResponseDto.CreditHistoryDetailResponseDto getCreditHistoryDetail(Long creditIdx, Long creditHistoryIdx, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        CreditHistory creditHistory = creditHistoryRespository.findByCreditHistoryIdx(creditIdx, creditHistoryIdx, member.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.CREDIT_HISTORY_NOT_FOUND));
        return CreditHistoryResponseDto.CreditHistoryDetailResponseDto.builder()
                .isSuccess(true)
                .cnt(1)
                .creditHistoryDetail(CreditHistoryDtoConverter.convertToCreditHistoryInfoResponseDto(creditHistory))
                .build();

    }

}
