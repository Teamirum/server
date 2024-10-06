package server.domain.credit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.credit.domain.Credit;
import server.domain.credit.domain.CreditHistory;
import server.domain.credit.dto.CreditDtoConverter;
import server.domain.credit.dto.CreditHistoryRequestDto;
import server.domain.credit.dto.CreditRequestDto;
import server.domain.credit.dto.CreditResponseDto;
import server.domain.credit.mapper.CreditHistoryMapper;
import server.domain.credit.mapper.CreditMapper;
import server.domain.credit.repository.CreditRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditService {

    private final CreditRepository creditRepository;
    private final MemberRepository memberRepository;
    private final CreditMapper creditMapper;
    private final CreditHistoryMapper creditHistoryMapper;

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
    public CreditResponseDto.CreditTaskSuccessResponseDto
    updateAmountSum(Credit credit, Integer amountSum) {
        creditRepository.updateAmountSum(credit.getIdx(), credit.getAmountSum() + amountSum);

        return CreditResponseDto.CreditTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(credit.getIdx())
                .build();
    }





}
