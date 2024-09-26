package server.domain.credit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.credit.domain.Credit;
import server.domain.credit.dto.CreditRequestDto;
import server.domain.credit.dto.CreditResponseDto;
import server.domain.credit.repository.CreditRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final MemberRepository memberRepository;

    public CreditResponseDto.CreditTaskSuccessResponseDto upload(CreditRequestDto.UploadCreditRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId);
        Credit credit = Credit.builder()
                .memberIdx(memberIdx)
                .creditNumber(requestDto.getCreditNumber())
                .creditName(requestDto.getCreditName())
                .companyName(requestDto.getCompanyName())
                .creditSecret(requestDto.getCreditSecret())
                .amountSum(0)
                .build();
        creditRepository.save(credit);

        Credit savedCredit = creditRepository.findByCreditIdx(credit.getIdx()).orElseThrow(() -> new ErrorHandler(ErrorStatus.CREDIT_SAVE_FAIL));
        return CreditResponseDto.CreditTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(savedCredit.getIdx())
                .build();
    }

}
