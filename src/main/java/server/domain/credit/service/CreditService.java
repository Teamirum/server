package server.domain.credit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.credit.domain.Credit;
import server.domain.credit.domain.CreditHistory;
import server.domain.credit.dto.CreditDtoConverter;
import server.domain.credit.dto.CreditHistoryRequestDto;
import server.domain.credit.dto.CreditRequestDto;
import server.domain.credit.dto.CreditResponseDto;
import server.domain.credit.mapper.CreditHistoryMapper;
import server.domain.credit.repository.CreditRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final MemberRepository memberRepository;
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

    // 카드 결제 내역 조회
    public List<CreditHistory> getCreditHistoryList(Long idx, String loginMemberId) {
        // 카드의 소유자 확인 (로그인한 사용자가 해당 카드의 소유자인지 확인)
        if (!isCardOwner(idx, loginMemberId)) {
            throw new ErrorHandler(ErrorStatus.UNAUTHORIZED_ACCESS);
        }

        // 해당 카드의 결제 내역 조회
        return creditHistoryMapper.findAllByCreditIdx(idx);
    }

    // 카드 소유자 확인 (예시 메서드)
    private boolean isCardOwner(Long idx, String loginMemberId) {
        // 카드 소유자 확인 로직 추가
        Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 해당 카드가 로그인한 사용자의 카드인지 확인
        return creditRepository.findByCreditIdxAndMemberIdx(idx, memberIdx) != null;
    }

    public Long findCreditMemberIdxByCreditIdx(Long creditIdx) {
        return (Long) creditRepository.findMemberIdxByCreditIdx(creditIdx)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CREDIT_NOT_FOUND));
    }

}
